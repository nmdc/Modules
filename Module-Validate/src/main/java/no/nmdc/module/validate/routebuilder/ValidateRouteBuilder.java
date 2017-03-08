package no.nmdc.module.validate.routebuilder;

import no.nmdc.module.validate.service.ValidationErrorService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.builder.xml.XPathBuilder;

/**
 * Defines the route for the validation.
 *
 * @author kjetilf
 */
public class ValidateRouteBuilder extends RouteBuilder {

    /**
     * Redelivery delay.
     */
    private static final int REDELIVERY_DELAY = 30000;

    /**
     * Maximum redeliveries.
     */
    private static final int MAXIMUM_REDELIVERIES = 3;

    /**
     * DIF format namespace.
     */
    private static final Namespaces DIF_NAMESPACES = new Namespaces("dif", "http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/")
            .add("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    private static final Namespaces ISO19139_NAMESPACES = new Namespaces("gmd", "http://www.isotc211.org/2005/gmd")
            .add("xs", "http://www.w3.org/2001/XMLSchema")
            .add("gmx", "http://www.isotc211.org/2005/gmx")
            .add("srv", "http://www.isotc211.org/2005/srv")
            .add("gco", "http://www.isotc211.org/2005/gco");

    /**
     * Error jms queue.
     */
    private static final String QUEUE_ERROR = "jms:queue:nmdc/harvest-failure";
    
    private final ValidationErrorService validationErrorService;

    public ValidateRouteBuilder(ValidationErrorService validationErrorService) {
        this.validationErrorService = validationErrorService;
    }

    @Override
    public void configure() throws Exception {
        XPathBuilder difDid = xpath("//dif:Entry_ID/text()", String.class).namespaces(DIF_NAMESPACES);
        XPathBuilder difDname = xpath("//dif:Entry_Title/text()", String.class).namespaces(DIF_NAMESPACES);
        XPathBuilder difDinst = xpath("//dif:Dataset_Publisher/text()", String.class).namespaces(DIF_NAMESPACES);
        XPathBuilder isoDid = xpath("//gmd:fileIdentifier/gco:CharacterString/text()", String.class).namespaces(ISO19139_NAMESPACES);
        XPathBuilder isoDname = xpath("//gmd:title/gco:CharacterString/text()", String.class).namespaces(ISO19139_NAMESPACES);
        XPathBuilder isoDinst = xpath("//gmd:organisationName/gco:CharacterString/text()", String.class).namespaces(ISO19139_NAMESPACES);      
        from("jms:queue:nmdc/harvest-validate")
                .errorHandler(deadLetterChannel(QUEUE_ERROR).maximumRedeliveries(MAXIMUM_REDELIVERIES).redeliveryDelay(REDELIVERY_DELAY))
                .to("log:begin?level=INFO&showHeaders=true&showBody=false")
                .doTry()
                .choice()
                        .when().xpath("/dif:DIF", DIF_NAMESPACES)
                            .setHeader("did", difDid)
                            .setHeader("dname", difDname)
                            .setHeader("dinst", difDinst) 
                        .when().xpath("/gmd:MD_Metadata", ISO19139_NAMESPACES)
                            .setHeader("did", isoDid)
                            .setHeader("dname", isoDname)
                            .setHeader("dinst", isoDinst)                        
                        .otherwise()
                            .to("log:end?level=WARN&showHeaders=true&showBody=false")
                    .endChoice()
                .endDoTry()
                .doCatch(org.apache.camel.ValidationException.class, RuntimeException.class)                
                    .setHeader("exception",simple("${property.CamelExceptionCaught}"))                
                    .to("log:end?level=WARN&showHeaders=true&showBody=false&showCaughtException=true")                               
                .end()
                .doTry()
                    .choice()
                        .when().xpath("/dif:DIF", DIF_NAMESPACES)                         
                            .to("validator:dif.xsd?useDom=false")
                            .setHeader("format", simple("dif"))
                            .to("log:end?level=INFO&showHeaders=true&showBody=false")
                            .to("jms:queue:nmdc/harvest-transform")
                            .to("jms:queue:nmdc/harvest-transform-dif")                            
                        .when().xpath("/gmd:MD_Metadata", ISO19139_NAMESPACES)
                            .to("validator:iso19139.xsd")
                            .setHeader("format", simple("iso-19139"))
                            .to("log:end?level=INFO&showHeaders=true&showBody=false")
                            .to("jms:queue:nmdc/harvest-transform")
                            .to("jms:queue:nmdc/harvest-transform-dif")
                        .otherwise()
                            .to("log:end?level=WARN&showHeaders=true&showBody=false")
                            .to(QUEUE_ERROR)
                    .endChoice()
                .endDoTry()
                .doCatch(org.apache.camel.ValidationException.class, RuntimeException.class)                
                    .setHeader("exception",simple("${property.CamelExceptionCaught}"))                
                    .to("log:end?level=WARN&showHeaders=true&showBody=false&showCaughtException=true")                               
                    .bean(validationErrorService, "validate")
                .end();
    }

}
