package com.exxeta.iss.sonar.esql.api.visitors;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;

public interface Issue {

	  EsqlCheck check();

	  @Nullable
	  Double cost();

	  Issue cost(double cost);

	}