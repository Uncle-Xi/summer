package com.summerframework.boot.web.server;

import com.summerframework.context.ConfigurableApplicationContext;

public abstract class GenericServer implements Server{

    ConfigurableApplicationContext context;

    public ConfigurableApplicationContext getContext() {
        return context;
    }

    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;
        onRefresh(context);
    }

    protected void onRefresh(ConfigurableApplicationContext context){};
}
