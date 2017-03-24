package com.exxeta.iss.sonar.esql.api;

import org.sonar.api.ExtensionPoint;
import org.sonar.api.batch.BatchSide;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableList;

/**
 * Extension point to create custom rule repository for ESQL.
 */
@Beta
@ExtensionPoint
@BatchSide
public abstract class CustomEsqlRulesDefinition implements RulesDefinition {

  /**
   * Defines rule repository with check metadata from check classes' annotations.
   * This method should be overridden if check metadata are provided via another format,
   * e.g: XMl, JSON.
   */
  @Override
  public void define(RulesDefinition.Context context) {
    RulesDefinition.NewRepository repo = context.createRepository(repositoryKey(), "esql").setName(repositoryName());

    // Load metadata from check classes' annotations
    new AnnotationBasedRulesDefinition(repo, "esql").addRuleClasses(false, ImmutableList.copyOf(checkClasses()));

    repo.done();
  }

  /**
   * Name of the custom rule repository.
   */
  public abstract String repositoryName();

  /**
   * Key of the custom rule repository.
   */
  public abstract String repositoryKey();

  /**
   * Array of the custom rules classes.
   */
  public abstract Class[] checkClasses();
}
