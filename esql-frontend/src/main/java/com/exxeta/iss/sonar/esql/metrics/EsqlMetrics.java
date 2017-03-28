package com.exxeta.iss.sonar.esql.metrics;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class EsqlMetrics {
	public static final String PROCEDURES_KEY = "procedures";
	public static final Metric<Integer> PROCEDURES = new Metric.Builder(PROCEDURES_KEY, "Procedures",
			Metric.ValueType.INT).setDescription("Procedures").setDirection(Metric.DIRECTION_WORST)
					.setQualitative(false).setDomain(CoreMetrics.DOMAIN_SIZE).create();
	public static final String MODULES_KEY = "modules";
	public static final Metric<Integer> MODULES = new Metric.Builder(MODULES_KEY, "Modules", Metric.ValueType.INT)
			.setDescription("Modules").setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setDomain(CoreMetrics.DOMAIN_SIZE).create();
}
