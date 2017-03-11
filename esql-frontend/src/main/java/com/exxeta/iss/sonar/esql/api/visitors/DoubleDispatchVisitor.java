package com.exxeta.iss.sonar.esql.api.visitors;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.BrokerSchemaStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.DataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.DecimalDataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.EsqlContentsTree;
import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.IndexTree;
import com.exxeta.iss.sonar.esql.api.tree.IntervalDataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.IntervalQualifierTree;
import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ArrayLiteralTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.InExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IntervalExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BlockTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ControlsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ExternalRoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LabelTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MessageSourceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Preconditions;

public abstract class DoubleDispatchVisitor implements TreeVisitor {
	private TreeVisitorContext context = null;

	@Override
	public TreeVisitorContext getContext() {
		Preconditions.checkState(context != null,
				"this#scanTree(context) should be called to initialised the context before accessing it");
		return context;
	}

	@Override
	public final void scanTree(TreeVisitorContext context) {
		this.context = context;
		scan(context.getTopTree());
	}

	protected void scan(@Nullable Tree tree) {
		if (tree != null) {
			tree.accept(this);
		}
	}

	protected void scanChildren(Tree tree) {
		Iterator<Tree> childrenIterator = ((EsqlTree) tree).childrenIterator();

		Tree child;

		while (childrenIterator.hasNext()) {
			child = childrenIterator.next();
			if (child != null) {
				child.accept(this);
			}
		}
	}

	protected <T extends Tree> void scan(List<T> trees) {
		trees.forEach(this::scan);
	}

	public void visitBlock(BlockTree tree) {
		scanChildren(tree);
	}

	public void visitDeclareStatement(DeclareStatementTree tree) {
		scanChildren(tree);
	}

	public void visitIfStatement(IfStatementTree tree) {
		scanChildren(tree);
	}

	public void visitElseClause(ElseClauseTree tree) {
		scanChildren(tree);
	}

	public void visitElseifClause(ElseifClauseTree tree) {
		scanChildren(tree);
	}

	public void visitIdentifier(IdentifierTree tree) {
		scanChildren(tree);
	}

	public void visitLiteral(LiteralTree tree) {
		scanChildren(tree);
	}

	public void visitToken(SyntaxToken token) {
		for (SyntaxTrivia syntaxTrivia : token.trivias()) {
			syntaxTrivia.accept(this);
		}
	}

	public void visitComment(SyntaxTrivia commentToken) {
		// no sub-tree
	}

	public void visitProgram(ProgramTree tree) {
		scanChildren(tree);

	}

	public void visitBrokerSchemaStatement(BrokerSchemaStatementTree tree) {
		scanChildren(tree);

	}

	public void visitPathClause(PathClauseTree tree) {
		scanChildren(tree);
	}

	public void visitSchemaName(SchemaNameTree tree) {
		scanChildren(tree);
	}

	public void visitTheFunction(TheFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitEsqlContents(EsqlContentsTree tree) {
		scanChildren(tree);
	}

	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		scanChildren(tree);
	}

	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		scanChildren(tree);
	}

	public void visitExternalRoutineBody(ExternalRoutineBodyTree tree) {
		scanChildren(tree);

	}

	public void visitParameterList(ParameterListTree tree) {
		scanChildren(tree);

	}

	public void visitParameter(ParameterTree tree) {
		scanChildren(tree);

	}

	public void visitRoutineBody(RoutineBodyTree tree) {
		scanChildren(tree);

	}

	public void visitReturnType(ReturnTypeTree tree) {
		scanChildren(tree);
	}

	public void visitLanguage(LanguageTree tree) {
		scanChildren(tree);
	}

	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		scanChildren(tree);
	}

	public void visitResultSet(ResultSetTree tree) {
		scanChildren(tree);
	}

	public void visitPropagateStatement(PropagateStatementTree tree) {
		scanChildren(tree);
	}

	public void visitMessageSource(MessageSourceTree tree) {
		scanChildren(tree);

	}

	public void visitControls(ControlsTree tree) {
		scanChildren(tree);
	}

	public void visitBeginEndStatement(BeginEndStatementTree tree) {
		scanChildren(tree);
	}

	public void visitBinaryExpression(BinaryExpressionTree tree) {
		scanChildren(tree);

	}

	public void visitUnaryExpression(UnaryExpressionTree tree) {
		scanChildren(tree);

	}

	public void visitArrayLiteral(ArrayLiteralTree tree) {
		scanChildren(tree);
	}

	public void visitParenthesisedExpression(ParenthesisedExpressionTree tree) {
		scanChildren(tree);
	}

	public void visitIntervalExpression(IntervalExpressionTree tree) {
		scanChildren(tree);

	}

	public void visitIndex(IndexTree tree) {
		scanChildren(tree);
		
	}

	public void visitPathElement(PathElementTree tree) {
		scanChildren(tree);
		
	}

	public void visitNamespace(NamespaceTree tree) {
		scanChildren(tree);
		
	}

	public void visitFieldReference(FieldReferenceTree tree) {
		scanChildren(tree);
		
	}

	public void visitCallExpression(CallExpressionTree tree) {
		scanChildren(tree);
		
	}

	public void visitDecimalDataType(DecimalDataTypeTree tree) {
		scanChildren(tree);
	}

	public void visitIntervalDataType(IntervalDataTypeTree tree) {
		scanChildren(tree);
	}

	public void visitIntervalQualifier(IntervalQualifierTree tree) {
		scanChildren(tree);
	}

	public void visitDataType(DataTypeTree tree) {
		scanChildren(tree);
	}

	public void visitSetStatement(SetStatementTree tree) {
		scanChildren(tree);
	}

	public void visitLabel(LabelTree tree) {
		scanChildren(tree);
	}

	public void visitIterateStatement(IterateStatementTree tree) {
		scanChildren(tree);
		
	}

	public void visitCallStatement(CallStatementTree tree) {
		scanChildren(tree);
	}

	public void visitCaseStatement(CaseStatementTree tree) {
		scanChildren(tree);
	}

	public void visitWhenClause(WhenClauseTree tree) {
		scanChildren(tree);
	}

	public void visitLeaveStatement(LeaveStatementTree tree) {
		scanChildren(tree);
	}

	public void visitLoopStatement(LoopStatementTree tree) {
		scanChildren(tree);
	}

	public void visitRepeatStatement(RepeatStatementTree tree) {
		scanChildren(tree);
	}

	public void visitReturnStatement(ReturnStatementTree tree) {
		scanChildren(tree);
	}

	public void visitInExpression(InExpressionTree tree) {
		scanChildren(tree);
	}

	public void visitThrowStatement(ThrowStatementTree tree) {
		scanChildren(tree);
	}

	public void visitWhileStatement(WhileStatementTree tree) {
	scanChildren(tree);
		
	}


}
