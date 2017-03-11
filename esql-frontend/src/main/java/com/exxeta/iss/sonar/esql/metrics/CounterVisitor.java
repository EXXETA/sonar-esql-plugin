package com.exxeta.iss.sonar.esql.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;

public class CounterVisitor extends SubscriptionVisitor {

  private int statementCounter = 0;
  private int moduleCounter = 0;
  private int functionCounter = 0;
  private int procedureCounter = 0;

  private static final Kind[] STATEMENT_NODES = {
		Kind.BEGIN_END_STATEMENT,
		Kind.CALL_STATEMENT,
		Kind.CASE_STATEMENT,
		Kind.DECLARE_STATEMENT,
		Kind.IF_STATEMENT,
		Kind.ITERATE_STATEMENT,
		Kind.LEAVE_STATEMENT,
		Kind.LOOP_STATEMENT,
		Kind.PROPAGATE_STATEMENT,
		Kind.REPEAT_STATEMENT,
		Kind.RETURN_STATEMENT,
		Kind.SET_STATEMENT
  };

  @Override
  public List<Kind> nodesToVisit() {
    List<Kind> result = new ArrayList<>();
    result.addAll(Arrays.asList(STATEMENT_NODES));
    result.add(Kind.CREATE_FUNCTION_STATEMENT);
    result.add(Kind.CREATE_PROCEDURE_STATEMENT);
    result.add(Kind.CREATE_MODULE_STATEMENT);
    return result;
  }

  public CounterVisitor(Tree tree) {
    scanTree(tree);
  }

  public int getStatementsNumber() {
    return statementCounter;
  }
  
  


	  public int getModulesNumber() {
		return moduleCounter;
	}
	
	
	public int getFunctionsNumber() {
		return functionCounter;
	}
	
	
	public int getProceduresNumber() {
		return procedureCounter;
	}


@Override
  public void visitNode(Tree tree) {
	if (tree.is(STATEMENT_NODES)) {
      statementCounter++;
    } else if (tree.is(Kind.CREATE_FUNCTION_STATEMENT)){
    	functionCounter++;
    } else if (tree.is(Kind.CREATE_PROCEDURE_STATEMENT)){
    	procedureCounter++;
    } else if (tree.is(Kind.CREATE_MODULE_STATEMENT)){
    	moduleCounter++;
    }
  }
}
