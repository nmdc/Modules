package no.nmdc.module.reindex.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.solr.SolrConstants;
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
     * Module properties.
     */
    @Autowired
    @Qualifier("moduleConf")
    private PropertiesConfiguration moduleConf;

    /**
     * The route
     * 1. Get message from the validation jms.
     * 2. Reindex them in the service.
     * 3. Send them to the transformation queue.
     *
     * @return  The route.
     */
    @Override
    public RouteBuilder route() {
        return new RouteBuilder() {

            @Override
            public void configure() {
               from("jms:queue:nmdc/harvest-reindex")
                       .errorHandler(deadLetterChannel("jms:queue:nmdc/harvest-failure").maximumRedeliveries(3).redeliveryDelay(30000))
                       .setHeader(SolrConstants.OPERATION, constant(SolrConstants.OPERATION_INSERT))
                       .setHeader(SolrConstants.FIELD + "id", body())
                       .to("solr://" + moduleConf.getString("solr.url"))
                       .setHeader(SolrConstants.OPERATION, constant(SolrConstants.OPERATION_COMMIT))
                       .to("solr://" + moduleConf.getString("solr.url"))
                       .to("log:end?level=INFO");
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing.
    }

}
