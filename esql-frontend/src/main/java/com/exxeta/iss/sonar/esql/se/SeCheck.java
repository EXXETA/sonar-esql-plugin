package com.exxeta.iss.sonar.esql.se;


import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.ScriptTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.Scope;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.Issues;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Extend this class to implement a new check based on symbolic execution.
 */
public class SeCheck implements EsqlCheck {

  private Issues issues = new Issues(this);

  private TreeVisitorContext context;

  /**
   * Override this method to check the truthiness of conditions in current execution (aka function scope).
   * This method is called after end of execution. Note that it's not called if execution was not finished due reaching the execution limit.
   */
  public void checkConditions(Map<Tree, Collection<Truthiness>> conditions) {
    // do nothing by default
  }

  /**
   * Override this method to perform actions before executing <code>element</code>.
   * This method is called before each element until end of execution or reaching the execution limit.
   * @param currentState current state at the program point preceding <code>element</code>
   * @param element syntax tree to be executed next
   */
  public void beforeBlockElement(ProgramState currentState, Tree element) {
    // do nothing by default
  }

  /**
   * Override this method to perform actions after executing <code>element</code>.
   * This method is called after each element until end of execution or reaching the execution limit.
   * @param currentState current state at the program point following <code>element</code>
   * @param element last executed syntax tree
   */
  public void afterBlockElement(ProgramState currentState, Tree element) {
    // do nothing by default
  }

  public void startOfFile(ScriptTree scriptTree) {
    // do nothing by default
  }

  public void endOfFile(ScriptTree scriptTree) {
    // do nothing by default
  }

  /**
   * Override this method to perform actions when the execution is finished.
   * This method is called for each execution, i.e. for each function in the file.
   * Note this method is not called if the execution limit was reached.
   * @param functionScope scope corresponding to the function which was executed
   */
  public void endOfExecution(Scope functionScope) {
    // do nothing by default
  }

  /**
   * Override this method to perform actions before the start of execution.
   * This method is called for each execution, i.e. for each function in the file.
   * Note this method is called even if the execution limit was reached later.
   * @param functionScope scope corresponding to the function which will be executed
   */
  public void startOfExecution(Scope functionScope) {
    // do nothing by default
  }

  /**
   * @deprecated see {@link JavaScriptCheck#addLineIssue(Tree, String)}
   */
  @Override
  @Deprecated
  public LineIssue addLineIssue(Tree tree, String message) {
    return issues.addLineIssue(tree, message);
  }

  @Override
  public PreciseIssue addIssue(Tree tree, String message) {
    return issues.addIssue(tree, message);
  }

  @Override
  public <T extends Issue> T addIssue(T issue) {
    return issues.addIssue(issue);
  }

  @Override
  public List<Issue> scanFile(TreeVisitorContext context) {
    List<Issue> result = ImmutableList.copyOf(issues.getList());
    issues.reset();
    return result;
    // we might add method "getIssue" to this class and use it instead of this one in SeCheckDispatcher
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  public void setContext(TreeVisitorContext context) {
    this.context = context;
  }

  public TreeVisitorContext getContext() {
    return context;
  }
}
