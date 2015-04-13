package no.nmdc.module.validate.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
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
     * DIF format namespace.
     */
    private static final Namespaces DIF_NAMESPACE = new Namespaces("dif", "http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/").add("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

    @Override
    public RouteBuilder route() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:queue:nmdc/harvest-validate")
                        .errorHandler(deadLetterChannel("jms:queue:nmdc/harvest-failure").maximumRedeliveries(3).redeliveryDelay(30000))
                        .to("log:begin?level=INFO&showHeaders=true&showBody=false")
                        .doTry()
                        .choice()
                        .when().xpath("/dif:DIF", DIF_NAMESPACE)
                        .to("validator:dif.xsd")
                        .setHeader("format", simple("dif"))
                        .to("log:end?level=INFO&showHeaders=true&showBody=false")
                        .to("jms:queue:nmdc/harvest-reindex")
                        .otherwise()
                        .to("log:end?level=WARN&showHeaders=true&showBody=false")
                        .to("jms:queue:nmdc/harvest-failure")
                        .endChoice().endDoTry()
                        .doCatch(org.apache.camel.ValidationException.class)
                        .to("log:end?level=WARN&showHeaders=true&showBody=false&showCaughtException=true")
                        .to("jms:queue:nmdc/harvest-failure")
                        .end();
            }

        };
    }


}
