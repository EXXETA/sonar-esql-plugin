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
package com.exxeta.iss.sonar.esql.api;

import org.sonar.squidbridge.measures.CalculatedMetricFormula;
import org.sonar.squidbridge.measures.MetricDef;

public enum EsqlMetric implements MetricDef{
	FILES,
	  LINES,
	  LINES_OF_CODE,
	  COMMENT_LINES,
	  COMMENT_BLANK_LINES,
	  STATEMENTS,
	  COMPLEXITY,
	  ROUTINES;

	@Override
	public boolean aggregateIfThereIsAlreadyAValue() {
		return true;
	}

	@Override
	public CalculatedMetricFormula getCalculatedMetricFormula() {
		return null;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public boolean isCalculatedMetric() {
		return false;
	}

	@Override
	public boolean isThereAggregationFormula() {
		return true;
	}



}
