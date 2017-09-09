
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

	public void setUuid(String value) {
		this.uuid = value;
	}

	public String getUserTraceLevel() {
		return userTraceLevel;
	}

	public void setUserTraceLevel(String value) {
		this.userTraceLevel = value;
	}

	public String getTraceLevel() {
		return traceLevel;
	}

	public void setTraceLevel(String value) {
		this.traceLevel = value;
	}

	public String getUserTraceFilter() {
		return userTraceFilter;
	}

	public void setUserTraceFilter(String value) {
		this.userTraceFilter = value;
	}

	public String getTraceFilter() {
		return traceFilter;
	}

	public void setTraceFilter(String value) {
		this.traceFilter = value;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer value) {
		this.fileSize = value;
	}

	public Integer getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(Integer value) {
		this.bufferSize = value;
	}

	public String getFileMode() {
		return fileMode;
	}

	public void setFileMode(String value) {
		this.fileMode = value;
	}

}
