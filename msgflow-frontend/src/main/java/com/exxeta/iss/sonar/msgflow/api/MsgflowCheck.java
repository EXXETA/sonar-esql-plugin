/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.msgflow.api;

import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;
import com.exxeta.iss.sonar.msgflow.api.visitors.Issue;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.exxeta.iss.sonar.msgflow.api.visitors.PreciseIssue;
import com.google.common.annotations.Beta;

/**
 * Marker interface for all ESQL checks.
 */
@Beta
public interface MsgflowCheck {

	<T extends Issue> T addIssue(T issue);

	PreciseIssue addIssue(Tree tree, String message);

	List<Issue> scanFile(MsgflowVisitorContext context);
}
