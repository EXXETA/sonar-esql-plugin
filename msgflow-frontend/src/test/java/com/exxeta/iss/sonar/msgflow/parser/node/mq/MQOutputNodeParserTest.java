package com.exxeta.iss.sonar.msgflow.parser.node.mq;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQInputNode;
import com.exxeta.iss.sonar.msgflow.parser.node.NodeParserTest;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.mq.MQOutputNodeImpl;

public class MQOutputNodeParserTest extends NodeParserTest<MQInputNode> {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {

		final MQOutputNodeParser parser = new MQOutputNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmMQOutput.msgnode");

		final MQOutputNodeImpl node = parse(
				"<nodes xmi:type=\"ComIbmMQOutput.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"247,185\" validateMaster=\"contentAndValue\">\n" + 
				"        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Output\"/>\n" + 
				"      </nodes>",
				MQOutputNodeImpl.class);

		assertThat(node).isNotNull();
		assertThat(node.queueName()).isEqualTo("");
		assertThat(node.getKind()).isEqualTo(Kind.MQ_OUTPUT);
		assertThat(node.startLine()).isEqualTo(1);
		assertThat(node.startColumn()).isEqualTo(133);
		assertThat(node.endLine()).isEqualTo(3);
		assertThat(node.endColumn()).isEqualTo(14);

	}
}
