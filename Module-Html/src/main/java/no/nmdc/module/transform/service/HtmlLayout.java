package no.nmdc.module.transform.service;

import java.io.IOException;
import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.camel.OutHeaders;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface HtmlLayout {
    
    
    String process(@Headers Map<String, Object> headers,@Body String payload, @OutHeaders Map<String, Object> out) throws IOException;
    
    

}
