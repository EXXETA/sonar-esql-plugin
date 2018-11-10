package com.exxeta.iss.sonar.msgflow.parser;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.createDocument;
import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.parseNode;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class AggregateControlParserTest {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {
		
		AggregateControlParser parser = new AggregateControlParser();
		
		assertThat(parser.getNodeType()).isEqualTo("ComIbmAggregateControl.msgnode");

		MessageFlowNode node = parse("<nodes xmi:type=\"ComIbmAggregateControl.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"230,60\" aggregateName=\"Sample\" timeoutInterval=\"0\" timeoutLocation=\"$LocalEnvironment/Aggregation/Timeout\">\n" + 
				"        <translation xmi:type=\"utility:ConstantString\" string=\"Aggregate Control\"/>\n" + 
				"      </nodes>", AggregateControlNode.class);
		
		assertThat(node).isNotNull();
		assertThat(node).isInstanceOf(AggregateControlNode.class);
		assertThat(((AggregateControlNode)node).getAggregateName()).isEqualTo("Sample");
	}

	private static <T extends MessageFlowNode>T parse(String string,Class<T> c) throws SAXException, IOException, ParserConfigurationException {
		Document dom = createDocument(string);
		
		MessageFlowNode node = parseNode(dom.getDocumentElement());
		
		assertThat(node).isInstanceOf(c);
		return (T)node;
	}
}
