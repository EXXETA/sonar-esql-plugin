
package com.exxeta.iss.sonar.esql.trace;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InsertType", propOrder = {
    "value"
})
public class InsertType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "type")
    protected String type;

   
    public String getValue() {
        return value;
    }

   
    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

}
