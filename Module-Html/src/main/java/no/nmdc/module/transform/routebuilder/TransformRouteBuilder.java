package no.nmdc.module.transform.routebuilder;

import no.nmdc.module.transform.service.HtmlLayout;
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

    @Autowired
    HtmlLayout htmlLayout;
    
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
        from("jms:queue:nmdc/harvest-transform-html")
                .errorHandler(deadLetterChannel(QUEUE_ERROR).maximumRedeliveries(MAXIMUM_REDELIVERIES).redeliveryDelay(REDELIVERY_DELAY))
                .to("log:end?level=INFO&showHeaders=true&showBody=false")
                .to("xslt:transformtodivs.xsl?saxon=true")
                .to(FILE_CMP_NAME + moduleConf.getString("div.savedir") + CHARSET_PARAMETER)   
                .bean(htmlLayout, "process")
                .to(FILE_CMP_NAME + moduleConf.getString("html.savedir") + CHARSET_PARAMETER)   
                .to("log:end?level=WARN&showHeaders=true&showBody=false")
                .to(QUEUE_ERROR)
                .end();
    }
}