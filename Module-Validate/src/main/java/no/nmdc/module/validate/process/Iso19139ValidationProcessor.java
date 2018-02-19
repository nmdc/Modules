package no.nmdc.module.validate.process;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import no.nmdc.module.validate.process.pojo.ScienceKeyword;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jdom2.Content;
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
public class Iso19139ValidationProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iso19139ValidationProcessor.class);
    private Set<ScienceKeyword> validKeywords = new HashSet<ScienceKeyword>();

    public Iso19139ValidationProcessor() {
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
        boolean hasThesaursOnKeywords = hasThesaurusOnKeywords(jdomDocument);
        Iterator<Content> contents = jdomDocument.getRootElement().getDescendants();
        while (contents.hasNext()) {
            Content content = contents.next();
            if (content.getParentElement().getName().equals("keyword")) {
                ScienceKeyword scienceKeyword = getScienceKeyword(content.getParentElement());
                if (hasThesaursOnKeywords) {
                    if (isGcmdKeyword(content.getParentElement())) {
                        if (!validKeywords.contains(scienceKeyword)) {
                            throw new ValidationException(exchng, "Invalid parameter keyword: " + scienceKeyword.toString());
                        }
                    }
                } else if (!validKeywords.contains(scienceKeyword)) {
                    throw new ValidationException(exchng, "Invalid parameter keyword: " + scienceKeyword.toString());
                }
            }
        }
    }

    private boolean hasThesaurusOnKeywords(Document jdomDocument) {
        Iterator<Content> contents = jdomDocument.getRootElement().getDescendants();
        while (contents.hasNext()) {
            Content content = contents.next();
            if (content.getParentElement().getName().equals("thesaurusName")) {
                return true;
            }
        }
        return false;
    }

    private ScienceKeyword getScienceKeyword(Element parameter) {
        String[] parameters = parameter.getValue().split(">");
        String category = "EARTH SCIENCE";
        String topic = null;
        String term = null;
        String var1 = null;
        String var2 = null;
        String var3 = null;
        if (parameters.length > 0 && parameters[0].trim().length() > 0) {
            topic = parameters[0].trim();
        }
        if (parameters.length > 1 && parameters[1].trim().length() > 0) {
            term = parameters[1].trim();
        }
        if (parameters.length > 2 && parameters[2].trim().length() > 0) {
            var1 = parameters[2].trim();
        }
        if (parameters.length > 3 && parameters[3].trim().length() > 0) {
            var2 = parameters[3].trim();
        }
        if (parameters.length > 4 && parameters[4].trim().length() > 0) {
            var3 = parameters[4].trim();
        }
        ScienceKeyword scienceKeyword
                = new ScienceKeyword(
                        category,
                        topic,
                        term,
                        var1,
                        var2,
                        var3,
                        null);
        return scienceKeyword;
    }

    private boolean isGcmdKeyword(Element element) {        
        Element thesarus = element.getParentElement()
                .getChild("thesaurusName" ,Namespace.getNamespace("http://www.isotc211.org/2005/gmd"))
                .getChild("CI_Citation" ,Namespace.getNamespace("http://www.isotc211.org/2005/gmd"))
                .getChild("title" ,Namespace.getNamespace("http://www.isotc211.org/2005/gmd"))
                .getChild("CharacterString" ,Namespace.getNamespace("http://www.isotc211.org/2005/gco"));
        return thesarus.getValue().contains("GCMD");
        
    }

}
