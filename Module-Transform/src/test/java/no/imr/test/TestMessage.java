package no.imr.test;

import java.io.File;
import java.io.IOException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 *
 * @author kjetilf
 */
public class TestMessage {

    @Test
    public void test() throws JMSException, IOException {
//        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//        Connection connection = connectionFactory.createConnection();
//        connection.start();
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination destination = session.createQueue("nmdc/harvest-validate");
//        MessageProducer producer = session.createProducer(destination);
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//        Message message = session.createTextMessage(FileUtils.readFileToString(new File("c:\\nmdc2\\test.xml"), "UTF-8"));
//        message.setStringProperty("format", "dif");
//        
//        producer.send(message);
//        session.close();
//        connection.close();
    }

}
