package no.nmdc.module.reindex.service;

import java.util.Map;

/**
 * The validation service api.
 *
 * @author kjetilf
 */
public interface ReindexService {

    /**
     * Reindex file.
     *
     * @param body  The body as map.
     * @return  The new body.
     */
    Map<String,Object> reindex(Map<String,Object> body);

}
