package no.nmdc.module.validate.process;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.camel.ValidationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import no.nmdc.module.validate.process.pojo.ScienceKeyword;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kjetilf
 */
public class DifValidationProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DifValidationProcessor.class);
    private Set<ScienceKeyword> validKeywords = new HashSet<ScienceKeyword>();

    public DifValidationProcessor() {
        try {            
            CSVParser csvp = CSVParser.parse(DifValidationProcessor.class.getClassLoader().getResourceAsStream("sciencekeywords.csv"), Charset.forName("UTF-8"), CSVFormat.newFormat(';'));
            for (CSVRecord record : csvp.getRecords()) {
                validKeywords.add(new ScienceKeyword(record.get(0)));
                if (record.get(1) != null && record.get(1).length() > 0) {
                    validKeywords.add(new ScienceKeyword(record.get(0), record.get(1)));
                }
                if (record.get(2) != null && record.get(2).length() > 0) {
                    validKeywords.add(new ScienceKeyword(record.get(0), record.get(1), record.get(2)));
                }
                if (record.get(3) != null && record.get(3).length() > 0) {
                    validKeywords.add(new ScienceKeyword(record.get(0), record.get(1), record.get(2), record.get(3)));
                }
                if (record.get(4) != null && record.get(4).length() > 0) {
                    validKeywords.add(new ScienceKeyword(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4)));
                }
                if (record.get(5) != null && record.get(5).length() > 0) {
                    validKeywords.add(new ScienceKeyword(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4), record.get(5)));
                }
                if (record.get(6) != null && record.get(6).length() > 0) {
                    validKeywords.add(new ScienceKeyword(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4), record.get(5), record.get(6)));
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error loading science keywords.", ex);
        }
    }

    @Override
    public void process(Exchange exchng) throws Exception {
        SAXBuilder jdomBuilder = new SAXBuilder();
        Document jdomDocument = jdomBuilder.build(new StringReader(exchng.getIn().getBody(String.class)));
        List<Element> parameters = jdomDocument.getRootElement().getChildren("Parameters", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));
        for (Element parameter : parameters) { 
            ScienceKeyword scienceKeyword = getScienceKeyword(parameter);
            if (!validKeywords.contains(scienceKeyword)) {
                throw new ValidationException(exchng, "Invalid parameter keyword: " + scienceKeyword.toString());
            }
        }
    }

    private ScienceKeyword getScienceKeyword(Element parameter) {
        String category = parameter.getChildText("Category", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));
        String topic = parameter.getChildText("Topic", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));
        String term = parameter.getChildText("Term", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));
        String var1 = parameter.getChildText("Variable_Level_1", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));
        String var2 = parameter.getChildText("Variable_Level_2", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));
        String var3 = parameter.getChildText("Variable_Level_3", Namespace.getNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"));        
        ScienceKeyword scienceKeyword = 
                new ScienceKeyword(
                        category != null && category.length() > 0 ? category : null, 
                        topic != null && topic.length() > 0 ? topic : null, 
                        term != null && term.length() > 0 ? term : null, 
                        var1 != null && var1.length() > 0 ? var1 : null, 
                        var2 != null && var2.length() > 0 ? var2 : null, 
                        var3 != null && var3.length() > 0 ? var3 : null, 
                        null);
        return scienceKeyword;
    }

}
