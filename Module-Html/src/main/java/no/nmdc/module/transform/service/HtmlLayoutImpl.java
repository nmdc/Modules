package no.nmdc.module.transform.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class HtmlLayoutImpl implements HtmlLayout {

    /**
     * This modules properties.
     */
    @Autowired
    @Qualifier("moduleConf")
    private PropertiesConfiguration moduleConf;

    private Resource template = new ClassPathResource("template.html");

    @Override
    public String process(Map<String, Object> headers, String payload, Map<String, Object> out) throws IOException {

        Document sourceDoc = Jsoup.parse(payload);

        Document templateDoc;
        templateDoc = Jsoup.parse(template.getFile(), "UTF-8", "");

        //templateDoc.select("#dataset-xml").first().text(source.substring(source.lastIndexOf('/') + 1));
        templateDoc.select("#dataset-xml").first().text((String) headers.get("CamelFileRelativePath"));

        templateDoc.select("#data-center").first().text(collectText(sourceDoc, "#Originating_Center"));
        templateDoc.select("#doi").first().text(collectText(sourceDoc, ".Dataset_DOI"));
        String title = collectText(sourceDoc, ".Dataset_Set_Citation", ".DataSet_Title");
        if (title.isEmpty()) {
            title = collectText(sourceDoc, "#Entry_Title");
        }
        templateDoc.select("#dataset-title").first().text(title);

        Elements citationList = sourceDoc.select(".Data_Set_Citation");
        if (citationList.size() > 1) {
            templateDoc.select("#recomended-citation-label").first().text("Recommended citations:");
        }
        Element citation_list = templateDoc.select("#cite-list").first();
        Element citation_template = templateDoc.select(".recomended-citation").first();
        citation_template.remove();
        
        for (Element element : citationList) {
            Element citation =citation_template.clone();
            citation.select(".cite-creator").first().text(element.select(".Dataset_Creator").text());
            String citeDate = element.select(".Dataset_Release_Date").text();
            if (citeDate.length()>4){
                citeDate = "("+citeDate.substring(0,4)+")";
            }
            citation.select(".cite-date").first().text(citeDate);
            citation.select(".cite-title").first().text(element.select(".Dataset_Title").text());
            citation.select(".cite-doi").first().text(element.select(".Dataset_DOI").text());
            citation_list.appendChild(citation);
        }
        templateDoc.select("#use-constraints").first().text(collectText(sourceDoc, "#Use_Constraints"));
        templateDoc.select("#abstract").first().text(collectText(sourceDoc, "#Summary"));

        Element science_keywords = templateDoc.select("#science_keywords").first();
        Element science_keyword_template = templateDoc.select(".science_keyword").first();
        science_keyword_template.remove();

        for (Element element : sourceDoc.select(".Parameters")) {
            StringBuilder text = new StringBuilder();
            text.append(element.select(".Category").text());
            text.append("> ");
            text.append(element.select(".Topic").text());
            text.append("> ");
            text.append(element.select(".Term").text());
            if (!element.select(".Variable_Level_1").text().isEmpty()) {
                text.append("> ");
                text.append(element.select(".Variable_Level_1").text());
            }
            if (!element.select(".Variable_Level_2").text().isEmpty()) {
                text.append("> ");
                text.append(element.select(".Variable_Level_2").text());
            }
            if (!element.select(".Variable_Level_3").text().isEmpty()) {
                text.append("> ");
                text.append(element.select(".Variable_Level_3").text());
            }
            Element newElement = science_keyword_template.clone();
            newElement.text(text.toString());
            science_keywords.appendChild(newElement);
        }

        templateDoc.select("#keywords").first().text(collectText(sourceDoc, ".Keyword"));
        Element download_table = templateDoc.select("#download_table").first();
        Element download_row = download_table.select(".download_row").first();
        download_row.remove();
        for (Element element : sourceDoc.select(".Related_URL")) {
            Element newRow = download_row.clone();
            if (element.select(".URL_Content_Type").first() != null) {
                newRow.select(".download_type").first().text(element.select(".URL_Content_Type").first().text());
            }

            if (element.select(".Description").first() != null) {
                newRow.select(".download_desc").first().text(element.select(".Description").first().text());
            }

            if (element.select(".URL").first() != null) {
                newRow.select(".download_url").first().text(element.select(".URL").first().text());
                newRow.select(".download_url").first().attr("href", element.select(".URL").first().text());
            }

            download_table.appendChild(newRow);
        }

        templateDoc.select("#dataset-extent").first().text(collectText(sourceDoc, "#boundingboxWKT"));

        Element all_metadata = templateDoc.select("#all_metadata").first();
        Element metadata_row = all_metadata.select(".row").first();
        metadata_row.remove();
        for (Element element : sourceDoc.getElementsByTag("body").first().children()) {
            String elementID = element.attr("id");
            String label = elementID.replace("_", " ");
            Element newRow = metadata_row.clone();

            if (element.childNodeSize() != 0) { //Skip empty nodes

                if (element.childNodeSize() == 1 && element.childNode(0) instanceof TextNode) { //Single node with text
                    newRow.select(".metadata_detail").first().text(((TextNode) element.childNode(0)).text());
                } else {
                    newRow.select(".metadata_detail").first().empty();
                    if (label.endsWith(" List")) {
                        newRow.select(".metadata_detail").first().appendChild(createTable(element.children()));
                    } else {
                        newRow.select(".metadata_detail").first().appendChild(createTable(new Elements(element)));
                    }
                }
                newRow.select(".metadata_label").first().text(label);
                all_metadata.appendChild(newRow);
            }
        }

        return templateDoc.outerHtml();

    }

    public static Element createTable(Elements rows) {
        Element result = null;
        if (rows.first().childNodeSize() <= 1) { //Just grab the text no need for table
            StringBuilder text = new StringBuilder();

            for (Element element : rows) {
                text.append(element.text());
                text.append(",");
            }
            if (text.length() > 0) {
                text.setLength(text.length() - 1);
            }
            result = new Element("span");
            result.text(text.toString());
            //System.out.println(element.text());
        } else {
            result = new Element("table");
            result.attr("class", "table");
            ArrayList<String> tableCols = new ArrayList<String>();
            for (Element row : rows) { //First find all non empty colums
                for (Element column : row.children()) {
                    if (!column.text().isEmpty() && !tableCols.contains(column.attr("class"))) {
                        tableCols.add(column.attr("class"));
                    }
                }
            }
            Element head = new Element("thead");
            Element headRow = new Element("tr");
            for (String col : tableCols) {
                Element td = new Element("td");
                td.text(col.replace("_", " "));
                headRow.appendChild(td);
            }
            head.appendChild(headRow);
            result.appendChild(head);
            Element body = new Element("tbody");
            for (Element row : rows) { //First find all non empty colums
                Element tableRow = new Element("tr");
                for (String col : tableCols) {
                    Element td = new Element("td");
                    if (row.select("." + col).first() != null) {
                        String text = row.select("." + col).first().text();
                        if (!text.isEmpty()) {
                            td.text(text);
                        }
                    }
                    tableRow.appendChild(td);
                }

                body.appendChild(tableRow);
            }
            result.appendChild(body);

        }

        return result;
    }

    public String collectText(Document doc, String parentNodeClass, String textNodeClass) {
        StringBuilder text = new StringBuilder();
        for (Element element : doc.select(parentNodeClass)) {
            text.append(element.select(textNodeClass).text());
            text.append(", ");
        }
        if (text.length() > 0) {
            text.setLength(text.length() - 2);
        }
        return text.toString();
    }

    public String collectText(Document doc, String parentNodeClass) {
        StringBuilder text = new StringBuilder();
        for (Element element : doc.select(parentNodeClass)) {
            text.append(element.text());
            text.append(", ");
        }
        if (text.length() > 0) {
            text.setLength(text.length() - 2);
        }
        return text.toString();
    }

}
