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
package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class FieldReferenceTest extends EsqlTreeModelTest<FieldReferenceTree> {


	@Test
	public void pathElement(){
		assertThat(Kind.PATH_ELEMENT)
		.matches("a")
		.matches("Field [> ]")
		.notMatches("")
		.notMatches("a.")
		.matches("(XML)Element1")
		.matches("(XML.Element)Element1[2]")
		.matches("(XML.Element)NSpace1:Element1")
		.matches("(XML.Element)NSpace1:Element1[2]")
		.matches("\"aItem\"")
		.matches("\"Item\"")
		.matches("[]")
		.matches("a[i-1]")
		.matches("(XML.Element)")// IIB accepts this Element although it is not allowed by the documentation.
		.matches("PRICE")
		.notMatches("a a")
		.matches("cursor")
		;
		

	}
	@Test
	public void fielReference() {
		assertThat(Kind.FIELD_REFERENCE)
		.matches("(XML.Element)NSpace1:Element1[2]")
			.matches("a")
			.matches("InputRoot")
			.notMatches("a.")
			.matches("a.b[]")
			.matches("a.b[].c")
			.notMatches("")
			.matches("Body.Invoice.Purchases.\"Item\"[]");
		
	}
	
	@Test
	public void model() throws Exception{
		FieldReferenceTree tree = parse("(XML.Element)NSpace1:Element1[<2].{nsexp()}:{nameexp()}.*:abc.:aaa", Kind.FIELD_REFERENCE);
		assertNotNull(tree);
		assertNotNull(tree.pathElement());
		PathElementTree firstElement = tree.pathElement();
		assertNotNull(tree.pathElements());
		assertNotNull(tree.pathElements().get(0));
		PathElementTypeTree type = firstElement.type();
		PathElementNamespaceTree namespace = firstElement.namespace();
		PathElementNameTree name = firstElement.name();
		IndexTree index = firstElement.index();
		assertNotNull(type);
		assertNotNull(namespace);
		assertNotNull(name);
		assertNotNull(index);
		assertNotNull(type.typeOpenParen());
		assertNotNull(type.typeExpression());
		assertNotNull(type.typeCloseParen());
		assertNotNull(namespace.namespace());
		assertNull(namespace.namespaceCurlyOpen());
		assertNull(namespace.namespaceExpression());
		assertNull(namespace.namespaceCurlyClose());
		assertNull(namespace.namespaceStar());
		assertNotNull(namespace.colon());
		assertNotNull(name.name());
		assertNull(name.nameCurlyOpen());
		assertNull(name.nameExpression());
		assertNull(name.nameCurlyClose());
		assertNotNull(index.openBracket());
		assertNotNull(index.direction());
		assertNotNull(index.index());
		assertNotNull(index.closeBracket());
	}
	
}
