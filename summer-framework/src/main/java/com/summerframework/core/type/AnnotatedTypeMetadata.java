package com.summerframework.core.type;

import java.util.Map;

public interface AnnotatedTypeMetadata {

    boolean isAnnotated(String annotationName);

    Map<String, Object> getAnnotationAttributes(String annotationName);
}
