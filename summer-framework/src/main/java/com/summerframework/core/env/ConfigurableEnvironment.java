package com.summerframework.core.env;

import com.summerframework.core.io.ClassPathResource;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;

import java.net.URL;
import java.util.*;

/**
 * @description: ConfigurableEnvironment
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ConfigurableEnvironment implements Environment {

    private static final LogFactory logger = new LogFactory(ConfigurableEnvironment.class);


    public ConfigurableEnvironment(){
    }

    public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";

    private final Set<Properties> allProperties = new LinkedHashSet<>();

    private Properties defaultProperties;

    private Map<?, ?> props = new HashMap<>();

    public Map<?, ?> getProps() {
        return props;
    }

    public void setProps(Map<?, ?> props) {
        this.props = props;
    }

    public Set<Properties> getAllProperties() {
        return allProperties;
    }

    public void addProperties(Properties properties) {
        this.allProperties.add(properties);
    }

    public Properties getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public void initEnvironment(String... propLocal) {
        try {
            //if (path == null || path.length < 1) {
            //    path = new String[]{ClassPathResource.defaultPropertiesPath};
            //}
            //Resource resource = new ClassPathResource(path[0], this.getClass().getClassLoader());
            //Properties config = new Properties();
            //config.load(resource.getInputStream());
            //setDefaultProperties(config);
            //addProperties(config);
            //System.out.println("defaultProperties >>> " + defaultProperties);

            if (defaultProperties == null) {
                defaultProperties = new Properties();
                defaultProperties.putAll(loadAllProperties(propLocal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadAllProperties(String... propLocal){
        Properties defaultProp = new Properties();
        try {
            Enumeration<URL> urlEnumeration = Thread.currentThread()
                    .getContextClassLoader().getResources(ClassPathResource.PROPERTIES_NAME);
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                //System.out.println("loadAllProperties -> " + url.getPath());
                defaultProp.putAll(loadProperties(url));
                break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return defaultProp;
    }

    public static void main(String[] args) {
        System.out.println(loadAllProperties(args));;
    }

    public boolean contentKey(String key){
        boolean content = false;
        if (defaultProperties == null) {
            return false;
        }
        for (Object keys : defaultProperties.keySet()) {
            if (((String)keys).contains(key)) {
                content = true;
            }
        }
        return content;
    }

    public static Properties loadProperties(URL url) {
        Properties properties = new Properties();
        try {
            properties.load(url.openStream());
        } catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    @Override
    public String getProperty(String key) {
        if (defaultProperties == null) {
            initEnvironment();
        }
        return defaultProperties.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (defaultProperties == null) {
            return defaultValue;
        }
        String value = defaultProperties.getProperty(key);
        return StringUtils.isEmpty(value)? defaultValue : value;
    }
}
