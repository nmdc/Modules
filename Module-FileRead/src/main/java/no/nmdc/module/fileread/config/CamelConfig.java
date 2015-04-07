package no.nmdc.module.fileread.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * The route configuration for the file reader.
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
     * 1. Get all modified files in a directory.
     * 2. Process the files and store the filename and other parameters in the body as map (Change this to pojo later).
     * 3. Transmit the body to the validation queue.
     *
     * If error occurs store the information in the dead queue after three tries.
     *
     * @return  The specified route.
     */
    @Override
    public RouteBuilder route() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + moduleConf.getString("loaddir") + "?noop=true&idempotentKey=${file:name}-${file:modified}")
                        .to("bodyProcessor")
                        .to("log:end?level=INFO")
                        .errorHandler(deadLetterChannel("jms:queue:dead").maximumRedeliveries(3).redeliveryDelay(30000))
                        .to("jms:queue:nmdc/harvest-validate");
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

}
