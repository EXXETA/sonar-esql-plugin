/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.google.common.collect.ImmutableList;

/**
 * This Java Class is created to check the logic wheather the message domain is valid or not.
 * @author Sapna Singh
 *
 */
@Rule(key = "MessageDomainNotvalid")
public class MessageDomainNotvalidCheck extends DoubleDispatchVisitorCheck{
	
	private static final String MESSAGE = "The message domain may not be valid.";
	private static final List<String> DOMAINS = ImmutableList.of("MQMD", "SOAP", "XML", "XMLNS", "XMLNSC", "BLOB",
			"JSON", "MRM");
	
	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		int i = 0;
		
		for (String line : lines) {
			i = i + 1;	
	
		String upperCaseTheLine = line.toUpperCase().trim();
	
	boolean outputRootAssigned = false;
    if(upperCaseTheLine.startsWith("SET OUTPUTROOT.") && !upperCaseTheLine.startsWith("SET OUTPUTROOT.*"))
        outputRootAssigned = true;
    if(outputRootAssigned)
    {
        int pos1 = upperCaseTheLine.indexOf('.');
        String value = upperCaseTheLine.substring(pos1 + 1);
        int pos2 = value.indexOf('.');
        if(pos2 > 0)
        {
            int posEquals = value.indexOf('=');
            if(posEquals > -1)
            {
                value = value.substring(0, posEquals);
                value = value.trim();
            }
            String[] parts = value.split("\\.");
            String thisDomain = parts[0];
            if(!DOMAINS.contains(thisDomain) && !thisDomain.startsWith("{"))
            {
               
            	addIssue(new LineIssue(this, i,   MESSAGE ));
	
           }
            }
              }
           }
	
	
        }
	
	

}
