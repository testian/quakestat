/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.jdom.Element;
import java.util.LinkedList;
/**
 *
 * @author testi
 */
public class ElementEmitter implements ContentHandler {
    private int level;
    private LinkedList<Element> elementStack;
    private ElementListener l;
    public ElementEmitter(int level, ElementListener l) {
    this.level = level;
    this.l = l;
    elementStack = new LinkedList<Element>();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementStack.isEmpty()) return;
        elementStack.getLast().addContent(String.copyValueOf(ch, start, length));
    }

    public void endDocument() throws SAXException {
        
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        Element element = elementStack.removeLast();
        if (elementStack.size() == level) l.onElement(element);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        
    }

    public void processingInstruction(String target, String data) throws SAXException {
        
    }

    public void setDocumentLocator(Locator locator) {
        
    }

    public void skippedEntity(String name) throws SAXException {
        
    }

    public void startDocument() throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        Element e = new Element(localName);
        if (!elementStack.isEmpty()) elementStack.getLast().addContent(e);
        elementStack.add(e);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

}
