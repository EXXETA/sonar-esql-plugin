package com.exxeta.iss.sonar.esql.checks.verifier;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckMessageTest {
	 @Test
	  public void testFormatDefaultMessage() {
	    CheckMessage message = new CheckMessage(null, "Value is {0,number,integer}, expected value is {1,number,integer}.", 3, 7);
	    assertThat(message.formatDefaultMessage()).isEqualTo("Value is 3, expected value is 7.");
	  }
	  @Test
	  public void testNotFormatMessageWithoutParameters() {
	    CheckMessage message = new CheckMessage(null, "public void main(){."); // This message can't be used as a pattern by the MessageFormat
	    // class
	    assertThat(message.formatDefaultMessage()).isEqualTo("public void main(){.");
	  }
}
