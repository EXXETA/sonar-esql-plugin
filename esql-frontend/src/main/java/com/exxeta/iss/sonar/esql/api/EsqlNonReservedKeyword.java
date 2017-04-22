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
package com.exxeta.iss.sonar.esql.api;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum EsqlNonReservedKeyword implements TokenType, GrammarRuleKey {
	
	
	AND("AND"),
	ANY("ANY"),
	AS("AS"),
	ATOMIC("ATOMIC"),
	ATTACH("ATTACH"),
	BEGIN("BEGIN"),
	BETWEEN("BETWEEN"),
	BIT("BIT"),
	BLOB("BLOB"),
	BOOLEAN("BOOLEAN"),
	BROKER("BROKER"),
	BY("BY"),
	CALL("CALL"),
	CATALOG("CATALOG"),
	CCSID("CCSID"),
	CHAR("CHAR"),
	CHARACTER("CHARACTER"),
	CLR("CLR"),
	COMPUTE("COMPUTE"),
	CONDITION("CONDITION"),
	CONSTANT("CONSTANT"),
	CONTINUE("CONTINUE"),
	COORDINATED("COORDINATED"),
	COUNT("COUNT"),
	CREATE("CREATE"),
	CURRENT_DATE("CURRENT_DATE"),
	CURRENT_GMTDATE("CURRENT_GMTDATE"),
	CURRENT_GMTTIME("CURRENT_GMTTIME"),
	CURRENT_GMTTIMESTAMP("CURRENT_GMTTIMESTAMP"),
	CURRENT_TIME("CURRENT_TIME"),
	CURRENT_TIMESTAMP("CURRENT_TIMESTAMP"),
	//DATA("DATA"),
	DATABASE("DATABASE"),
	DATE("DATE"),
	DAY("DAY"),
	DAYOFWEEK("DAYOFWEEK"),
	DAYOFYEAR("DAYOFYEAR"),
	DAYS("DAYS"),
	DECIMAL("DECIMAL"),
	DECLARE("DECLARE"),
	DEFAULT("DEFAULT"),
	DELETE("DELETE"),
	DETACH("DETACH"),
	DO("DO"),
	DOMAIN("DOMAIN"),
	DOT_NET(".NET"),
	DYNAMIC("DYNAMIC"),
	ELSE("ELSE"),
	ELSEIF("ELSEIF"),
	ENCODING("ENCODING"),
	END("END"),
	ENVIRONMENT("ENVIRONMENT"),
	ESCAPE("ESCAPE"),
	ESQL("ESQL"),
	EVAL("EVAL"),
	EVENT("EVENT"),
	EXCEPTION("EXCEPTION"),
	EXISTS("EXISTS"),
	EXIT("EXIT"),
	EXTERNAL("EXTERNAL"),
	FALSE("FALSE"),
	FIELD("FIELD"),
	FILTER("FILTER"),
	FINALIZE("FINALIZE"),
	FIRSTCHILD("FIRSTCHILD"),
	FLOAT("FLOAT"),
	FOR("FOR"),
	FORMAT("FORMAT"),
	FOUND("FOUND"),
	FULL("FULL"),
	FUNCTION("FUNCTION"),
	GMTTIME("GMTTIME"),
	GMTTIMESTAMP("GMTTIMESTAMP"),
	GROUP("GROUP"),
	HANDLER("HANDLER"),
	HAVING("HAVING"),
	HOUR("HOUR"),
	IDENTITY("IDENTITY"),
	IF("IF"),
	IN("IN"),
	INF("INF"),
	INFINITY("INFINITY"),
	INOUT("INOUT"),
	INSERT("INSERT"),
	INT("INT"),
	INTEGER("INTEGER"),
	INTERVAL("INTERVAL"),
	INTO("INTO"),
	IS("IS"),
	ISLEAPYEAR("ISLEAPYEAR"),
	ITERATE("ITERATE"),
	JAVA("JAVA"),
	LABEL("LABEL"),
	LANGUAGE("LANGUAGE"),
	LAST("LAST"),
	LASTCHILD("LASTCHILD"),
	LEAVE("LEAVE"),
	LIKE("LIKE"),
	LIST("LIST"),
	LOCALTIMEZONE("LOCALTIMEZONE"),
	LOG("LOG"),
	LOOP("LOOP"),
	MAX("MAX"),
	MESSAGE("MESSAGE"),
	MIN("MIN"),
	MINUTE("MINUTE"),
	MODIFIES("MODIFIES"),
	MODULE("MODULE"),
	MONTH("MONTH"),
	MONTHS("MONTHS"),
	MOVE("MOVE"),
	NAME("NAME"),
	NAMESPACE("NAMESPACE"),
	NAN("NAN"),
	NEXTSIBLING("NEXTSIBLING"),
	NONE("NONE"),
	NOT("NOT"),
	NULL("NULL"),
	NULLABLE("NULLABLE"),
	NUM("NUM"),
	NUMBER("NUMBER"),
	OF("OF"),
	OPTIONS("OPTIONS"),
	OR("OR"),
	ORDER("ORDER"),
	OUT("OUT"),
	PARENT("PARENT"),
	PARSE("PARSE"),
	PASSTHRU("PASSTHRU"),
	PATH("PATH"),
	PLACING("PLACING"),
	PREVIOUSSIBLING("PREVIOUSSIBLING"),
	PROCEDURE("PROCEDURE"),
	PROPAGATE("PROPAGATE"),
	QUARTEROFYEAR("QUARTEROFYEAR"),
	QUARTERS("QUARTERS"),
	READS("READS"),
	REFERENCE("REFERENCE"),
	REPEAT("REPEAT"),
	RESIGNAL("RESIGNAL"),
	RESULT("RESULT"),
	RETURN("RETURN"),
	RETURNS("RETURNS"),
	ROW("ROW"),
	SAMEFIELD("SAMEFIELD"),
	SCHEMA("SCHEMA"),
	SECOND("SECOND"),
	SELECT("SELECT"),
	SET("SET"),
	SETS("SETS"),
	SEVERITY("SEVERITY"),
	SHARED("SHARED"),
	SHORT("SHORT"),
	SOME("SOME"),
	SQL("SQL"),
	SQLCODE("SQLCODE"),
	SQLERRORTEXT("SQLERRORTEXT"),
	SQLEXCEPTION("SQLEXCEPTION"),
	SQLNATIVEERROR("SQLNATIVEERROR"),
	SQLSTATE("SQLSTATE"),
	SQLWARNING("SQLWARNING"),
	SUM("SUM"),
	TERMINAL("TERMINAL"),
	THE("THE"),
	THEN("THEN"),
	THROW("THROW"),
	TIME("TIME"),
	TIMESTAMP("TIMESTAMP"),
	TO("TO"),
	TRACE("TRACE"),
	TRUE("TRUE"),
	TYPE("TYPE"),
	UNCOORDINATED("UNCOORDINATED"),
	UNKNOWN("UNKNOWN"),
	UNTIL("UNTIL"),
	UPDATE("UPDATE"),
	USER("USER"),
	UUIDASBLOB("UUIDASBLOB"),
	UUIDASCHAR("UUIDASCHAR"),
	VALUE("VALUE"),
	VALUES("VALUES"),
	WEEKOFMONTH("WEEKOFMONTH"),
	WEEKOFYEAR("WEEKOFYEAR"),
	WEEKS("WEEKS"),
	WHERE("WHERE"),
	WHILE("WHILE"),
	YEAR("YEAR"), 
	QUARTER("QUARTER"), 
	EXTRACT("EXTRACT"), 
	ROUND("ROUND"), 
	MODE("MODE"), 
	ROUND_UP("ROUND_UP"),
	ROUND_DOWN("ROUND_DOWN"),
	ROUND_CEILING("ROUND_CEILING"),
	ROUND_FLOOR("ROUND_FLOOR"),
	ROUND_HALF_UP("ROUND_HALF_UP"),
	ROUND_HALF_EVEN("ROUND_HALF_EVEN"),
	ROUND_HALF_DOWN("ROUND_HALF_DOWN"), 
	OVERLAY("OVERLAY"), 
	POSITION("POSITION");
	
private final String value;

private EsqlNonReservedKeyword(String value) {
  this.value = value;
}

public String getName() {
  return name();
}

public String getValue() {
  return value;
}

public boolean hasToBeSkippedFromAst(AstNode node) {
  return false;
}

public static String[] keywordValues() {
  EsqlNonReservedKeyword[] keywordsEnum = EsqlNonReservedKeyword.values();
  String[] keywords = new String[keywordsEnum.length];
  for (int i = 0; i < keywords.length; i++) {
    keywords[i] = keywordsEnum[i].getValue();
  }
  return keywords;
}

}
