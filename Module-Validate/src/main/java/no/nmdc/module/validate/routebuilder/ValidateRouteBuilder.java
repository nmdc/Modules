package no.nmdc.module.validate.routebuilder;

import no.nmdc.module.validate.service.ValidationErrorService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;

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
            .add("srv", "http://www.isotc211.org/2005/srv");

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
        from("jms:queue:nmdc/harvest-validate")
                .errorHandler(deadLetterChannel(QUEUE_ERROR).maximumRedeliveries(MAXIMUM_REDELIVERIES).redeliveryDelay(REDELIVERY_DELAY))
                .to("log:begin?level=INFO&showHeaders=true&showBody=false")
                .doTry()
                    .choice()
                        .when().xpath("/*:DIF", DIF_NAMESPACES)
                            .setHeader("did",xpath("//*:Entry_ID/text()", String.class))
                            .setHeader("dname",xpath("//*:Entry_Title/text()", String.class))
                            .setHeader("dinst",xpath("//*:Dataset_Publisher/text()", String.class))
                            .to("validator:dif.xsd?useDom=false")
                            .setHeader("format", simple("dif"))
                            .to("log:end?level=INFO&showHeaders=true&showBody=false")
                            .to("jms:queue:nmdc/harvest-transform")
                            .to("jms:queue:nmdc/harvest-transform-dif")
                        .when().xpath("/*:MD_Metadata", ISO19139_NAMESPACES)
                            .setHeader("did",xpath("//*:fileIdentifier/*:CharacterString/text()", String.class))
                            .setHeader("dname",xpath("//*:title/*:CharacterString/text()", String.class))
                            .setHeader("dinst",xpath("//*:organisationName/*:CharacterString/text()", String.class))
                            .to("validator:iso19139.xsd?useDom=false")
                            .setHeader("format", simple("iso-19139"))
                            .to("log:end?level=INFO&showHeaders=true&showBody=false")
                            .to("jms:queue:nmdc/harvest-transform")
                            .to("jms:queue:nmdc/harvest-transform-dif")
                        .otherwise()
                            .to("log:end?level=WARN&showHeaders=true&showBody=false")
                            .to(QUEUE_ERROR)
                    .endChoice()
                .endDoTry()
                .doCatch(org.apache.camel.ValidationException.class)                
                    .setHeader("exception",simple("${property.CamelExceptionCaught}"))                
                    .to("log:end?level=WARN&showHeaders=true&showBody=false&showCaughtException=true")                               
                    .bean(validationErrorService, "validate")
                .end();
    }

}
