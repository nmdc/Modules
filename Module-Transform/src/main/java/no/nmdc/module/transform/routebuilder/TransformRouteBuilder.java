package no.nmdc.module.transform.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Defines the route the transformation is to go through.
 *
 * @author kjetilf
 */
public class TransformRouteBuilder extends RouteBuilder {

    /**
     * Redelivery delay.
     */
    private static final int REDELIVERY_DELAY = 30000;

    /**
     * Maximum number of redeliveries.
     */
    private static final int MAXIMUM_REDELIVERIES = 3;

    /**
     * Error jms queue.
     */
    private static final String QUEUE_ERROR = "jms:queue:nmdc/harvest-failure";

    /**
     * Charset parameter used in routes.
     */
    private static final String CHARSET_PARAMETER = "?charset=utf-8";

    /**
     * File component camel name.
     */
    private static final String FILE_CMP_NAME = "file:";

    /**
     * This modules properties.
     */
    @Autowired
    @Qualifier("moduleConf")
    private PropertiesConfiguration moduleConf;
    
    @Override
    public void configure() {
        onException(Exception.class).to("log:GeneralError?level=ERROR&showHeaders=true&showBody=true&showException=true");
        
        from("jms:queue:nmdc/harvest-transform")
                .to("log:failure?level=WARN&showHeaders=true&showBody=false")
                    .choice()
                        .when(header("format").isEqualTo("dif"))
                            .to("direct:transformDif")
                        .when(header("format").isEqualTo("iso-19139"))
                            .to("direct:transformIso")
                        .otherwise()
                            .to(QUEUE_ERROR)
                    .endChoice()
                .end();
        
        from("direct:transformDif")
                .to("xslt:DIFToNMDC.xsl?saxon=false&output=string")
                .multicast()
                .to("direct:saveFile", "direct:index", "direct:transformHtml");
        
        from("direct:transformIso")
                .to("xslt:ISO19139ToNMDC.xsl?saxon=false&output=string")
                .multicast()
                .to("direct:saveFile", "direct:index", "direct:transformHtml");
        
        from("direct:saveFile")
                .to(FILE_CMP_NAME + moduleConf.getString("nmdc.savedir") + CHARSET_PARAMETER);
        
        from("direct:index")
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/xml; charset=UTF-8"))                
                .to("http4://" + moduleConf.getString("solr.url") + "/solr/nmdc/update?commitWithin=30000&authUsername=" + moduleConf.getString("solr.username") + "&authPassword=" + moduleConf.getString("solr.password") + "&authenticationPreemptive=true&tr=update.xslt");
        
        from("direct:transformHtml")
                .to("jms:queue:nmdc/harvest-transform-html");
        
    }
}
