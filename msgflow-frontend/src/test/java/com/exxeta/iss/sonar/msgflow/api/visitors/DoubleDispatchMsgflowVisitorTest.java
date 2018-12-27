package com.exxeta.iss.sonar.msgflow.api.visitors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowCommentNote;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.api.tree.Messageflow;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParser;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParserBuilder;
import com.sonar.sslr.api.RecognitionException;

public class DoubleDispatchMsgflowVisitorTest {

	private static final String NODE_MSGFLOW_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<ecore:EPackage xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:ComIbmAggregateControl.msgnode=\"ComIbmAggregateControl.msgnode\" xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" xmlns:eflow=\"http://www.ibm.com/wbi/2005/eflow\" xmlns:utility=\"http://www.ibm.com/wbi/2005/eflow_utility\" nsURI=\"AggregateControl.msgflow\" nsPrefix=\"AggregateControl.msgflow\">\n" + 
			"  <eClassifiers xmi:type=\"eflow:FCMComposite\" name=\"FCMComposite_1\">\n" + 
			"    <eSuperTypes href=\"http://www.ibm.com/wbi/2005/eflow#//FCMBlock\"/>\n" + 
			"    <translation xmi:type=\"utility:TranslatableString\" key=\"AggregateControl\" bundleName=\"AggregateControl\" pluginId=\"FilterCheck\"/>\n" + 
			"    <colorGraphic16 xmi:type=\"utility:GIFFileGraphic\" resourceName=\"platform:/plugin/FilterCheck/icons/full/obj16/AggregateControl.gif\"/>\n" + 
			"    <colorGraphic32 xmi:type=\"utility:GIFFileGraphic\" resourceName=\"platform:/plugin/FilterCheck/icons/full/obj30/AggregateControl.gif\"/>\n" + 
			"    <composition>\n" + 
			"      <nodes xmi:type=\"ComIbmAggregateControl.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"230,60\" aggregateName=\"Sample\" timeoutInterval=\"0\" timeoutLocation=\"$LocalEnvironment/Aggregation/Timeout\">\n" + 
			"        <translation xmi:type=\"utility:ConstantString\" string=\"Aggregate Control\"/>\n" + 
			"      </nodes>\n" + 
			"      <nodes xmi:type=\"ComIbmMQOutput.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"247,185\" validateMaster=\"contentAndValue\">\n" + 
			"        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Output\"/>\n" + 
			"      </nodes>\n" + 
			"      <nodes xmi:type=\"ComIbmAggregateControl.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_2\" location=\"230,60\" aggregateName=\"Sample\" timeoutInterval=\"0\" timeoutLocation=\"$LocalEnvironment/Aggregation/Timeout\">\n" + 
			"        <translation xmi:type=\"utility:ConstantString\" string=\"Aggregate Control\"/>\n" + 
			"      </nodes>\n" + 
			"       <nodes xmi:type=\"ComIbmMQInput.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"130,150\" parserXmlnscBuildTreeUsingXMLSchema=\"true\" parserXmlnscMixedContentRetainMode=\"all\" parserXmlnscCommentsRetainMode=\"all\" validateMaster=\"contentAndValue\" messageDomainProperty=\"XMLNSC\" messageSetProperty=\"TestHttp\">\n" + 
			"        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Input\"/>\n" + 
			"      </nodes>\n" + 
			"       <nodes xmi:type=\"ComIbmMQReply.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_2\" location=\"130,150\" parserXmlnscBuildTreeUsingXMLSchema=\"true\" parserXmlnscMixedContentRetainMode=\"all\" parserXmlnscCommentsRetainMode=\"all\" validateMaster=\"contentAndValue\" messageDomainProperty=\"XMLNSC\" messageSetProperty=\"TestHttp\">\n" + 
			"        <translation xmi:type=\"utility:ConstantString\" string=\"MQ Input\"/>\n" + 
			"      </nodes>\n" + 
			"      <nodes xmi:type=\"ComIbmCompute.msgnode:FCMComposite_1\" xmi:id=\"FCMComposite_1_1\" location=\"250,190\" dataSource=\"ORACLEDB\" computeExpression=\"esql://routine/#Compute_Compute.Main\">\n" + 
			"        <translation xmi:type=\"utility:ConstantString\" string=\"Compute\"/>\n" + 
			"      </nodes>      \n" + 
			"      <connections xmi:type=\"eflow:FCMConnection\" xmi:id=\"FCMConnection_1\" targetNode=\"FCMComposite_1_2\" sourceNode=\"FCMComposite_1_1\" sourceTerminalName=\"OutTerminal.failure\" targetTerminalName=\"InTerminal.in\"/>\n" + 
			"      \n" + 
			"    </composition>\n" + 
			"    <propertyOrganizer/>\n" + 
			"    <stickyBoard>    <stickyBoard>\n" + 
			"      <stickyNote location=\"113,231\" association=\"FCMComposite_1_2 FCMComposite_1_3 FCMComposite_1_1\">\n" + 
			"        <body xmi:type=\"utility:ConstantString\" string=\"This Message Flow is a middleware service for a utility bill payment functionality.\"/>\n" + 
			"      </stickyNote>\n" + 
			"    </stickyBoard></stickyBoard>\n" + 
			"  </eClassifiers>\n" + 
			"</ecore:EPackage>";
	
	private static final String INCOMPLETED_XML = NODE_MSGFLOW_XML.substring(0, NODE_MSGFLOW_XML.length()-1);
	

	@Test
	public void test_visit_token() throws Exception {
		assertNumberOfVisitedNodes(NODE_MSGFLOW_XML, 6);
	}
	@Test
	public void test_visit_comment() throws Exception {
		assertNumberOfVisitedComments(NODE_MSGFLOW_XML, 1);
	}
	@Test
	public void test_visit_connection() throws Exception {
		assertNumberOfVisitedConnections(NODE_MSGFLOW_XML, 1);
	}
	

	private class TestVisitor extends DoubleDispatchMsgflowVisitor {
		int nodeCounter;
		int commentCounter;
		int connectionCounter;

		@Override
		public void visitMsgflow(Messageflow tree) {
			nodeCounter = 0;
			commentCounter = 0;
			super.visitMsgflow(tree);
		}

		@Override
		public void visitNode(MessageFlowNode node) {
			nodeCounter++;
			super.visitNode(node);
		}

		@Override
		public void visitCommentNote(final MessageFlowCommentNote commentToken) {
			commentCounter++;
			super.visitCommentNote(commentToken);
		}

		@Override
		public void visitConnection(MessageFlowConnection tree) {
			connectionCounter++;
			super.visitConnection(tree);
		}

	}

	private void assertNumberOfVisitedNodes(String code, int expectedNodeNumber) {
		assertThat(getTestVisitor(code).nodeCounter).isEqualTo(expectedNodeNumber);
	}

	private void assertNumberOfVisitedComments(String code, int expectedCommentNumber) {
		assertThat(getTestVisitor(code).commentCounter).isEqualTo(expectedCommentNumber);
	}

	private void assertNumberOfVisitedConnections(String code, int expectedConnectionNumber) {
		assertThat(getTestVisitor(code).connectionCounter).isEqualTo(expectedConnectionNumber);
	}

	private TestVisitor getTestVisitor(String code) {
		MsgflowParser p = MsgflowParserBuilder.createParser();
		TestVisitor testVisitor = new TestVisitor();
		testVisitor.visitMsgflow(p.parse(code));
		return testVisitor;
	}

}
