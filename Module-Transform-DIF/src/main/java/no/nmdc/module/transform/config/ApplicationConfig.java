package no.nmdc.module.transform.config;

import no.nmdc.module.transform.routebuilder.TransformRouteBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This imports and prepares the configuration files.
 *
 * @author kjetilf
 */
@Configuration
public class ApplicationConfig {

    /**
     * Import the general activemq configuration.
     *
     * @return  The activemq configuration properties.
     * @throws ConfigurationException   Could not import.
     */
    @Bean(name = "activeMQConf")
    public PropertiesConfiguration getActiveMQConfiguration() throws ConfigurationException {
        PropertiesConfiguration conf = new PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/activemq.properties");
        conf.setReloadingStrategy(new FileChangedReloadingStrategy());
        return conf;
    }

    /**
     * Import this modules configuration.
     *
     * @return  Configuration properties.
     * @throws ConfigurationException       Could not import.
     */
    @Bean(name = "moduleConf")
    public PropertiesConfiguration getFileReaderConfiguration() throws ConfigurationException {
        PropertiesConfiguration conf = new PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/module-transform-dif.properties");
        conf.setReloadingStrategy(new FileChangedReloadingStrategy());
        return conf;
    }

    /**
     * Create the route builder.
     *
     * @return route builder.
     */
    @Bean(name = "routeBuilder")
    public TransformRouteBuilder routeBuilder() {
        return new TransformRouteBuilder();
    }

}
