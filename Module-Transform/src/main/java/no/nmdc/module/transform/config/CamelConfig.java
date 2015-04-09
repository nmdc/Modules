package no.nmdc.module.transform.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * The route copnfiguration for the validation.
 *
 * @author kjetilf
 */
@Configuration
public class CamelConfig extends SingleRouteCamelConfiguration implements InitializingBean {

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
                       .errorHandler(deadLetterChannel("jms:queue:dead").maximumRedeliveries(3).redeliveryDelay(30000))
                       .choice()
                            .when(header("format").isEqualTo("dif"))
                                .to("log:end?level=INFO")
                                .to("jms:queue:nmdc/harvest-reindex")
                            .otherwise()
                                .to("xslt:")
                                .to("log:end?level=INFO")
                                .to("jms:queue:nmdc/harvest-reindex");
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

}
