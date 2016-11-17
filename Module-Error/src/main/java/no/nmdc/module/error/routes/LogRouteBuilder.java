package no.nmdc.module.error.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

/**
 * Defines the route for the logging errors.
 *
 * @author kjetilf
 */
@Service
public class LogRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:nmdc/harvest-failure")
                .to("log:end?level=INFO&showHeaders=true&showBody=false");
    }

}
