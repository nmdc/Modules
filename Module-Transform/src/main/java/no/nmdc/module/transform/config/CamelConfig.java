package no.nmdc.module.transform.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * The route copnfiguration for the validation.
 *
 * @author kjetilf
 */
@Configuration
public class CamelConfig extends SingleRouteCamelConfiguration implements InitializingBean {

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

    /**
     * The route
     * 1. Get message from the validation jms.
     * 2. Transform them in the service.
     * 3. Send them to the transformation queue.
     *
     * @return  The route.
     */
    @Override
    public RouteBuilder route() {
        return new RouteBuilder() {

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
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

}
