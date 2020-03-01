package com.summer.xicp.template;

import com.summer.xicp.configuration.XiCpProperties;
import com.summerframework.beans.factory.annotation.Autowired;
import com.xicp.EventType;
import com.xicp.WatchedEvent;
import com.xicp.Watcher;
import com.xicp.XiCP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: XiCPTemplate
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XiCPTemplate implements Watcher {

    Map<String, PostProcess> nodeDataChangedMap = new HashMap<>();
    Map<String, PostProcess> nodeChildrenChangedMap = new HashMap<>();
    XiCP xc;
    XiCpProperties xiCpProperties;

    public XiCPTemplate(XiCpProperties xiCpProperties){
        this.xiCpProperties = xiCpProperties;
        try {
            this.xc = new XiCP(xiCpProperties.getConnectString(), this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createTermNode(String path, String data){
        this.xc.createTermNode(path, data);
    }

    public void createPermNode(String path, String data){
        this.xc.createPermNode(path, data);
    }

    public void createPermNode(String path, String data, PostProcess postProcess){
        this.xc.createPermNode(path, data);
        nodeChildrenChangedMap.put(path, postProcess);
    }

    public boolean exits(String path){
        return this.xc.exsits(path);
    }

    public void delNode(String path){
        this.xc.delNode(path);
    }

    public String getData(String path){
        return this.xc.getData(path);
    }

    public List<String> getChildren(String path){
        return this.xc.getChildren(path);
    }

    public void closeConnect(){
        this.xc.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (EventType.NodeChildrenChanged.equals(watchedEvent.getEventType())) {
            for (String path : nodeChildrenChangedMap.keySet()) {
                if (watchedEvent.getPath().equalsIgnoreCase(path)) {
                    nodeChildrenChangedMap.get(path).process();
                }
            }
        }
    }

    public interface PostProcess{
        void process();
    }
}
