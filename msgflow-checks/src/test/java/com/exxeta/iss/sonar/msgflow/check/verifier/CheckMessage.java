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
package com.exxeta.iss.sonar.msgflow.check.verifier;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * This class was copy&pasted from sslr-squid-bridge to avoid dependency on it
 */
public class CheckMessage {

  private Integer line;
  private Double cost;
  private final Object check;
  private final String defaultMessage;
  private final Object[] messageArguments;
  private Boolean bypassExclusion;

  public CheckMessage(Object check, String message, Object... messageArguments) {
    this.check = check;
    this.defaultMessage = message;
    this.messageArguments = messageArguments;
  }


  public void setLine(int line) {
    this.line = line;
  }


  public Integer getLine() {
    return line;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public Double getCost() {
    return cost;
  }

  public void setBypassExclusion(boolean bypassExclusion) {
    this.bypassExclusion = bypassExclusion;
  }

  public boolean isBypassExclusion() {
    return bypassExclusion != null && bypassExclusion;
  }


  public Object getCheck() {
    return check;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }

  public Object[] getMessageArguments() {
    return messageArguments;
  }


  public String getText() {
    return formatDefaultMessage();
  }

  @Override
  public String toString() {
    return "CheckMessage{" +
      "line=" + line +
      ", cost=" + cost +
      ", check=" + check +
      ", defaultMessage='" + defaultMessage + '\'' +
      ", messageArguments=" + Arrays.toString(messageArguments) +
      ", bypassExclusion=" + bypassExclusion +
      '}';
  }

  public String formatDefaultMessage() {
    if (messageArguments.length == 0) {
      return defaultMessage;
    } else {
      return MessageFormat.format(defaultMessage, messageArguments);
    }
  }

}