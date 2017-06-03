/*
 * SonarQube Esql Plugin
 * Copyright (C) 2011-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.exxeta.iss.sonar.esql.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import org.sonar.api.config.Settings;
import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.compat.CompatibleInputFile;
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
	 * @param descendantToReturn
	 *            the node kind to seek in the generated tree
	 * @return the node found for the given kind, null if not found.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Tree> T parse(String s, Kind descendantToReturn) throws Exception {
		Tree node = getParser(descendantToReturn).parse(s);
		checkFullFidelity(node, s);
		return (T) getFirstDescendant((EsqlTree) node, descendantToReturn);
	}

	protected SymbolModelImpl symbolModel(CompatibleInputFile file) {
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

	protected SymbolModelImpl symbolModel(CompatibleInputFile file, Settings settings) throws IOException {
		ProgramTree root = (ProgramTree) getParser(EsqlLegacyGrammar.PROGRAM).parse(file.contents());
		return (SymbolModelImpl) new EsqlVisitorContext(root, file, settings).getSymbolModel();
	}

	protected EsqlVisitorContext context(CompatibleInputFile file) throws IOException {
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
