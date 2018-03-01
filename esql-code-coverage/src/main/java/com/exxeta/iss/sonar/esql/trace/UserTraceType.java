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

    public String getThread() {
        return thread;
    }

    public String getFunction() {
        return function;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getNumber() {
        return number;
    }

    public String getFile() {
        return file;
    }

    public String getLine() {
        return line;
    }

}
