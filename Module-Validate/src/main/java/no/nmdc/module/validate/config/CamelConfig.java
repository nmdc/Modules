package no.nmdc.module.validate.config;

import no.nmdc.module.validate.routebuilder.ValidateRouteBuilder;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

    @Override
    public RouteBuilder route() {
        return new ValidateRouteBuilder();
    }

}
