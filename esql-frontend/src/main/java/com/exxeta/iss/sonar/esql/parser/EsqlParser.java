package com.exxeta.iss.sonar.esql.parser;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.sonar.sslr.api.typed.ActionParser;

public final class EsqlParser extends ActionParser<Tree>{
	  public EsqlParser(Charset charset) {
		    super(
		      charset,
		      EsqlLegacyGrammar.createGrammarBuilder(),
		      EsqlGrammar.class,
		      new TreeFactory(),
		      new EsqlNodeBuilder(),
		      EsqlLegacyGrammar.PROGRAM);
		  }

		  @Override
		  public Tree parse(File file) {
		    return setParents(super.parse(file));
		  }

		  @Override
		  public Tree parse(String source) {
		    return setParents(super.parse(source));
		  }

		  private static Tree setParents(Tree tree) {
		    EsqlTree jsTree = (EsqlTree) tree;
		    Iterator<Tree> childrenIterator = jsTree.childrenIterator();
		    while (childrenIterator.hasNext()) {
		      EsqlTree child = (EsqlTree) childrenIterator.next();
		      if (child != null) {
		        child.setParent(tree);
		        if (!child.isLeaf()) {
		          setParents(child);
		        }
		      }
		    }
		    return tree;
		  }


}