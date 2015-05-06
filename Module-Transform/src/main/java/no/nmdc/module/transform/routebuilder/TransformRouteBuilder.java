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
     * This modules properties.
     */
    @Autowired
    @Qualifier("moduleConf")
    private PropertiesConfiguration moduleConf;

    @Override
    public void configure() {
        from("jms:queue:nmdc/harvest-transform")
                .errorHandler(deadLetterChannel("jms:queue:nmdc/harvest-failure").maximumRedeliveries(MAXIMUM_REDELIVERIES).redeliveryDelay(REDELIVERY_DELAY))
                .choice()
                .when(header("format").isEqualTo("dif"))
                .to("log:end?level=INFO&showHeaders=true&showBody=false")
                .to("file:" + moduleConf.getString("dif.savedir") + "?charset=utf-8")
                .to("xslt:DIFToNMDC.xsl?saxon=true")
                .to("file:" + moduleConf.getString("nmdc.savedir") + "?charset=utf-8")
                .when(header("format").isEqualTo("iso-19139"))
                .to("log:end?level=INFO&showHeaders=true&showBody=false")
                .to("xslt:ISO19139ToDIF.xsl?saxon=true")
                .to("file:" + moduleConf.getString("dif.savedir") + "?charset=utf-8")
                .to("xslt:ISO19139ToNMDC.xsl?saxon=true")
                .to("file:" + moduleConf.getString("nmdc.savedir") + "?charset=utf-8")
                .otherwise()
                .to("log:end?level=WARN&showHeaders=true&showBody=false")
                .to("jms:queue:nmdc/harvest-failure")
                .endChoice()
                .end();
    }
}
