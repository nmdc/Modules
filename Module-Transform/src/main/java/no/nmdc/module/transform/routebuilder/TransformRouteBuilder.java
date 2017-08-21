package no.nmdc.module.transform.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
                .choice()
                    .when(header("format").isEqualTo("dif"))                                      
                        .to("log:start-dif?level=INFO&showHeaders=true&showBody=false")
                        .to("xslt:DIFToNMDC.xsl?saxon=false")
                        .to(FILE_CMP_NAME + moduleConf.getString("nmdc.savedir") + CHARSET_PARAMETER)        
                        .to("jms:queue:nmdc/harvest-transform-html")
                        .to("http4://test1.nmdc.no:8983/solr/nmdc/update/xml/docs")
                    .when(header("format").isEqualTo("iso-19139"))
                        .to("log:start-iso?level=INFO&showHeaders=true&showBody=false")
                        .to("xslt:ISO19139ToNMDC.xsl?saxon=false")
                        .to(FILE_CMP_NAME + moduleConf.getString("nmdc.savedir") + CHARSET_PARAMETER)
                        .to("jms:queue:nmdc/harvest-transform-html")                        
                    .otherwise()
                        .to("log:failure?level=WARN&showHeaders=true&showBody=false")
                        .to(QUEUE_ERROR)
                .end();
    }
}
