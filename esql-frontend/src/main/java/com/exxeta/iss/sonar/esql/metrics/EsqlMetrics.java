/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import com.google.common.collect.Lists;

public class EsqlMetrics implements Metrics {
	public static final String PROCEDURES_KEY = "procedures";
	public static final Metric<Integer> PROCEDURES = new Metric.Builder(PROCEDURES_KEY, "Procedures",
			Metric.ValueType.INT).setDescription("Procedures").setDirection(Metric.DIRECTION_WORST)
					.setQualitative(false).setDomain(CoreMetrics.DOMAIN_SIZE).create();
	public static final String MODULES_KEY = "modules";
	public static final Metric<Integer> MODULES = new Metric.Builder(MODULES_KEY, "Modules", Metric.ValueType.INT)
			.setDescription("Modules").setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setDomain(CoreMetrics.DOMAIN_SIZE).create();
	public static final String MODULE_COMPLEXITY_KEY = "moduleComplexity";
	public static final Metric<Integer> MODULE_COMPLEXITY = new Metric.Builder(MODULE_COMPLEXITY_KEY, "Module Complexity", Metric.ValueType.INT)
			.setDescription("Module Complexity").setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setDomain(CoreMetrics.DOMAIN_SIZE).create();
	
	private static final List<Metric> metrics = Lists.newArrayList(PROCEDURES, MODULES, MODULE_COMPLEXITY);
	@Override
	public List<Metric> getMetrics() {
		return metrics;
	}
}
