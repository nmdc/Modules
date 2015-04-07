package no.nmdc.module.fileread.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure activeMQ/JMS.
 *
 *
 * @author kjetilf
 */
@Configuration
public class ActiveMQConfig {

    /**
     * Active mq properties.
     */
    @Autowired
    @Qualifier("activeMQConf")
    private PropertiesConfiguration activeMQConf;

    /**
     * This modules properties.
     */
    @Autowired
    @Qualifier("moduleConf")
    private PropertiesConfiguration moduleConf;

    /**
     * Active MQ connection factory.
     *
     * @return activeMQConnectionFactory
     */
    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(activeMQConf.getString("activemq.brokerurl"));
        return factory;
    }

    /**
     * Create PooledConnectionFactory.
     *
     * @return PooledConnectionFactory
     */
    @Bean
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setMaxConnections(activeMQConf.getInt("max.connections"));
        factory.setConnectionFactory(jmsConnectionFactory());
        factory.initConnectionsPool();
        return factory;
    }

    /**
     * Configure JMS.
     *
     * @return JmsConfiguration
     */
    @Bean
    public JmsConfiguration jmsConfig() {
        JmsConfiguration conf = new JmsConfiguration();
        conf.setConnectionFactory(pooledConnectionFactory());
        conf.setConcurrentConsumers(moduleConf.getInt("max.consumers"));
        return conf;
    }

    /**
     * Configure ActiveMQComponent.
     *
     * @return  Component.
     */
    @Bean
    public ActiveMQComponent activemq() {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConfiguration(jmsConfig());
        return activeMQComponent;
    }


}
