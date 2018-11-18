package com.exxeta.iss.sonar.msgflow.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonar.api.internal.apachecommons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public class ParserUtils {

	public static Document createDocument(final File file)
			throws SAXException, IOException, ParserConfigurationException {
		return createDocument(new FileInputStream(file));
	}

	public static Document createDocument(final InputStream is)
			throws SAXException, IOException, ParserConfigurationException {
		final String startPosAttribName = "startPos";
		final String endPosAttribName = "endPos";
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		final SAXParser parser = factory.newSAXParser();
		final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		final Document doc = docBuilder.newDocument();

		final Stack<Element> elementStack = new Stack<>();
		final StringBuilder textBuffer = new StringBuilder();
		final DefaultHandler handler = new DefaultHandler() {
			private Locator locator;

			// Outputs text accumulated under the current node
			private void addTextIfNeeded() {
				if (textBuffer.length() > 0) {
					final Element el = elementStack.peek();
					final Node textNode = doc.createTextNode(textBuffer.toString());
					el.appendChild(textNode);
					textBuffer.delete(0, textBuffer.length());
				}
			}

			@Override
			public void characters(final char ch[], final int start, final int length) throws SAXException {
				textBuffer.append(ch, start, length);
			}

			@Override
			public void endElement(final String uri, final String localName, final String qName) {
				addTextIfNeeded();
				final Element closedEl = elementStack.pop();
				if (elementStack.isEmpty()) { // Is this the root element?
					doc.appendChild(closedEl);
				} else {
					final Element parentEl = elementStack.peek();
					parentEl.appendChild(closedEl);
				}
				closedEl.setUserData(endPosAttribName,
						String.valueOf(locator.getLineNumber()) + ":" + String.valueOf(locator.getColumnNumber()),
						null);
			}

			@Override
			public void setDocumentLocator(final Locator locator) {
				this.locator = locator; // Save the locator, so that it can be used later for line tracking when
										// traversing nodes.
			}

			@Override
			public void startElement(final String uri, final String localName, final String qName,
					final Attributes attributes) throws SAXException {
				addTextIfNeeded();
				final Element el = doc.createElement(qName);
				for (int i = 0; i < attributes.getLength(); i++) {
					el.setAttribute(attributes.getQName(i), attributes.getValue(i));
				}
				el.setUserData(startPosAttribName,
						String.valueOf(locator.getLineNumber()) + ":" + String.valueOf(locator.getColumnNumber()),
						null);
				elementStack.push(el);
			}
			
		};
		parser.parse(is, handler);
		return doc;
		// return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}

	public static Document createDocument(final String source)
			throws SAXException, IOException, ParserConfigurationException {

		return createDocument(IOUtils.toInputStream(source));
	}

	public static String getXPathString(final Element nodeElement, final String xpathExpression)
			throws XPathExpressionException {
		final XPathExpression expression = XPathFactory.newInstance().newXPath().compile(xpathExpression);

		return (String) expression.evaluate(nodeElement, XPathConstants.STRING);
	}

	public static AbstractMessageFlowNode parseNode(final Element element) {
		final String type = element.getAttribute("xmi:type").split(":")[0];
		return ParserLists.getNodeParser(type).parseMessageFlowNode(element);
	}

	private ParserUtils() {

	}

}
