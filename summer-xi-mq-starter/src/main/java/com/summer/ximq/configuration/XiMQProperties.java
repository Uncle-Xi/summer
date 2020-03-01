package com.summer.ximq.configuration;

import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.boot.autoconfigure.properties.ConfigurationProperties;

/**
 * @description: XiMQProperties
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@ConfigurationProperties
public class XiMQProperties {

    @Value(value = "xi.mq.auto.offset.reset")
    public String offsetReset;

    @Value(value = "xi.mq.enable.auto.commit")
    public String autoCommit;

    @Value(value = "xi.mq.servers.connect.address")
    public String connectString;

    @Value(value = "xi.mq.topic.name")
    public String topicName;

    @Value(value = "xi.mq.group.id")
    public String groupId;

    @Value(value = "xi.mq.send.message.ack.model")
    public String ackModel;

    public String getOffsetReset() {
        return offsetReset;
    }

    public void setOffsetReset(String offsetReset) {
        this.offsetReset = offsetReset;
    }

    public String getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(String autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAckModel() {
        return ackModel;
    }

    public void setAckModel(String ackModel) {
        this.ackModel = ackModel;
    }
}
