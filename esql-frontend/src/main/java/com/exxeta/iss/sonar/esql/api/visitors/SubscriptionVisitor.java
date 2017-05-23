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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Preconditions;

	public abstract class SubscriptionVisitor implements TreeVisitor {

		  private TreeVisitorContext context;
		  private Collection<Tree.Kind> nodesToVisit;

		  public abstract List<Tree.Kind> nodesToVisit();

		  public void visitNode(Tree tree) {
		    // Default behavior : do nothing.
		  }

		  public void leaveNode(Tree tree) {
		    // Default behavior : do nothing.
		  }

		  public void visitFile(Tree scriptTree) {
		    // default behaviour is to do nothing
		  }

		  public void leaveFile(Tree scriptTree) {
		    // default behaviour is to do nothing
		  }

		  @Override
		  public TreeVisitorContext getContext() {
		    Preconditions.checkState(context != null, "this#scanTree(context) should be called to initialised the context before accessing it");
		    return context;
		  }

		  @Override
		  public final void scanTree(TreeVisitorContext context) {
		    this.context = context;
		    visitFile(context.getTopTree());
		    scanTree(context.getTopTree());
		    leaveFile(context.getTopTree());
		  }

		  public void scanTree(Tree tree) {
		    nodesToVisit = nodesToVisit();
		    visit(tree);
		  }

		  private void visit(Tree tree) {
		    boolean isSubscribed = isSubscribed(tree);
		    if (isSubscribed) {
		      visitNode(tree);
		    }
		    visitChildren(tree);
		    if (isSubscribed) {
		      leaveNode(tree);
		    }
		  }

		  protected boolean isSubscribed(Tree tree) {
		    return nodesToVisit.contains(((EsqlTree) tree).getKind());
		  }

		  private void visitChildren(Tree tree) {
		    EsqlTree javaTree = (EsqlTree) tree;

		    if (!javaTree.isLeaf()) {
		      for (Iterator<Tree> iter = javaTree.childrenIterator(); iter.hasNext(); ) {
		        Tree next = iter.next();

		        if (next != null) {
		          visit(next);
		        }
		      }
		    }
		  }

}
