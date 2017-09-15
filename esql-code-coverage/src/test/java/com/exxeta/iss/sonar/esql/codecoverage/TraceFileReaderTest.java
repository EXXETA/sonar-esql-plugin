package com.exxeta.iss.sonar.esql.codecoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

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
	
	
}
