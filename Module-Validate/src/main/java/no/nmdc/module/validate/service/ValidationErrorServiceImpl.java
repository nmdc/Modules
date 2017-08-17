
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
        LOGGER.info("Identifier: {} ", in.get("did") != null ? in.get("did").toString() : " Ukjent");
        LOGGER.info("Feil funnet i datasett: {} ", in.get("dname") != null ? in.get("dname").toString() : " Ukjent");
        LOGGER.info("Institusjon: {}.",  in.get("dinst") != null ? in.get("dinst").toString() : " Ukjent");
        LOGGER.info("Feilmelding: {}", exception);
        return null;
    }
    
}
