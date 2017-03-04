package com.exxeta.iss.sonar.esql.api.tree;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IntervalExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
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
import com.exxeta.iss.sonar.esql.api.tree.statement.MessageSourceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.Kinds;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

public interface Tree {
	boolean is(Kind... kind);

	void accept(DoubleDispatchVisitor visitor);

	public enum Kind implements GrammarRuleKey, Kinds {
	    TOKEN(SyntaxToken.class),
		TRIVIA(SyntaxTrivia.class), 
		ELSE_CLAUSE(ElseClauseTree.class), 
		IF_STATEMENT(IfStatementTree.class), 
		ELSEIF_CLAUSE(ElseifClauseTree.class), 
		DECLARE_STATEMENT(DeclareStatementTree.class), 
		PROGRAM(ProgramTree.class), 
		SCHEMA_NAME(SchemaNameTree.class), 
		BROKER_SCHEMA_STATEMENT(BrokerSchemaStatementTree.class), 
		PATH_CLAUSE(PathClauseTree.class), 
		THE_FUNCTION(TheFunctionTree.class), 
		ESQL_CONTENTS(EsqlContentsTree.class), 
		CREATE_FUNCTION_STATEMENT(CreateFunctionStatementTree.class), 
		ROUTINE_BODY(RoutineBodyTree.class), 
		EXTERNAL_ROUTINE_BODY(ExternalRoutineBodyTree.class), 
		PARAMETER(ParameterTree.class), 
		RETURN_TYPE(ReturnTypeTree.class), 
		LANGUAGE(LanguageTree.class), 
		CREATE_MODULE_STATEMENT(CreateModuleStatementTree.class), 
		ROUTINE_DECLARATION(RoutineDeclarationTree.class), 
		RESULT_SET(ResultSetTree.class), 
		CREATE_PROCEDURE_STATEMENT(CreateProcedureStatementTree.class), 
		PROPAGATE_STATEMENT(PropagateStatementTree.class), 
		MESSAGE_SOURCE(MessageSourceTree.class), 
		CONTROLS(ControlsTree.class), 
		BEGIN_END_STATEMENT(BeginEndStatementTree.class), 
		CONDITIONAL_OR(BinaryExpressionTree.class), 
		CONDITIONAL_AND(BinaryExpressionTree.class), 
		EQUAL_TO(BinaryExpressionTree.class), 
		LESS_THAN(BinaryExpressionTree.class), 
		GREATER_THAN(BinaryExpressionTree.class), 
		LESS_THAN_OR_EQUAL_TO(BinaryExpressionTree.class), 
		GREATER_THAN_OR_EQUAL_TO(BinaryExpressionTree.class), 
		PLUS(BinaryExpressionTree.class), 
		MINUS(BinaryExpressionTree.class), 
		MULTIPLY(BinaryExpressionTree.class), 
		NOT_EQUAL_TO(BinaryExpressionTree.class), 
		DIVIDE(BinaryExpressionTree.class), 
		CONCAT(BinaryExpressionTree.class), 
		ARGUMENTS(ParameterListTree.class), 
		PARAMETER_LIST(ParameterListTree.class), 
		LIST_LITERAL(LiteralTree.class), 
		TIME_LITERAL(LiteralTree.class), 
		DATE_LITERAL(LiteralTree.class), 
		ARRAY_LITERAL(LiteralTree.class), 
		INTERVAL_LITERAL(LiteralTree.class), 
		NULL_LITERAL(LiteralTree.class), 
		BOOLEAN_LITERAL(LiteralTree.class), 
		HEX_LITERAL(LiteralTree.class), 
		STRING_LITERAL(LiteralTree.class), 
		NUMERIC_LITERAL(LiteralTree.class), 
		PARENTHESISED_EXPRESSION(ParenthesisedExpressionTree.class), 
		INTERVAL_EXPRESSION(IntervalExpressionTree.class), 
		FIELD_REFERENCE(FieldReferenceTree.class), 
		NAMESPACE(NamespaceTree.class), 
		PATH_ELEMENT(PathElementTree.class), 
		INDEX(IndexTree.class), 
		CALL_EXPRESSION(CallExpressionTree.class), 
		INTERVAL_QUALIFIER(IntervalQualifierTree.class), 
		INTERVAL_DATA_TYPE(IntervalDataTypeTree.class), 
		DECIMAL_DATA_TYPE(DecimalDataTypeTree.class), 
		Data_TYPE(DataTypeTree.class), 
		SET_STATEMENT(SetStatementTree.class),
		ITERATE_STATEMENT(IterateStatementTree.class), 
		LABEL(LabelTree.class), 
		CALL_STATEMENT(CallStatementTree.class), 
		CASE_STATEMENT(CaseStatementTree.class), 
		WHEN_CLAUSE(WhenClauseTree.class), 
		LEAVE_STATEMENT(LeaveStatementTree.class);

		final Class<? extends Tree> associatedInterface;

		Kind(Class<? extends Tree> associatedInterface) {
			this.associatedInterface = associatedInterface;
		}

		public Class<? extends Tree> getAssociatedInterface() {
			return associatedInterface;
		}
		
	    @Override
	    public boolean contains(Kinds other) {
	      return this.equals(other);
	    }

	}
}
