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
                       .errorHandler(deadLetterChannel("jms:queue:nmdc/harvest-failure").maximumRedeliveries(3).redeliveryDelay(30000))
                       .choice()
                            .when(header("format").isEqualTo("dif"))
                                .to("log:end?level=INFO")
                                .to("xslt:DifToNMDC.xsl?saxon=true")
                                .to("file:" + moduleConf.getString("savedir") + "?charset=utf-8")
                                .to("jms:queue:nmdc/harvest-reindex")
                            .when(header("format").isEqualTo("iso-19139"))
                                .to("log:end?level=INFO")
                                .to("xslt:ISO19139ToNMDC.xsl?saxon=true")
                                .to("file:" + moduleConf.getString("savedir") + "?charset=utf-8")
                                .to("jms:queue:nmdc/harvest-reindex")
                            .otherwise()
                                .to("log:end?level=WARN")
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
