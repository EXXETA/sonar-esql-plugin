package com.exxeta.iss.sonar.msgflow.api.tree.node.mq;

import com.exxeta.iss.sonar.msgflow.api.tree.Located;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;

public interface MQHeaderNode extends MessageFlowNode, Located {
    String getMqmdHeaderOptions();
    String getCodedCharacterSetIdentifer();
    String getFormat();
    String getVersionNumber();
    String getMessageType();
    String getMessageExpiry();
    String getFeedbackOrReasonCode();
    String getMessagePriority();
    String getMessagePersistence();
    String getMessageIdentifier();
    String getCorrelationIdentifier();
    String getReplyToQueue();
    String getReplyToQueueManager();
}

