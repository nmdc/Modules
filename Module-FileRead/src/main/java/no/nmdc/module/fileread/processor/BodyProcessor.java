package no.nmdc.module.fileread.processor;

import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Creates the body as a map with the required parameters.
 *
 * @author kjetilf
 */
public class BodyProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> params = new HashMap();
        params.put("filename", exchange.getIn().getHeader("CamelFileAbsolutePath"));
        exchange.getOut().setBody(params);
    }

}
