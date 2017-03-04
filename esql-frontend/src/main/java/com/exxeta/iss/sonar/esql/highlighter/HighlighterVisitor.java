package com.exxeta.iss.sonar.esql.highlighter;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.source.Highlightable;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.api.Trivia;
import com.sonar.sslr.impl.Lexer;

public class HighlighterVisitor extends SubscriptionVisitor{

	  private final SensorContext sensorContext;
	  private NewHighlighting highlighting;

	  private static final Kind[] METHODS = {
	    Kind.GENERATOR_METHOD,
	    Kind.METHOD,
	    Kind.GET_METHOD,
	    Kind.SET_METHOD
	  };

	  public HighlighterVisitor(SensorContext sensorContext) {
		    this.sensorContext = sensorContext;
		  }

	  @Override
	  public List<Kind> nodesToVisit() {
	    return ImmutableList.<Kind>builder()
	      .add(METHODS)
	      .add(
	        Kind.FIELD,
	        Kind.LET_DECLARATION,
	        Kind.NUMERIC_LITERAL,
	        Kind.TEMPLATE_LITERAL,
	        Kind.STRING_LITERAL,
	        Kind.TOKEN)
	      .build();
	  }

	  @Override
	  public void visitFile(Tree scriptTree) {
	    highlighting = sensorContext.newHighlighting().onFile(fileSystem.inputFile(fileSystem.predicates().is(getContext().getFile())));
	  }

	  @Override
	  public void leaveFile(Tree scriptTree) {
	    highlighting.save();
	  }

	  @Override
	  public void visitNode(Tree tree) {
	    SyntaxToken token = null;
	    TypeOfText code = null;

	    if (tree.is(METHODS)) {
	      token = ((MethodDeclarationTree) tree).staticToken();
	      code = TypeOfText.KEYWORD;

	    } else if (tree.is(Kind.FIELD)) {
	      token = ((FieldDeclarationTree) tree).staticToken();
	      code = TypeOfText.KEYWORD;

	    } else if (tree.is(Kind.LET_DECLARATION)) {
	      token = ((VariableDeclarationTree) tree).token();
	      code = TypeOfText.KEYWORD;

	    } else if (tree.is(Kind.TOKEN)) {
	      highlightToken((InternalSyntaxToken) tree);

	    } else if (tree.is(Kind.STRING_LITERAL)) {
	      token = ((LiteralTreeImpl) tree).token();
	      code = TypeOfText.STRING;

	    } else if (tree.is(Kind.NUMERIC_LITERAL)) {
	      token = ((LiteralTreeImpl) tree).token();
	      code = TypeOfText.CONSTANT;

	    } else if (tree.is(Kind.TEMPLATE_LITERAL)) {
	      highlightTemplateLiteral((TemplateLiteralTree) tree);
	    }

	    if (token != null) {
	      highlight(token, code);
	    }
	  }

	  private void highlightTemplateLiteral(TemplateLiteralTree tree) {
	    highlight(tree.openBacktick(), TypeOfText.STRING);
	    highlight(tree.closeBacktick(), TypeOfText.STRING);

	    for (TemplateCharactersTree templateCharactersTree : tree.strings()) {
	      templateCharactersTree.characters().forEach(token -> highlight(token, TypeOfText.STRING));
	    }
	  }

	  private void highlightToken(InternalSyntaxToken token) {
	    if (isKeyword(token.text())) {
	      highlight(token, TypeOfText.KEYWORD);
	    }
	    highlightComments(token);
	  }

	  private void highlightComments(InternalSyntaxToken token) {
	    TypeOfText type;
	    for (SyntaxTrivia trivia : token.trivias()) {
	      if (trivia.text().startsWith("/**")) {
	        type = TypeOfText.STRUCTURED_COMMENT;
	      } else {
	        type = TypeOfText.COMMENT;
	      }
	      highlight(trivia, type);
	    }
	  }

	  private void highlight(SyntaxToken token, TypeOfText type) {
	    highlighting.highlight(token.line(), token.column(), token.endLine(), token.endColumn(), type);
	  }

	  private static boolean isKeyword(String text) {
		    for (String keyword : EsqlReservedKeyword.keywordValues()) {
			      if (keyword.equals(text)) {
			        return true;
			      }
			    }
		    for (String keyword : EsqlNonReservedKeyword.keywordValues()) {
			      if (keyword.equals(text)) {
			        return true;
			      }
			    }
	    return false;
	  }
}
