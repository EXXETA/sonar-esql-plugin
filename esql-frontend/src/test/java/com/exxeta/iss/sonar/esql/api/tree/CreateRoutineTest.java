/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;


public class CreateRoutineTest {

	
	@Test
	public void createProcedureStatement(){
		assertThat(Kind.CREATE_PROCEDURE_STATEMENT)
		.matches("CREATE PROCEDURE swapParms (  IN parm1 CHARACTER,  OUT parm2  CHARACTER,  INOUT parm3 CHARACTER )BEGIN   SET parm2 = parm3;   SET parm3 = parm1; END;")
		.matches("CREATE PROCEDURE InsertVehicle(INOUT p_VehicleIdentifier CHARACTER,INOUT p_MMCAssignmentState CHARACTER,INOUT p_ResultCode INTEGER,INOUT p_ResultText CHARACTER)LANGUAGE DATABASE EXTERNAL NAME \"A.V.INSERT\";")
		.matches("CREATE PROCEDURE InsertV(INOUT p_id CHARACTER,INOUT p_state CHARACTER,INOUT p_ResultCode INTEGER,INOUT p_ResultText CHARACTER)LANGUAGE DATABASE EXTERNAL NAME \"A.V.INSERT\";")
		;
		
	}
	
	
}
