/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.check;

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.google.common.collect.Lists;

@Rule(key = "UnknownMessageDomain")
public class UnknownMessageDomainCheck extends DoubleDispatchVisitorCheck {

	private static List<String> rootElements = Collections.unmodifiableList(Lists.newArrayList("InputRoot", "OutputRoot", "Root"));
	private static List<String> domains = Collections.unmodifiableList(Lists.newArrayList(
			"DFDL","MRM", 
			"XML", "XMLNS", "XMLNSC", 
			"JMS", "JMSMap", "JMSStream", "JMSTransport",  
			"IDOC", "MIME", "BLOB", "JSON", "SOAP",	"Properties", 
			"HTTPRequestHeader", "HTTPResponseHeader", "HTTPInputHeader", "HTTPReplyHeader", 
			"MQCIH", "MQDLH", "MQIIH",  "MQMD", "MQMDE", "MQPCF", "MQRFH", "MQRFH2", "MQRFH2C", "MQRMH", "MQSAPH", "MQWIH",
			"EmailOutputHeader", "EmailInputHeader", 
			"Collection", "*", 
			"DataObject", "IDOC", "SMQ_BMH", "SQL",
			"ComIbmAggregateReplyBody"
		));
	
	
	@Override
	public void visitFieldReference(FieldReferenceTree tree) {
		if (tree.pathElement().name().name() != null) {
			String pathElement1 = tree.pathElement().name().name().name();
			if (rootElements.contains(pathElement1) && !tree.pathElements().isEmpty()
					&& tree.pathElements().get(0).name() != null && tree.pathElements().get(0).name().name() != null) {
				String pathElement2 = tree.pathElements().get(0).name().name().name();
				if (!domains.contains(pathElement2)) {
					addIssue(tree, "Unknown domain \"" + pathElement2 + "\".");
				}
			}
		}
		super.visitFieldReference(tree);
	}

}
