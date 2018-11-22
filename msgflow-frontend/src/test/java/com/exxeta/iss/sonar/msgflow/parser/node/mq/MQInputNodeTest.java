package com.exxeta.iss.sonar.msgflow.parser.node.mq;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQInputNode;
import com.exxeta.iss.sonar.msgflow.parser.node.NodeParserTest;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.mq.MQInputNodeImpl;

public class MQInputNodeTest extends NodeParserTest<MQInputNode> {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {

		final MQInputNodeParser parser = new MQInputNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmMQInput.msgnode");

		final MQInputNodeImpl node = parse(
				"<nodes xmi:type=\"ComIbmMQInput.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_2\" location=\"26,143\" queueName=\"IN\">\n"
						+ "        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Input\"/>\n"
						+ "      </nodes>",
				MQInputNodeImpl.class);

		assertThat(node).isNotNull();
		assertThat(node.queueName()).isEqualTo("IN");
		assertThat(node.getKind()).isEqualTo(Kind.MQ_INPUT);
		assertThat(node.startLine()).isEqualTo(1);
		assertThat(node.startColumn()).isEqualTo(113);
		assertThat(node.endLine()).isEqualTo(3);
		assertThat(node.endColumn()).isEqualTo(14);
		assertThat(node.id()).isEqualTo("FCMComposite_1_2");
		assertThat(node.locationX()).isEqualTo(26);
		assertThat(node.locationY()).isEqualTo(143);
		
		
		assertThat(node.properties()).isEmpty();
		node.addProperty("a", "b");
		assertThat(node.properties()).isNotEmpty();
		

	}
}
