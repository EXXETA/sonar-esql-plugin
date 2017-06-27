package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.google.common.collect.ImmutableList;

@Rule(key="XmlnscDomain")
public class XmlnscDomainCheck extends DoubleDispatchVisitorCheck{

	ImmutableList<String> rootElements = ImmutableList.of("Root", "IntputRoot", "OutputRoot");
	ImmutableList<String> wrongDomain = ImmutableList.of("XML", "XMLNS");
	
	
	@Override
	public void visitFieldReference(FieldReferenceTree tree) {
		super.visitFieldReference(tree);
		if (rootElements.contains(tree.pathElement().name().name().text())
				&& !tree.pathElements().isEmpty()){
			String domain = tree.pathElements().get(0).name().name().text();
			checkDomain(domain, tree.pathElements().get(0));
		}
	}
	
	@Override
	public void visitCreateStatement(CreateStatementTree tree) {
		super.visitCreateStatement(tree);
		if (tree.domainExpression()!=null && tree.domainExpression().is(Kind.STRING_LITERAL)){
			String domain = ((LiteralTree)tree.domainExpression()).value();
			domain = domain.substring(1, domain.length()-1);
			checkDomain(domain, tree.domainExpression());
		}
	}

	private void checkDomain(String domain, Tree tree) {
		if (wrongDomain.contains(domain)){
			addIssue(tree, "Use the XMLNSC domain instead of "+domain+".");
		}
	}
	
	
}
