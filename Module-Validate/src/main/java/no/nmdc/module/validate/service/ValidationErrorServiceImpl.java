
package no.nmdc.module.validate.service;

import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.camel.OutHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kjetilf
 */
public class ValidationErrorServiceImpl implements ValidationErrorService {    
    
    /**
     * 
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationErrorServiceImpl.class);
    
    @Override
    public Object validate(@Headers Map<?, ?> in, @Body String payload, @OutHeaders Map<String, Object> out) {
        String exception = in.get("exception").toString();
        String message = exception.substring(exception.indexOf("cvc-complex-type"), exception.indexOf("]"));
        LOGGER.info("Feil funnet i datasett: {} ", in.get("dname").toString());
        LOGGER.info("Institusjon: {}.", in.get("dinst").toString());
        LOGGER.info("Feilmelding: {}", message);
        return null;
    }
    
}
