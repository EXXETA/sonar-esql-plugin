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
package com.exxeta.iss.sonar.esql.api.tree;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.sonar.api.internal.google.common.collect.Lists;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CallStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class SeparatedListTest extends EsqlTreeModelTest<Tree> {

	@Test
	public void test() throws Exception {
		CallStatementTreeImpl tree1 = (CallStatementTreeImpl) parse("CALL f(a,b) ;", Tree.Kind.CALL_STATEMENT);
		SeparatedList<ExpressionTree> list = tree1.parameterList();
		assertThat(list.toArray()).hasSize(2);
		assertThat(list.toArray(new Tree[] {})).hasSize(2);
		ExpressionTree a = list.get(0);
		ExpressionTree b = list.get(1);
		SeparatedList<ExpressionTree> list2 = new SeparatedList<>(new ArrayList<>(), new ArrayList<>());
		list2.add(a);
		assertThat(list2.contains(a)).isTrue();
		list2.remove(a);
		assertThat(list2.contains(a)).isFalse();
		list2.add(a);
		list2.clear();
		assertThat(list2.contains(a)).isFalse();
		list2.add(a);
		list2.addAll(Collections.emptyList());
		list2.removeAll(Collections.emptyList());
		assertThat(list2).hasSize(1);
		assertThat(list2.containsAll(Lists.newArrayList(a,b))).isFalse();
		list2.clear();
		list2.addAll(Lists.newArrayList(a,b));
		assertThat(list2).hasSize(2);
		list2.clear();
		list2.addAll(0,Lists.newArrayList(a,b));
		assertThat(list2).hasSize(2);
		list2.set(1, a);
		assertThat(list2.get(1)).isEqualTo(a);
		list2.clear();
		list2.add(0, a);
		assertThat(list2.indexOf(a)).isEqualTo(0);
		
		list2.remove(0);
		assertThat(list2).hasSize(0);
		assertThat(list2.containsAll(Lists.newArrayList(a))).isFalse();
		list2.add(a);
		assertThat(list2.lastIndexOf(a)).isEqualTo(0);
		assertThat(list2.subList(0, 0)).hasSize(0);
		assertThat(list2.listIterator().next()).isEqualTo(a);
		assertThat(list2.listIterator(0).next()).isEqualTo(a);
		
	}
}
