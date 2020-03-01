package com.summerframework.context.annotation;

import com.summerframework.core.type.AnnotatedTypeMetadata;

@FunctionalInterface
public interface Condition {

    boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
}
