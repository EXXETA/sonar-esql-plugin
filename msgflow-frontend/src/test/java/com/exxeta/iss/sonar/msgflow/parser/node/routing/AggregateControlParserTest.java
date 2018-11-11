package com.exxeta.iss.sonar.msgflow.parser.node.routing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.node.routing.AggregateControlNode;
import com.exxeta.iss.sonar.msgflow.parser.node.NodeParserTest;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.routing.AggregateControlNodeImpl;

public class AggregateControlParserTest extends NodeParserTest<AggregateControlNode> {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {

		final AggregateControlNodeParser parser = new AggregateControlNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmAggregateControl.msgnode");

		final AggregateControlNodeImpl node = parse(
				"<nodes xmi:type=\"ComIbmAggregateControl.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"230,60\" aggregateName=\"Sample\" timeoutInterval=\"0\" timeoutLocation=\"$LocalEnvironment/Aggregation/Timeout\">\n"
						+ "        <translation xmi:type=\"utility:ConstantString\" string=\"Aggregate Control\"/>\n"
						+ "      </nodes>",
				AggregateControlNodeImpl.class);

		assertThat(node).isNotNull();
		assertThat(node.aggregateName()).isEqualTo("Sample");
		assertThat(node.timeoutInterval()).isEqualTo("0");
		assertThat(node.timeoutLocation()).isEqualTo("$LocalEnvironment/Aggregation/Timeout");
		assertThat(node.id()).isEqualTo("FCMComposite_1_1");
		assertThat(node.locationX()).isEqualTo(230);
		assertThat(node.locationY()).isEqualTo(60);
		assertThat(node.name()).isEqualTo("Aggregate Control");
		assertThat(node.sourceTerminals()).isEmpty();
		assertThat(node.targetTerminals()).isEmpty();
	}
}
