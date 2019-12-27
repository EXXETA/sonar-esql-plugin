package com.exxeta.iss.sonar.msgflow.tree.impl.node.mq;

import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQHeaderNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;
import lombok.Getter;
import org.w3c.dom.Node;

@Getter
public class MQHeaderNodeImpl extends AbstractMessageFlowNode implements MQHeaderNode {

    /*
    Options:
    "Delete header"
    "Modify header"
    "Carry forward header"
    "Inherit from header"
    "Delete header"
     */
    private final String mqmdHeaderOptions;
    private final String codedCharacterSetIdentifer;
    private final String format;
    private final String versionNumber;
    private final String messageType;
    private final String messageExpiry;
    private final String feedbackOrReasonCode;
    private final String messagePriority;
    private final String messagePersistence;
    private final String messageIdentifier;
    private final String correlationIdentifier;
    private final String replyToQueue;
    private final String replyToQueueManager;

    public MQHeaderNodeImpl(final Node node, final String id, final String name, final int locationX,
                            final int locationY, String mqmdHeaderOptions, String codedCharacterSetIdentifer,
                            String format, String versionNumber, String messageType, String messageExpiry,
                            String feedbackOrReasonCode, String messagePriority, String messagePersistence,
                            String messageIdentifier, String correlationIdentifier, String replyToQueue,
                            String replyToQueueManager) {
        super(node, id, name, locationX, locationY);

        this.mqmdHeaderOptions = mqmdHeaderOptions;
        this.codedCharacterSetIdentifer = codedCharacterSetIdentifer;
        this.format = format;
        this.versionNumber = versionNumber;
        this.messageType = messageType;
        this.messageExpiry = messageExpiry;
        this.feedbackOrReasonCode = feedbackOrReasonCode;
        this.messagePriority = messagePriority;
        this.messagePersistence = messagePersistence;
        this.messageIdentifier = messageIdentifier;
        this.correlationIdentifier = correlationIdentifier;
        this.replyToQueue = replyToQueue;
        this.replyToQueueManager = replyToQueueManager;
    }

    @Override
    public void accept(final DoubleDispatchMsgflowVisitor visitor) {
        visitor.visitMQHeaderNode(this);
    }

    @Override
    public Kind getKind() {
        return Kind.MQ_HEADER;
    }

}
