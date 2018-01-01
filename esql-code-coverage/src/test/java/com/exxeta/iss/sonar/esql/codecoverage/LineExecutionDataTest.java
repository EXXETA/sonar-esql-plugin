package com.exxeta.iss.sonar.esql.codecoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineExecutionDataTest {

	@Test
	public void equalTest(){
		
		LineExecutionData a = new LineExecutionData("f", "1", "s");
		LineExecutionData b = new LineExecutionData("f", "1", "s2");
		LineExecutionData c = new LineExecutionData(null, "1", "s");
		LineExecutionData d = new LineExecutionData("f", null, "s");
		LineExecutionData e = new LineExecutionData("f", "1", null);
		
		assertTrue(a.equals(a));
		assertFalse(a.equals(null));
		assertFalse(a.equals(b));
		assertTrue("f".equals(a.getFunction()));
		assertTrue("1".equals(a.getRelativeLine()));
		assertTrue("s".equals(a.getStatement()));
		assertEquals(a.hashCode(),129447);
		assertEquals(new LineExecutionData(null, null, null).hashCode(), 29791);
		assertFalse(a.equals(""));
		assertFalse(a.equals(new LineExecutionData(null, "", "")));
		assertFalse(a.equals(new LineExecutionData("f", null, "")));
		assertFalse(a.equals(new LineExecutionData("f", "1", null)));
		assertTrue(a.equals(new LineExecutionData("f", "1", "s")));
		assertFalse(c.equals(new LineExecutionData(null, "", "")));
		assertFalse(c.equals(new LineExecutionData("f", null, "")));
		assertFalse(c.equals(new LineExecutionData("f", "1", null)));
		assertTrue(c.equals(new LineExecutionData(null, "1", "s")));
		assertFalse(d.equals(new LineExecutionData(null, "", "")));
		assertFalse(d.equals(new LineExecutionData("f", null, "")));
		assertFalse(d.equals(new LineExecutionData("f", "1", null)));
		assertTrue(d.equals(new LineExecutionData("f", null, "s")));
		assertFalse(e.equals(new LineExecutionData("f", null, "")));
		assertTrue(e.equals(new LineExecutionData("f", "1", null)));
		assertFalse(e.equals(new LineExecutionData("f", "1", "aaa")));
		
	}
	
	
}
