package no.nmdc.module.validate.process;

import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author kjetilf
 */
public class ValidationProcessor implements Processor {

    @Override
    public void process(Exchange exchng) throws Exception {
        
    }

}
