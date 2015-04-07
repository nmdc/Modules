package no.nmdc.module.validate.service;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test validation service.
 *
 * @author kjetilf
 */
public class TestValidateServiceImpl {

    /**
     * Testing valid DIF file.
     */
    @Test
    public void testDifValidation() {
        ValidateService validService = new ValidateServiceImpl();
        Map<String, Object> params = new HashMap<>();
        params.put("filename", Thread.currentThread().getContextClassLoader().getResource("Lista.xml").getFile());
        Map<String, Object> res = validService.validate(params);
        assertTrue((boolean) res.get("validated"));
    }

    /**
     * Testing against invalid DIF file.
     */
    @Test
    public void testDifValidationInvalid() {
        ValidateService validService = new ValidateServiceImpl();
        Map<String, Object> params = new HashMap<>();
        params.put("filename", Thread.currentThread().getContextClassLoader().getResource("Bud.xml").getFile());
        Map<String, Object> res = validService.validate(params);
        assertFalse((boolean) res.get("validated"));
    }
}
