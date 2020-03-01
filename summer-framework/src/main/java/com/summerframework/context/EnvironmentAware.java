package com.summerframework.context;

import com.summerframework.beans.Aware;
import com.summerframework.core.env.Environment;

public interface EnvironmentAware extends Aware {

    void setEnvironment(Environment environment);
}