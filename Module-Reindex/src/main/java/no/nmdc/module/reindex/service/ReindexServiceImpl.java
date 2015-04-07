package no.nmdc.module.reindex.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * The validation implementation.
 *
 * @author kjetilf
 */
@Service(value = "reindexService")
public class ReindexServiceImpl implements ReindexService {

    @Override
    public Map<String,Object> reindex(Map<String,Object> body) {
        Map<String,Object> params = new HashMap(body);
        params.put("reindexed", true);
        return params;
    }

}
