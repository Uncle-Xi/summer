package com.summerframework.boot.starter.loader;

import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.logger.PrintColor;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @description: JarLauncher
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class JarLauncher {

    private static final LogFactory logger = new LogFactory(JarLauncher.class);
    private static final String JAR_LAUNCHER = "com.summerframework.boot.starter.loader.JarLauncher";
    private static final String MAIN = "main";

    public static void main(String[] args) throws Exception {
        findMainClassAndRunIt(args);
    }

    private static void findMainClassAndRunIt(String[] args) throws Exception {
        PrintColor.setIsJar(true);
        String[] param = new String[]{};
        String clazz = null;
        if (args != null && args.length > 0) {
            clazz = args[0];
        } else {
            Enumeration<URL> urlEnumeration =
                    Thread.currentThread().getContextClassLoader().
                            getResources("META-INF/MANIFEST.MF");
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                if (url == null) { continue; }
                logger.info(url.getFile());
                Properties properties = ConfigurableEnvironment.loadProperties(url);
                clazz = properties.getProperty("Main-Class");
            }
        }
        if (clazz != null && !clazz.equals(JAR_LAUNCHER)) {
            Class<?> mainClass = Class.forName(clazz);
            Method[] methods = mainClass.getMethods();
            for(Method main : methods){
                if (MAIN.equals(main.getName())) {
                    logger.info("JarLauncher is started.");
                    main.invoke(null, (Object)param);
                    return;
                }
            }
        }
        logger.info("Nothing to do ...");
    }
}
