package no.nmdc.module.validate.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * The validation implementation.
 *
 * @author kjetilf
 */
@Service(value = "validateService")
public class ValidateServiceImpl implements ValidateService {

    @Override
    public Map<String,Object> validate(Map<String,Object> body) {
        Map<String,Object> params = new HashMap(body);
        params.put("validated", true);
        return params;
    }

}
