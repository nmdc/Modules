package no.nmdc.module.error.routes.attachment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author kjetilf
 */
@Component
public class SendErrorEmailImpl implements SendErrorEmail {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendErrorEmailImpl.class);

    @Autowired
    @Qualifier("moduleConf")
    private Configuration configuration;

    @Override
    public void sendEmail() {
        String checkFile = configuration.getString("mail.checkFile");
        File file = new File(checkFile);
        if (file.exists() && file.length() > 0) {
            LOGGER.info("File above 0 size and is being prepared.");
            List<String> toList = (List) configuration.getList("mail.to");
            String from = configuration.getString("mail.from");
            String host = configuration.getString("mail.server");
            String subject = configuration.getString("mail.subject");
            
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(properties);
            try {
                String messageText = getMessage(file);
                MimeMessage message = new MimeMessage(session);
		message.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
		message.setHeader("Content-Transfer-Encoding", "quoted-printable");           
                message.setText(messageText, "UTF-8");
                message.setFrom(new InternetAddress(from));
                for (String to : toList) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }
                
                message.setSubject(subject, "UTF-8");
                Transport.send(message);
                LOGGER.info("Message sent successfully.");
            } catch (IOException | MessagingException ex) {
                LOGGER.error("Error sending mail.", ex);
            }
        } else {
            LOGGER.info("Message not sent because it is 0 size.");
        }
    }

    private String getMessage(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(configuration.getString("mail.pretext"));
        builder.append(FileUtils.readFileToString(file));
        builder.append(new String(configuration.getString("mail.posttext").getBytes(), "UTF-8"));
        LOGGER.info("Message: " + new String(configuration.getString("mail.posttext").getBytes(), "UTF-8"));
        return builder.toString();
    }

}
