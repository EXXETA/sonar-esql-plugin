package com.exxeta.iss.sonar.msgflow.parser.node.mq;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQInputNode;
import com.exxeta.iss.sonar.msgflow.parser.node.NodeParserTest;

public class MQInputParserTest extends NodeParserTest<MQInputNode> {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {

		final MQInputNodeParser parser = new MQInputNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmMQInput.msgnode");

		final MQInputNode node = parse(
				"<nodes xmi:type=\"ComIbmMQInput.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_2\" location=\"26,143\" queueName=\"IN\">\n"
						+ "        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Input\"/>\n"
						+ "      </nodes>",
				MQInputNode.class);

		assertThat(node).isNotNull();
		assertThat(node.queueName()).isEqualTo("IN");
	}
}
