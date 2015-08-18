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
        from("jms:queue:nmdc/harvest-transform")
                .errorHandler(deadLetterChannel(QUEUE_ERROR).maximumRedeliveries(MAXIMUM_REDELIVERIES).redeliveryDelay(REDELIVERY_DELAY))
                .choice()
                .when(header("format").isEqualTo("dif"))
                .to("log:end?level=INFO&showHeaders=true&showBody=false")
                .to(FILE_CMP_NAME + moduleConf.getString("dif.savedir") + CHARSET_PARAMETER)
                .to("xslt:DIFToNMDC.xsl?saxon=true")
                .to(FILE_CMP_NAME + moduleConf.getString("nmdc.savedir") + CHARSET_PARAMETER)
                .when(header("format").isEqualTo("iso-19139"))
                .to("log:end?level=INFO&showHeaders=true&showBody=false")
                .to("xslt:ISO19139ToNMDC.xsl?saxon=true")
                .to(FILE_CMP_NAME + moduleConf.getString("nmdc.savedir") + CHARSET_PARAMETER)
                .to("xslt:ISO19139ToDIF.xsl?saxon=true")
                .to(FILE_CMP_NAME + moduleConf.getString("dif.savedir") + CHARSET_PARAMETER)
                .otherwise()
                .to("log:end?level=WARN&showHeaders=true&showBody=false")
                .to(QUEUE_ERROR)
                .endChoice()
                .end();
    }
}
