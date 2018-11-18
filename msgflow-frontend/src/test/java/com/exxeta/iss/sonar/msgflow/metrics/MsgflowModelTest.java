package com.exxeta.iss.sonar.msgflow.metrics;

import java.io.File;

import com.exxeta.iss.sonar.msgflow.parser.MsgflowParserBuilder;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;

public abstract class MsgflowModelTest {

	  protected MsgflowTree parse(File file) {
		  
		  return MsgflowParserBuilder.createParser().parse(file);
	}
}
