/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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

package com.exxeta.iss.sonar.esql;

import java.io.File;
import java.util.Collection;

import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SourceCodeBuilderCallback;
import org.sonar.squidbridge.SourceCodeBuilderVisitor;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesOfCodeVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlMetric;
import com.exxeta.iss.sonar.esql.parser.EsqlParser;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

public final class EsqlAstScanner {

	private EsqlAstScanner() {
	}

	/**
	 * Helper method for testing checks without having to deploy them on a Sonar
	 * instance.
	 */
	public static SourceFile scanSingleFile(File file,
			SquidAstVisitor<Grammar>... visitors) {
		if (!file.isFile()) {
			throw new IllegalArgumentException("File '" + file + "' not found.");
		}
		AstScanner<Grammar> scanner = create(new EsqlConfiguration(
				Charsets.UTF_8), visitors);
		scanner.scanFile(file);

		Collection<SourceCode> sources = scanner.getIndex().search(
				new QueryByType(SourceFile.class));
		if (sources.size() != 1) {
			throw new IllegalStateException(
					"Only one SourceFile was expected whereas "
							+ sources.size() + " has been returned.");
		}
		return (SourceFile) sources.iterator().next();
	}

	public static AstScanner<Grammar> create(EsqlConfiguration conf,
			SquidAstVisitor<Grammar>... visitors) {
		final SquidAstVisitorContextImpl<Grammar> context = new SquidAstVisitorContextImpl<Grammar>(
				new SourceProject("Esql Project"));
		final Parser<Grammar> parser = EsqlParser.create(conf);

		AstScanner.Builder<Grammar> builder = AstScanner.<Grammar> builder(
				context).setBaseParser(parser);

		/* Metrics */
		builder.withMetrics(EsqlMetric.values());

		/* Comments */
		builder.setCommentAnalyser(new EsqlCommentAnalyser());

		/* Files */
		builder.setFilesMetric(EsqlMetric.FILES);

		/* Functions */
		builder.withSquidAstVisitor(CounterVisitor.<Grammar> builder()
				.setMetricDef(EsqlMetric.ROUTINES)
				.subscribeTo(EsqlGrammar.routineDeclaration).build());

		builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<Grammar>(
				new SourceCodeBuilderCallback() {
					public SourceCode createSourceCode(
							SourceCode parentSourceCode, AstNode astNode) {
						AstNode identifier = astNode
								.findFirstDirectChild(GenericTokenType.IDENTIFIER);
						final String functionName = identifier == null ? "anonymous"
								: identifier.getTokenValue();
						final String fileKey = parentSourceCode
								.isType(SourceFile.class) ? parentSourceCode
								.getKey() : parentSourceCode.getParent(
								SourceFile.class).getKey();
						SourceFunction function = new SourceFunction(fileKey
								+ ":" + functionName + ":"
								+ astNode.getToken().getLine() + ":"
								+ astNode.getToken().getColumn());
						function.setStartAtLine(astNode.getTokenLine());
						return function;
					}
				}, EsqlGrammar.routineDeclaration));

		/* Metrics */
		builder.withSquidAstVisitor(new LinesVisitor<Grammar>(EsqlMetric.LINES));
		builder.withSquidAstVisitor(new LinesOfCodeVisitor<Grammar>(
				EsqlMetric.LINES_OF_CODE));
		builder.withSquidAstVisitor(CommentsVisitor
				.<Grammar> builder()
				.withCommentMetric(EsqlMetric.COMMENT_LINES)
				// .withBlankCommentMetric(EsqlMetric.COMMENT_BLANK_LINES)
				.withNoSonar(true)
				.withIgnoreHeaderComment(conf.getIgnoreHeaderComments())
				.build());
		builder.withSquidAstVisitor(CounterVisitor.<Grammar> builder()
				.setMetricDef(EsqlMetric.STATEMENTS)
				.subscribeTo(EsqlGrammar.variableStatement,
				// parser.getGrammar().emptyStatement,
				// parser.getGrammar().expressionStatement,
						EsqlGrammar.ifStatement,
						// parser.getGrammar().iterationStatement,
						// parser.getGrammar().continueStatement,
						// parser.getGrammar().breakStatement,
						EsqlGrammar.returnStatement)
				// parser.getGrammar().withStatement,
				// parser.getGrammar().switchStatement,
				// parser.getGrammar().throwStatement,
				// parser.getGrammar().tryStatement,
				// parser.getGrammar().debuggerStatement)
				.build());

		builder.withSquidAstVisitor(new com.exxeta.iss.sonar.esql.metrics.ComplexityVisitor());

		/* External visitors (typically Check ones) */
		for (SquidAstVisitor<Grammar> visitor : visitors) {
			if (visitor instanceof CharsetAwareVisitor) {
				((CharsetAwareVisitor) visitor).setCharset(conf.getCharset());
			}
			builder.withSquidAstVisitor(visitor);
		}

		return builder.build();
	}

}
