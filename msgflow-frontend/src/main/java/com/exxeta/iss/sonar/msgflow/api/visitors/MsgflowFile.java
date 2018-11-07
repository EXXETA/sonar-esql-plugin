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
package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.io.IOException;
import java.net.URI;

public interface MsgflowFile {

	  /**
	   * @deprecated use {@link MsgflowFile#fileName()} or {@link MsgflowFile#uri()}
	   */
  String relativePath();

  /**
   * File name with extension
   */
  String fileName();

  String contents() throws IOException;
  
  /**
   * Identifier of the file. The only guarantee is that it is unique in the project.
   * You should not assume it is a file:// URI.
   */
  URI uri();
}
