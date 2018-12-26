package com.exxeta.iss.sonar.msgflow.parser.node.transformation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.msgflow.api.tree.node.transformation.ComputeNode;
import com.exxeta.iss.sonar.msgflow.parser.node.NodeParserTest;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.transformation.ComputeNodeImpl;

public class ComputeNodeParserTest extends NodeParserTest<ComputeNode> {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException {

		final ComputeNodeParser parser = new ComputeNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmCompute.msgnode");

		final ComputeNodeImpl node = parse(
				"<nodes xmi:type=\"ComIbmCompute.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"250,190\" dataSource=\"ORACLEDB\" computeExpression=\"esql://routine/#Compute_Compute.Main\">\n" + 
				"        <translation xmi:type=\"utility:ConstantString\" string=\"Compute\"/>\n" + 
				"      </nodes>",
				ComputeNodeImpl.class);

		assertThat(node).isNotNull();
		assertThat(node.id()).isEqualTo("FCMComposite_1_1");
		assertThat(node.locationX()).isEqualTo(250);
		assertThat(node.locationY()).isEqualTo(190);
		assertThat(node.dataSource()).isEqualTo("ORACLEDB");
		
		
		assertThat(node.getKind()).isEqualTo(Kind.COMPUTE);
		assertThat(node.startLine()).isEqualTo(1);
		assertThat(node.startColumn()).isEqualTo(178);
		assertThat(node.endLine()).isEqualTo(3);
		assertThat(node.endColumn()).isEqualTo(14);

	}
}
