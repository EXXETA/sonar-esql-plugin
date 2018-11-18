package com.exxeta.iss.sonar.msgflow.parser;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.createDocument;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowCommentNote;
import com.exxeta.iss.sonar.msgflow.metrics.MsgflowModelTest;
import com.exxeta.iss.sonar.msgflow.parser.node.mq.MQInputNodeParser;

public class StickeyNoteParserTest extends MsgflowModelTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {

		final MQInputNodeParser parser = new MQInputNodeParser();

		assertThat(parser.getNodeType()).isEqualTo("ComIbmMQInput.msgnode");

		final Document document = createDocument(
				"<stickyNote location=\"113,231\" association=\"FCMComposite_1_2 FCMComposite_1_3 FCMComposite_1_1\">\n"
						+ "        <body xmi:type=\"utility:ConstantString\" string=\"This Message Flow is a middleware service for a utility bill payment functionality.\"/>\n"
						+ "      </stickyNote>");

		MessageFlowCommentNote stickeyNote = new StickeyNoteParser().parse(document.getDocumentElement());
		assertThat(stickeyNote).isNotNull();
		assertThat(stickeyNote.locationX()).isEqualTo(113);
		assertThat(stickeyNote.locationY()).isEqualTo(231);
		assertThat(stickeyNote.associations()).hasSize(3);
		assertThat(stickeyNote.associations()).contains("FCMComposite_1_2", "FCMComposite_1_3", "FCMComposite_1_1");
		assertThat(stickeyNote.comment())
				.isEqualTo("This Message Flow is a middleware service for a utility bill payment functionality.");

	}
}
