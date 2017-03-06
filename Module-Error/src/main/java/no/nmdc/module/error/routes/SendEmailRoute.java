package no.nmdc.module.error.routes;

import java.io.File;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import no.nmdc.module.error.routes.attachment.SendErrorEmail;

/**
 *
 * @author kjetilf
 */
@Service
public class SendEmailRoute extends RouteBuilder {

    @Autowired
    @Qualifier("moduleConf")
    private Configuration configuration;
    
    @Autowired
    private SendErrorEmail sendEmailAttachment;
    
    @Override
    public void configure() throws Exception {
        String mailCron = configuration.getString("mail.cron");
        from(mailCron)
                .to("log:end?level=INFO&showHeaders=true&showBody=false")
                .bean(sendEmailAttachment, "sendEmail");
    }

}
