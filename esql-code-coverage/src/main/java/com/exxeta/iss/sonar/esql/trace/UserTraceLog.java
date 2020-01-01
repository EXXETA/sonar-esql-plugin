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
package com.exxeta.iss.sonar.esql.trace;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "", propOrder = { "userTraceOrInformation" })
@XmlRootElement(name = "UserTraceLog")
public class UserTraceLog {

//	@XmlElementRefs({ @XmlElementRef(name = "Information", type = JAXBElement.class, required = false),
//			@XmlElementRef(name = "UserTrace", type = JAXBElement.class, required = false) })
	@XmlElement(name = "UserTrace")
	protected List<UserTraceType> userTraceOrInformation;
	@XmlAttribute(name = "uuid")
	protected String uuid;
	@XmlAttribute(name = "userTraceLevel")
	protected String userTraceLevel;
	@XmlAttribute(name = "traceLevel")
	protected String traceLevel;
	@XmlAttribute(name = "userTraceFilter")
	protected String userTraceFilter;
	@XmlAttribute(name = "traceFilter")
	protected String traceFilter;
	@XmlAttribute(name = "fileSize")
	protected Integer fileSize;
	@XmlAttribute(name = "bufferSize")
	protected Integer bufferSize;
	@XmlAttribute(name = "fileMode")
	protected String fileMode;

	public List<UserTraceType> getUserTraceOrInformation() {
		if (userTraceOrInformation == null) {
			userTraceOrInformation = new ArrayList<UserTraceType>();
		}
		return this.userTraceOrInformation;
	}

	public String getUuid() {
		return uuid;
	}
	public String getTraceLevel() {
		return traceLevel;
	}

	public String getUserTraceFilter() {
		return userTraceFilter;
	}

	public String getTraceFilter() {
		return traceFilter;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public Integer getBufferSize() {
		return bufferSize;
	}

	public String getFileMode() {
		return fileMode;
	}

}
