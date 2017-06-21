package uff.ic.geocataloguing;

import uff.ic.geocataloguing.model.Builder;
import uff.ic.geocataloguing.model.InvalidDatasetDescException;
import uff.ic.geocataloguing.model.Parser;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias XMLParser
 */
public class XMLParser extends Parser {

    public XMLParser(Builder builder) {
        super(builder);
    }

    transient private String text = null;

    transient private ArrayList elements = new ArrayList();
    transient private MetadataElement current = null;

    /**
     * Decodifica uma sequ�ncia de bytes em um vetor de chaves XML.
     *
     * @version 1.0
     * @since 1.0
     * @param source sequ�ncia de bytes para ser decodificada
     * @return vetor MetadataElement[] representando as chaves XML contidas na sequ�ncia de bytes.
     * @exception ParserConfigurationException
     * @exception SAXException
     * @exception IOException
     * @exception InvalidDatasetDescException
     */
    public MetadataElement[] parse(InputSource source)
        throws ParserConfigurationException, SAXException, IOException,
        InvalidDatasetDescException {
        MetadataElement[] _elements = null;
        elements = new ArrayList();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader reader = saxParser.getXMLReader();
        reader.setContentHandler(this);
        reader.parse(source);
        elements.trimToSize();
        _elements = (MetadataElement[]) elements
            .toArray(new MetadataElement[0]);
        if (getBuilder() != null)
            getBuilder().build(_elements);
        return _elements;
    }

    // receive notification of the beginning of an
    // element
    public void startElement(String uri, String name, String qName,
        Attributes atts) {
        current = new MetadataElement(uri, name, qName, atts);
        elements.add(current);
        text = new String();

    }

    // receive notification of the end of an element
    public void endElement(String uri, String name, String qName) {
        if (current != null && text != null)
            current.setValue(text.trim());
        current = null;
    }

    // receive notification of character data
    public void characters(char[] ch, int start, int length) {
        if (current != null && text != null) {
            String value = new String(ch, start, length);
            text += value;
        }
    }
}
