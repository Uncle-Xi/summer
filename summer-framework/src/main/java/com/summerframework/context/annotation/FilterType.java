package com.summerframework.context.annotation;

public enum FilterType {

    /**
     * Filter candidates marked with a given annotation.
     */
    ANNOTATION,

    /**
     * Filter candidates assignable to a given type.
     */
    ASSIGNABLE_TYPE,

    /**
     * Filter candidates matching a given AspectJ type pattern expression.
     */
    ASPECTJ,

    /**
     * Filter candidates matching a given regex pattern.
     */
    REGEX,

    /** Filter candidates using a given custom
     */
    CUSTOM
}
