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
package com.exxeta.iss.sonar.esql.codecoverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.trace.InsertType;
import com.exxeta.iss.sonar.esql.trace.UserTraceLog;
import com.exxeta.iss.sonar.esql.trace.UserTraceType;

public class TraceFileReaderTest {

	ExecutionDataVisitor testVisitor = new ExecutionDataVisitor() {
		private int moduleCount = 0;
		@Override
		public void visitModuleExecution(ModuleExecutionData data) {
			moduleCount++;
			assertNotNull(data);
			assertNotNull(data.getSchemaAndModuleName());
			assertThat (moduleCount).isLessThanOrEqualTo(3);
			assertThat (data.size()).isGreaterThan(0);
		}
	};
	
	@Test
	public void readerTest() {


		TraceFileReader reader = new TraceFileReader(
				new File("src/test/resources/codecoverage/MessageRoutingSampleTrace.txt")).readTrace(testVisitor);
		assertEquals(3, reader.getModuleCount());
		assertNotNull(reader.getModuleExecutionData(""));
		assertEquals(2, reader.getModuleExecutionData("").size());
		assertEquals(3, reader.getModuleExecutionData(".Refresh_memory_cache_Compute").size());
		assertEquals(36, reader.getModuleExecutionData(".Routing_using_memory_cache_Compute").size());

		reader = new TraceFileReader(null).readTrace(testVisitor);

	}
	
	@Test(expected=RuntimeException.class)
	public void testReaderWithNotExistingFile(){
		new TraceFileReader(new File("src/test/resources/codecoverage/doesNotExist.txt")).readTrace(testVisitor);
	}
	

	@Test
	public void xmlReaderTest() {

		ExecutionDataVisitor visitor = new ExecutionDataVisitor() {
			private int moduleCount = 0;

			@Override
			public void visitModuleExecution(ModuleExecutionData data) {
				moduleCount++;
				assertNotNull(data);
				assertThat (moduleCount).isLessThanOrEqualTo(20);
				assertThat (data.size()).isGreaterThan(0);

			}
		};

		TraceFileReader reader = new TraceFileReader(new File("src/test/resources/codecoverage/trace.xml"))
				.readTrace(visitor);
		assertEquals(2, reader.getModuleCount());
		assertNull(reader.getModuleExecutionData(""));
		assertEquals(5, reader.getModuleExecutionData(".Test_Compute.Main").size());

	}
	
	@Test(expected=RuntimeException.class)
	public void xmlReaderTestCorruptFile(){
		new TraceFileReader(new File("src/test/resources/codecoverage/corruptTrace.xml"))
		.readTrace(testVisitor);
	}

	@Test
	public void jaxBTest() throws JAXBException {
		assertThat(new UserTraceLog().getUserTraceOrInformation()).isNotNull();
		UserTraceLog trace = new TraceFileReader(new File("src/test/resources/codecoverage/trace.xml")).parseTraceXml();
		assertThat(trace.getBufferSize()).isEqualTo(0);
		assertThat(trace.getFileMode()).isEqualTo("safe");
		assertThat(trace.getFileSize()).isEqualTo(20480000);
		assertThat(trace.getTraceFilter()).isEqualTo("none");
		assertThat(trace.getTraceLevel()).isEqualTo("none");
		assertThat(trace.getUserTraceFilter()).isEqualTo("debugTrace");
		assertThat(trace.getUuid()).isEqualTo("UserTraceLog");
		assertThat(trace.getUserTraceOrInformation()).hasSize(75);
		assertThat(new UserTraceType().getInsert()).isNotNull();
		UserTraceType trace1 = trace.getUserTraceOrInformation().get(0);
		assertThat(trace1.getCatalog()).isEqualTo("BIPmsgs");
		assertThat(trace1.getFile()).isEqualTo(
				"F:\\build\\S1000_slot1\\S1000_P\\src\\DataFlowEngine\\Configuration\\ImbConfigurationManager.cpp");
		assertThat(trace1.getFunction()).isEqualTo("ImbConfigurationManager::evaluate");
		assertThat(trace1.getLabel()).isEqualTo("ConfigurationManager");
		assertThat(trace1.getLine()).isEqualTo("956");
		assertThat(trace1.getName()).isEqualTo("ConfigurationManager");
		assertThat(trace1.getNumber()).isEqualTo("4040");
		assertThat(trace1.getText())
				.isEqualTo("'Configuration changed successfully and committed to persistent store'");
		assertThat(trace1.getThread()).isEqualTo("9328");
		assertThat(trace1.getTimestamp()).isEqualTo("2017-09-15 04:28:04.403125");
		assertThat(trace1.getType()).isEqualTo("ComIbmConfigurationManager");
		assertThat(trace1.getInsert()).hasSize(3);
		InsertType insert = trace1.getInsert().get(0);
		assertThat(insert.getType()).isEqualTo("string");
		assertThat(insert.getValue()).isEqualTo("'default'");

	}

}
