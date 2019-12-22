package com.exxeta.iss.sonar.esql.checks.verifier;

import java.text.MessageFormat;
import java.util.Arrays;

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
