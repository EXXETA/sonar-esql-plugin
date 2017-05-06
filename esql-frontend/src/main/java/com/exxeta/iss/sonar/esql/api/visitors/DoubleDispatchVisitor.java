/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.api.visitors;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.AsbitstreamFunctionTree;
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
import com.exxeta.iss.sonar.esql.api.tree.expression.BetweenExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.InExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IntervalExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IsExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CastFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ExtractFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ForFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FromClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.OverlayFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.PassthruFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.PositionFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.RoundFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.RowConstructorFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SubstringFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TrimFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhenClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhereClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.AttachStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BlockTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ControlsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareHandlerStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DetachStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.EvalStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ExternalRoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ForStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.FromClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.InsertStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LabelTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LogStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MessageSourceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MoveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.NameClausesTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResignalStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.UpdateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ValuesClauseTree;
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

	public void visitIsExpression(IsExpressionTree tree) {
		scanChildren(tree);
	}

	public void visitBetweenExpression(BetweenExpressionTree tree) {
		scanChildren(tree);
	}

	public void visitThrowStatement(ThrowStatementTree tree) {
		scanChildren(tree);
	}

	public void visitWhileStatement(WhileStatementTree tree) {
		scanChildren(tree);

	}

	public void visitAttachStatement(AttachStatementTree tree) {
		scanChildren(tree);
	}

	public void visitCreateStatement(CreateStatementTree tree) {
		scanChildren(tree);
	}

	public void visitRepeatClause(RepeatClauseTree tree) {
		scanChildren(tree);
	}

	public void visitValuesClause(ValuesClauseTree tree) {
		scanChildren(tree);
	}

	public void visitFromClause(FromClauseTree tree) {
		scanChildren(tree);
	}

	public void visitParseClause(ParseClauseTree tree) {
		scanChildren(tree);
	}

	public void visitDeleteStatement(DeleteStatementTree tree) {
		scanChildren(tree);
	}

	public void visitDetachStatement(DetachStatementTree tree) {
		scanChildren(tree);
	}

	public void visitResignalStatement(ResignalStatementTree tree) {
		scanChildren(tree);
	}

	public void visitForStatement(ForStatementTree tree) {
		scanChildren(tree);
	}

	public void visitMoveStatement(MoveStatementTree tree) {
		scanChildren(tree);
	}

	public void visitNameClauses(NameClausesTree tree) {
		scanChildren(tree);
	}

	public void visitDeleteFromStatement(DeleteFromStatementTree tree) {
		scanChildren(tree);
	}

	public void visitInsertStatement(InsertStatementTree tree) {
		scanChildren(tree);
	}

	public void visitPassthruStatement(PassthruStatementTree tree) {
		scanChildren(tree);
	}

	public void visitSetColumn(SetColumnTree tree) {
		scanChildren(tree);
	}

	public void visitUpdateStatement(UpdateStatementTree tree) {
		scanChildren(tree);
	}

	public void visitSqlState(SqlStateTree tree) {
		scanChildren(tree);
	}

	public void visitDeclareHandlerStatement(DeclareHandlerStatementTree tree) {
		scanChildren(tree);
	}

	public void visitEvalStatement(EvalStatementTree tree) {
		scanChildren(tree);
	}

	public void visitLogStatement(LogStatementTree tree) {
		scanChildren(tree);
	}

	public void visitExtractFunction(ExtractFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitRoundFunction(RoundFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitOverlayFunction(OverlayFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitPositionFunction(PositionFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitSubstringFunction(SubstringFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitTrimFunction(TrimFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitAsbitstreamFunction(AsbitstreamFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitForFunction(ForFunctionTree tree) {
		scanChildren(tree);
		
	}

	public void visitCastFunction(CastFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitWhenClauseExpression(WhenClauseExpressionTree tree) {
		scanChildren(tree);
	}

	public void visitCaseFunction(CaseFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitWhereClause(WhereClauseTree tree) {
		scanChildren(tree);
	}

	public void visitAliasedFieldReference(AliasedFieldReferenceTree tree) {
		scanChildren(tree);
	}

	public void visitSelectFunction(SelectFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitFromClauseExpression(FromClauseExpressionTree tree) {
		scanChildren(tree);
		
	}

	public void visitSelectClause(SelectClauseTree tree) {
		scanChildren(tree);
	}

	public void visitAliasedExpression(AliasedExpressionTree tree) {
		scanChildren(tree);
	}

	public void visitRowConstructorFunction(RowConstructorFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitPassthruFunction(PassthruFunctionTree tree) {
		scanChildren(tree);
	}

	public void visitStatements(StatementsTree tree) {
		scanChildren(tree);
	}

}
