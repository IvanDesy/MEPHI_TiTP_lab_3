package importers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import Objects.Reactor;
import masters.ReactorsHolder;
/**
 *
 * @author vanya
 */
public class XmlImporter extends Importer {
    @Override
    public void importFile(File file, ReactorsHolder reactorMap) throws IOException {
        if (isTrueType(file)) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                try (var fileInputStream = new FileInputStream(file)) {
                    Document doc = builder.parse(fileInputStream);
                    doc.getDocumentElement().normalize();

                    NodeList nodeList = doc.getDocumentElement().getChildNodes();

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            Element element = (Element) nodeList.item(i);
                            Reactor reactor = parseReactor(element);
                            reactorMap.addReactor(element.getNodeName(), reactor);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (next != null) {
            next.importFile(file, reactorMap);
        } else {
            System.out.println("Unsupported file format");
        }
    }

    protected boolean isTrueType(File file){
        return file.getName().endsWith(".xml");
    }

    private Reactor parseReactor(Element element) {
        String type = element.getNodeName();
        String reactorClass = getElementTextContent(element, "class");
        Double burnup = getDoubleElementTextContent(element, "burnup");
        Double electricalCapacity = getDoubleElementTextContent(element, "electrical_capacity");
        Double enrichment = getDoubleElementTextContent(element, "enrichment");
        Double firstLoad = getDoubleElementTextContent(element, "first_load");
        Double kpd = getDoubleElementTextContent(element, "kpd");
        Integer lifeTime = getIntElementTextContent(element, "life_time");
        Double thermalCapacity = getDoubleElementTextContent(element, "termal_capacity");

        return new Reactor(type, reactorClass, burnup, electricalCapacity, enrichment, firstLoad, kpd, lifeTime, thermalCapacity, "XML");
    }

    private String getElementTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : "";
    }

    private Double getDoubleElementTextContent(Element element, String tagName) {
        String textContent = getElementTextContent(element, tagName);
        return !textContent.isEmpty() ? Double.parseDouble(textContent) : null;
    }

    private Integer getIntElementTextContent(Element element, String tagName) {
        String textContent = getElementTextContent(element, tagName);
        return !textContent.isEmpty() ? Integer.parseInt(textContent) : null;
    }
}