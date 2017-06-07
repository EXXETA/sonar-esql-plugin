package com.exxeta.iss.sonar.esql.check;

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.google.common.collect.Lists;

@Rule(key = "UnknownMessageDomain")
public class UnknownMessageDomainCheck extends DoubleDispatchVisitorCheck {

	private static List<String> rootElements = Collections.unmodifiableList(Lists.newArrayList("InputRoot", "OutputRoot", "Root"));
	private static List<String> domains = Collections.unmodifiableList(Lists.newArrayList("DFDL","MRM", "XML", "XMLNS", "XMLNSC", "JMS", "IDOC", "MIME", "BLOB", "JSON"));
	
	
	@Override
	public void visitFieldReference(FieldReferenceTree tree) {
		
		String pathElement1 = tree.pathElement().name().name().text();
		if (rootElements.contains(pathElement1) && tree.pathElements().size()>0){
			String pathElement2 = tree.pathElements().get(0).name().name().text();
			if (!domains.contains(pathElement2)){
				addIssue(tree, "Unknown domain \""+pathElement2+"\".");
			}
		}
		super.visitFieldReference(tree);
	}

}
