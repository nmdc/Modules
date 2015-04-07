package no.nmdc.module.validate.service;

import java.util.Map;

/**
 * The validation service api.
 *
 * @author kjetilf
 */
public interface ValidateService {

    /**
     * Validate file.
     *
     * @param body  The body as map.
     * @return  The new body.
     */
    Map<String,Object> validate(Map<String,Object> body);

}
