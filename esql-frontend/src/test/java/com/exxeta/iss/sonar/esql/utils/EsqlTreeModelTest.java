/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Configuration;
import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlNodeBuilder;
import com.exxeta.iss.sonar.esql.parser.TreeFactory;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.symbols.SymbolModelImpl;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.typed.ActionParser;

public abstract class EsqlTreeModelTest<T extends Tree> {

	//protected final ActionParser<Tree> p;

	private ActionParser<Tree> getParser(GrammarRuleKey rule){
		return  new ActionParser<>(Charsets.UTF_8, EsqlLegacyGrammar.createGrammarBuilder(), EsqlGrammar.class,
				new TreeFactory(), new EsqlNodeBuilder(), rule);

	}
	
	/**
	 * Parse the given string and return the first descendant of the given kind.
	 *
	 * @param s
	 *            the string to parse
	 * @param rule
	 *            the rule used to parse the string
	 * @param descendantToReturn
	 *            the node kind to seek in the generated tree
	 * @return the node found for the given kind, null if not found.
	 */
	@SuppressWarnings("unchecked")
	protected T parse(String s, GrammarRuleKey rule, Kind descendantToReturn) throws Exception {
		Tree node = getParser(rule).parse(s);
		checkFullFidelity(node, s);
		return (T) getFirstDescendant((EsqlTree) node, descendantToReturn);
	}
	
	/**
	 * Parse the given string and return the first descendant of the given kind.
	 *
	 * @param s
	 *            the string to parse
	 * @param descendantToReturn
	 *            the node kind to seek in the generated tree
	 * @return the node found for the given kind, null if not found.
	 */
	protected T parse(String s, Kind descendantToReturn) throws Exception {
		return parse(s, descendantToReturn, descendantToReturn);
	}

	protected SymbolModelImpl symbolModel(InputFile file) {
		try {
			return symbolModel(file, null);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected ProgramTree parse(File file) {
		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			return (ProgramTree) getParser(EsqlLegacyGrammar.PROGRAM).parse(content);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected ProgramTree parse(String content) {
		return (ProgramTree) getParser(EsqlLegacyGrammar.PROGRAM).parse(content);
	}

	protected SymbolModelImpl symbolModel(InputFile file,  Configuration configuration) throws IOException {
		ProgramTree root = (ProgramTree) getParser(EsqlLegacyGrammar.PROGRAM).parse(file.contents());
		return (SymbolModelImpl) new EsqlVisitorContext(root, file, configuration).getSymbolModel();
	}

	protected EsqlVisitorContext context(InputFile file) throws IOException {
		ProgramTree root = (ProgramTree) getParser(EsqlLegacyGrammar.PROGRAM).parse(file.contents());
		return new EsqlVisitorContext(root, file, null);
	}

	private Tree getFirstDescendant(EsqlTree node, Kind descendantToReturn) {
		if (node.is(descendantToReturn)) {
			return node;
		}
		if (node.isLeaf()) {
			return null;
		}
		Iterator<Tree> childrenIterator = node.childrenIterator();
		while (childrenIterator.hasNext()) {
			Tree child = childrenIterator.next();
			if (child != null) {
				Tree childDescendant = getFirstDescendant((EsqlTree) child, descendantToReturn);
				if (childDescendant != null) {
					return childDescendant;
				}
			}
		}
		return null;
	}

	/**
	 * Return the concatenation of all the given node tokens value.
	 */
	protected String expressionToString(Tree node) {
		return SourceBuilder.build(node).trim();
	}

	private static void checkFullFidelity(Tree tree, String s) {
		assertThat(SourceBuilder.build(tree)).isEqualTo(s);
	}
}
