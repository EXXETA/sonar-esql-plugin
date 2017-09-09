

package com.exxeta.iss.sonar.esql.trace;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserTraceType", propOrder = {
    "insert"
})
public class UserTraceType {

    @XmlElement(name = "Insert")
    protected List<InsertType> insert;
    @XmlAttribute(name = "timestamp")
    protected String timestamp;
    @XmlAttribute(name = "thread")
    protected String thread;
    @XmlAttribute(name = "function")
    protected String function;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "text")
    protected String text;
    @XmlAttribute(name = "catalog")
    protected String catalog;
    @XmlAttribute(name = "number")
    protected String number;
    @XmlAttribute(name = "file")
    protected String file;
    @XmlAttribute(name = "line")
    protected String line;

    public List<InsertType> getInsert() {
        if (insert == null) {
            insert = new ArrayList<InsertType>();
        }
        return this.insert;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String value) {
        this.thread = value;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String value) {
        this.function = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String value) {
        this.label = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String value) {
        this.catalog = value;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String value) {
        this.number = value;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String value) {
        this.file = value;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String value) {
        this.line = value;
    }

}
