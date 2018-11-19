package com.exxeta.iss.sonar.msgflow.parser;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.createDocument;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.metrics.MsgflowModelTest;

public class ConnectionParserTest extends MsgflowModelTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {

		final ConnectionParser parser = new ConnectionParser();

		final Document document = createDocument(
				" <connections xmi:type=\"eflow:FCMConnection\" xmi:id=\"FCMConnection_1\" targetNode=\"FCMComposite_1_2\" sourceNode=\"FCMComposite_1_1\" sourceTerminalName=\"OutTerminal.failure\" targetTerminalName=\"InTerminal.in\"/>");

		MessageFlowConnection connection = parser.parse(document.getDocumentElement());
		assertThat(connection).isNotNull();
		assertThat(connection. srcNode()).isEqualTo("FCMComposite_1_1");

		assertThat(connection. srcTerminal()).isEqualTo("OutTerminal.failure");

		assertThat(connection. targetNode()).isEqualTo("FCMComposite_1_2");

		assertThat(connection. targetTerminal()).isEqualTo("InTerminal.in");
	}
}
