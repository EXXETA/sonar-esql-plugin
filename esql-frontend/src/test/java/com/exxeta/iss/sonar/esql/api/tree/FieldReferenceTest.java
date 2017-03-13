package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class FieldReferenceTest {


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
		;

	}
	@Test
	public void fielReference() {
		assertThat(Kind.FIELD_REFERENCE)
			.matches("a")
			.matches("InputRoot")
			.notMatches("a.")
			.matches("a.b[]")
			.matches("a.b[].c")
			.notMatches("")
			.matches("(XML.Element)NSpace1:Element1[2]");
		

		
	}
	
}
