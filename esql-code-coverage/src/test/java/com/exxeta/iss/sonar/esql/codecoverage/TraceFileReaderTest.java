package com.exxeta.iss.sonar.esql.codecoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.trace.InsertType;
import com.exxeta.iss.sonar.esql.trace.UserTraceLog;
import com.exxeta.iss.sonar.esql.trace.UserTraceType;

import static org.assertj.core.api.Assertions.assertThat;


import static org.hamcrest.core.Is.is;


public class TraceFileReaderTest {


	@Test
	public void readerTest() {
		
		ExecutionDataVisitor visitor = new ExecutionDataVisitor() {
			private int moduleCount = 0;
			@Override
			public void visitModuleExecution(ModuleExecutionData data) {
				moduleCount++;
				assertNotNull(data);
				assert(moduleCount<=3);
				assert(data.size()>0);
				
			}
		};
		
		TraceFileReader reader = new TraceFileReader(new File("src/test/resources/codecoverage/MessageRoutingSampleTrace.txt")).readTrace(visitor);
		assertEquals(3, reader.getModuleCount());
		assertNotNull(reader.getModuleExecutionData(""));
		assertEquals(2, reader.getModuleExecutionData("").size());
		assertEquals(3, reader.getModuleExecutionData(".Refresh_memory_cache_Compute").size());
		assertEquals(36, reader.getModuleExecutionData(".Routing_using_memory_cache_Compute").size());
		
	}
	@Test
	public void xmlReaderTest() {
		
		ExecutionDataVisitor visitor = new ExecutionDataVisitor() {
			private int moduleCount = 0;
			@Override
			public void visitModuleExecution(ModuleExecutionData data) {
				moduleCount++;
				assertNotNull(data);
				assert(moduleCount<=20);
				assert(data.size()>0);
				
			}
		};
		
		TraceFileReader reader = new TraceFileReader(new File("src/test/resources/codecoverage/trace.xml")).readTrace(visitor);
		assertEquals(1, reader.getModuleCount());
		assertNull(reader.getModuleExecutionData(""));
		assertEquals(7, reader.getModuleExecutionData(".Test_Compute").size());
		
	}
	
	
	@Test
	public void jaxBTest(){
		try {
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
			assertThat(trace1.getFile()).isEqualTo("F:\\build\\S1000_slot1\\S1000_P\\src\\DataFlowEngine\\Configuration\\ImbConfigurationManager.cpp");
			assertThat(trace1.getFunction()).isEqualTo("ImbConfigurationManager::evaluate");
			assertThat(trace1.getLabel()).isEqualTo("ConfigurationManager");
			assertThat(trace1.getLine()).isEqualTo("956");
			assertThat(trace1.getName()).isEqualTo("ConfigurationManager");
			assertThat(trace1.getNumber()).isEqualTo("4040");
			assertThat(trace1.getText()).isEqualTo("'Configuration changed successfully and committed to persistent store'");
			assertThat(trace1.getThread()).isEqualTo("9328");
			assertThat(trace1.getTimestamp()).isEqualTo("2017-09-15 04:28:04.403125");
			assertThat(trace1.getType()).isEqualTo("ComIbmConfigurationManager");
			assertThat(trace1.getInsert()).hasSize(3);
			InsertType insert = trace1.getInsert().get(0);
			assertThat(insert.getType()).isEqualTo("string");
			assertThat(insert.getValue()).isEqualTo("'default'");
			
		} catch (JAXBException e) {
			e.printStackTrace();
			assert(false);
		}
		
	}
	
}
