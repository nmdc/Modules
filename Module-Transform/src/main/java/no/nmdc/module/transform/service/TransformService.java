package no.nmdc.module.transform.service;

import java.util.Map;

/**
 * The validation service api.
 *
 * @author kjetilf
 */
public interface TransformService {

    /**
     * Validate file.
     *
     * @param body  The body as map.
     * @return  The new body.
     */
    Map<String,Object> transform(Map<String,Object> body);

}
