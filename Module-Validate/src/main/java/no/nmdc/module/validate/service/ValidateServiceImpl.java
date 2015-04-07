package no.nmdc.module.validate.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 * The validation implementation.
 *
 * @author kjetilf
 */
@Service(value = "validateService")
public class ValidateServiceImpl implements ValidateService {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateServiceImpl.class);

    /**
     * File to xsd schema for the DIF format.
     */
    private static final File DIF_SCHEMA_FILE =  new File(Thread.currentThread().getContextClassLoader().getResource("dif.xsd").getFile());

    @Override
    public Map<String, Object> validate(final Map<String, Object> body) {
        Map<String, Object> params = new HashMap(body);
        if (validate((String) params.get("filename"), DIF_SCHEMA_FILE)) {
            params.put("validated", true);
            params.put("format", "dif");
        } else {
            params.put("validated", false);
        }
        return params;
    }

    /**
     * Validates input file.
     *
     * @param filename File to validate.
     * @param schemaFile Schema file.
     * @return Validation result.
     */
    private boolean validate(final String filename, final File schemaFile) {
        boolean result = false;
        Source xmlFile = new StreamSource(new File(filename));
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            LOGGER.info("File {] is DIF format. ", filename);
            result = true;
        } catch (SAXException | IOException ex) {
            LOGGER.info("File is not DIF format. ", ex);
        }
        return result;
    }

}
