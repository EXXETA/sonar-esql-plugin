package com.exxeta.iss.sonar.msgflow.parser.node.mq;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQReplyNode;
import com.exxeta.iss.sonar.msgflow.parser.node.NodeParserTest;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.mq.MQReplyNodeImpl;

public class MQReplyParserTest extends NodeParserTest<MQReplyNode> {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {

		final MQReplyNodeParser parser = new MQReplyNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmMQReply.msgnode");

		final MQReplyNodeImpl node = parse(
				"<nodes xmi:type=\"ComIbmMQReply.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_2\" location=\"26,143\" queueName=\"IN\">\n"
						+ "        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Input\"/>\n"
						+ "      </nodes>",
				MQReplyNodeImpl.class);

		assertThat(node).isNotNull();
		assertThat(node.queueName()).isEqualTo("IN");
		assertThat(node.getKind()).isEqualTo(Kind.MQ_INPUT);
		assertThat(node.startLine()).isEqualTo(1);
		assertThat(node.startColumn()).isEqualTo(113);
		assertThat(node.endLine()).isEqualTo(3);
		assertThat(node.endColumn()).isEqualTo(14);

	}
}
