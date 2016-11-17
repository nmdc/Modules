package no.nmdc.module.error.config;

import java.util.Arrays;
import java.util.List;
import no.nmdc.module.error.routes.LogRouteBuilder;
import no.nmdc.module.error.routes.SendEmailRoute;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * The route configuration for the validation.
 *
 * @author kjetilf
 */
@Configuration
public class CamelConfig extends CamelConfiguration implements InitializingBean {

    @Autowired
    private SendEmailRoute sendEmailRoute;

    @Autowired
    private LogRouteBuilder logRouteBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

    @Override
    public List<RouteBuilder> routes() {
        return Arrays.asList(logRouteBuilder, sendEmailRoute);
    }

}
