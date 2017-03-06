
package no.nmdc.module.validate.service;

import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.camel.OutHeaders;

/**
 *
 * @author kjetilf
 */
public interface ValidationErrorService {
    
    /**
     * 
     * @param in
     * @param payload
     * @param out
     * @return 
     */
    Object validate(@Headers Map<?, ?> in, @Body String payload, @OutHeaders Map<String, Object> out);
    
}
