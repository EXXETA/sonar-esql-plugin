package com.exxeta.iss.sonar.msgflow.metrics;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.createDocument;
import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.parseNode;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exxeta.iss.sonar.msgflow.parser.MsgflowParserBuilder;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;

public abstract class MsgflowModelTest {

	protected MsgflowTree parse(File file) {

		return MsgflowParserBuilder.createParser().parse(file);
	}

	protected <T> T parse(final String string, final Class<T> c)
			throws SAXException, IOException, ParserConfigurationException {
		final Document dom = createDocument(string);

		final AbstractMessageFlowNode node = parseNode(dom.getDocumentElement());

		assertThat(node).isInstanceOf(c);
		return (T) node;
	}
}
