package com.summerframework.boot.autoconfigure;

import com.summerframework.context.annotation.Condition;
import com.summerframework.context.annotation.ConditionContext;
import com.summerframework.core.type.AnnotatedTypeMetadata;

/**
 * @description: OnPropertyCondition
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class OnPropertyCondition  implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }
}
