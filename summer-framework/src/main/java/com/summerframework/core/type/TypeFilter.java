package com.summerframework.core.type;

import java.io.IOException;

public interface TypeFilter {

    boolean match(String matcher, Object object)
            throws IOException;
}
