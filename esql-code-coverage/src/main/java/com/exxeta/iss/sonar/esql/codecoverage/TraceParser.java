/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

package com.exxeta.iss.sonar.esql.codecoverage;

import com.exxeta.iss.sonar.esql.trace.InsertType;
import com.exxeta.iss.sonar.esql.trace.UserTraceLog;
import com.exxeta.iss.sonar.esql.trace.UserTraceType;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

@Slf4j
public class TraceParser extends DefaultHandler {

    public static final Logger LOG = Loggers.get(TraceParser.class.getName());
    private UserTraceLog result = null;

    private UserTraceType userTraceType;
    private InsertType insertType;

    public UserTraceLog getResult() {
        return result;
    }

    @Override
    public void startDocument() throws SAXException {
        result = new UserTraceLog();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName) {
            case "UserTraceLog":
                mapAttributes(attributes, result);
                break;
            case "UserTrace":
            case "Information":
                userTraceType = new UserTraceType();
                result.getUserTraceOrInformation().add(userTraceType);
                mapAttributes(attributes, userTraceType);
                break;
            case "Insert":
                insertType = new InsertType();
                if (userTraceType==null){
                    log.warn("Ignoring Insert element before first UserTraceType element");
                } else {
                    userTraceType.getInsert().add(insertType);
                    mapAttributes(attributes, insertType);
                }
                break;
            default:
                LOG.debug("unsupported trace tag " + localName);
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName) {
            case "UserTrace":
            case "Information":
                userTraceType = null;
            case "Insert":
                insertType = null;
            default:
                LOG.debug("unsupported trace tag " + localName);
        }
    }

    private void mapAttributes(Attributes attributes, Object obj) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            for (int i = 0; i < attributes.getLength(); i++) {
                String localName = attributes.getLocalName(i);
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    if (pd.getWriteMethod() != null && pd.getName().equals(localName)) {
                        if (pd.getPropertyType() == String.class) {
                            pd.getWriteMethod().invoke(obj, attributes.getValue(i));
                        } else if (pd.getPropertyType() == Integer.class) {
                            pd.getWriteMethod().invoke(obj, Integer.parseInt(attributes.getValue(i)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Cannot map attribute");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (insertType != null) {
            if (insertType.getValue() != null) {
                insertType.setValue(insertType.getValue() + new String(ch, start, length));
            } else {
                insertType.setValue(new String(ch, start, length));
            }
        }


    }
}
