package no.nmdc.module.transform.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * The validation implementation.
 *
 * @author kjetilf
 */
@Service(value = "transformService")
public class TransformServiceImpl implements TransformService {

    @Override
    public Map<String,Object> transform(Map<String,Object> body) {
        Map<String,Object> params = new HashMap(body);
        params.put("NMDCFileName", "Something later");
        return params;
    }

}
