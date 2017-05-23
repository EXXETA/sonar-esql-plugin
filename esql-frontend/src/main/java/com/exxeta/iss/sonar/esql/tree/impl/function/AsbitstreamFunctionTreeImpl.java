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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.AsbitstreamFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.parser.TreeFactory.Tuple;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.sonar.sslr.api.typed.Optional;

public class AsbitstreamFunctionTreeImpl extends EsqlTree implements AsbitstreamFunctionTree {
	private InternalSyntaxToken asbitstreamKeyword;
	private InternalSyntaxToken openingParenthesis;
	private FieldReferenceTreeImpl fieldReference;

	private InternalSyntaxToken optionsSeparator;
	private ExpressionTree optionsExpression;
	private InternalSyntaxToken encodingSeparator;
	private ExpressionTree encodingExpression;
	private InternalSyntaxToken ccsidSeparator;
	private ExpressionTree ccsidExpression;
	private InternalSyntaxToken setSeparator;
	private ExpressionTree setExpression;
	private InternalSyntaxToken typeSeparator;
	private ExpressionTree typeExpression;
	private InternalSyntaxToken formatSeparator;
	private ExpressionTree formatExpression;

	private InternalSyntaxToken closingParenthesis;

	private boolean commaSeparated;

	
	public AsbitstreamFunctionTreeImpl(InternalSyntaxToken asbitstreamKeyword, InternalSyntaxToken openingParenthesis,
			FieldReferenceTreeImpl fieldReference,
			List<Tuple<InternalSyntaxToken, Optional<ExpressionTree>>> parameters,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.asbitstreamKeyword = asbitstreamKeyword;
		this.openingParenthesis = openingParenthesis;
		this.fieldReference = fieldReference;

		if (parameters != null) {
			commaSeparated = parameters.get(0).first().is(EsqlPunctuator.COMMA);
			if (commaSeparated) {
				if (parameters.size()>0){
					optionsSeparator = parameters.get(0).first();
					optionsExpression = parameters.get(0).second().isPresent()?parameters.get(0).second().get():null;
				}
				if (parameters.size()>1){
					encodingSeparator = parameters.get(1).first();
					encodingExpression = parameters.get(1).second().isPresent()?parameters.get(1).second().get():null;
				}
				if (parameters.size()>2){
					ccsidSeparator = parameters.get(2).first();
					ccsidExpression = parameters.get(2).second().isPresent()?parameters.get(2).second().get():null;
				}
				if (parameters.size()>3){
					setSeparator = parameters.get(3).first();
					setExpression = parameters.get(3).second().isPresent()?parameters.get(3).second().get():null;
				}
				if (parameters.size()>4){
					typeSeparator = parameters.get(4).first();
					typeExpression = parameters.get(4).second().isPresent()?parameters.get(4).second().get():null;
				}
				if (parameters.size()>5){
					formatSeparator = parameters.get(5).first();
					formatExpression = parameters.get(5).second().isPresent()?parameters.get(5).second().get():null;
				}
			} else {
				for (Tuple<InternalSyntaxToken, Optional<ExpressionTree>> tuple : parameters) {
					if (tuple.first().is(EsqlNonReservedKeyword.OPTIONS)){
						optionsSeparator = tuple.first();
						optionsExpression = tuple.second().get();
					}
					if (tuple.first().is(EsqlNonReservedKeyword.ENCODING)){
						encodingSeparator = tuple.first();
						encodingExpression = tuple.second().get();
					}
					if (tuple.first().is(EsqlNonReservedKeyword.CCSID)){
						ccsidSeparator = tuple.first();
						ccsidExpression = tuple.second().get();
					}
					if (tuple.first().is(EsqlNonReservedKeyword.SET)){
						setSeparator = tuple.first();
						setExpression = tuple.second().get();
					}
					if (tuple.first().is(EsqlNonReservedKeyword.TYPE)){
						typeSeparator = tuple.first();
						typeExpression = tuple.second().get();
					}
					if (tuple.first().is(EsqlNonReservedKeyword.FORMAT)){
						formatSeparator = tuple.first();
						formatExpression = tuple.second().get();
					}
				}
			}
		}

		this.closingParenthesis = closingParenthesis;
	}


	@Override
	public InternalSyntaxToken asbitstreamKeyword() {
		return asbitstreamKeyword;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	
	
	@Override
	public InternalSyntaxToken optionsSeparator() {
		return optionsSeparator;
	}


	@Override
	public ExpressionTree optionsExpression() {
		return optionsExpression;
	}


	@Override
	public InternalSyntaxToken encodingSeparator() {
		return encodingSeparator;
	}


	@Override
	public ExpressionTree encodingExpression() {
		return encodingExpression;
	}


	@Override
	public InternalSyntaxToken ccsidSeparator() {
		return ccsidSeparator;
	}


	@Override
	public ExpressionTree ccsidExpression() {
		return ccsidExpression;
	}


	@Override
	public InternalSyntaxToken setSeparator() {
		return setSeparator;
	}


	@Override
	public ExpressionTree setExpression() {
		return setExpression;
	}


	@Override
	public InternalSyntaxToken typeSeparator() {
		return typeSeparator;
	}


	@Override
	public ExpressionTree typeExpression() {
		return typeExpression;
	}


	@Override
	public InternalSyntaxToken formatSeparator() {
		return formatSeparator;
	}


	@Override
	public ExpressionTree formatExpression() {
		return formatExpression;
	}


	public boolean isCommaSeparated() {
		return commaSeparated;
	}


	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAsbitstreamFunction(this);

	}

	@Override
	public Kind getKind() {
		return Kind.ASBITSTREAM_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(asbitstreamKeyword, openingParenthesis, fieldReference,
				optionsSeparator, optionsExpression,
				encodingSeparator, encodingExpression,
				ccsidSeparator, ccsidExpression,
				setSeparator, setExpression,
				typeSeparator, typeExpression,
				formatSeparator, formatExpression,
				closingParenthesis);
	}

}
