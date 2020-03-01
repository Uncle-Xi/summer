package com.summerframework.boot;

import com.summerframework.beans.BeanUtils;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.boot.util.TransferFile;
import com.summerframework.boot.web.ServerSelector;
import com.summerframework.boot.web.server.HttpServer;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.context.support.AbstractApplicationContext;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.logger.PrintColor;
import com.summerframework.core.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.summerframework.boot.web.server.HttpServer.DEFAULT_PORT;

/**
 * @description: SummerBootApplication
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class SummerApplication {

    private static final LogFactory logger = new LogFactory(SummerApplication.class);

    public static final String DEFAULT_CONTEXT_CLASS = "com.summerframework.boot.autoconfigure." +
            "AnnotationAutoConfigureApplicationContext";

    private ConfigurableEnvironment environment;
    private Class<? extends ConfigurableApplicationContext> applicationContextClass;
    private Class<?> mainApplicationClass;
    private WebApplicationType webApplicationType;

    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        return run(new Class<?>[] { primarySource }, args);
    }

    public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
        return new SummerApplication(primarySources).run(args);
    }

    public SummerApplication(Class<?>... primarySources) {
        Assert.notNull(primarySources, "PrimarySources must not be null");
        this.webApplicationType = WebApplicationType.deduceFromClasspath();
        this.mainApplicationClass = primarySources == null? null : primarySources.length > 0? primarySources[0] : null;
    }

    public ConfigurableApplicationContext run(String[] args) {
        ConfigurableApplicationContext context;
        try {
            context = createApplicationContext();
            environment = prepareEnvironment(context);
            String banner = environment.getProperty("banner.location", "banner.txt");
            prepareContext(context, environment, banner);
            refreshContext(context);
            afterRefresh(context);
        } catch (Throwable ex) {
            throw new IllegalStateException(ex);
        }
        return context;
    }

    private ConfigurableEnvironment prepareEnvironment(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = getOrCreateEnvironment();
        environment.initEnvironment();
        return environment;
    }

    private ConfigurableEnvironment getOrCreateEnvironment() {
        if (this.environment != null) {
            return this.environment;
        }
        switch (this.webApplicationType) {
            case SERVLET:
                return new ConfigurableEnvironment();
        }
        return null;
    }

    private void printBanner(String banner){
        System.out.println(PrintColor.getRed()
                + "\n..........................................................Welcome Use Summer..........................................................."
                + PrintColor.getNone());
        try {
            File tempFile = TransferFile.getTransferFile(new File(banner), banner);
            FileReader fileReader = new FileReader(tempFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(PrintColor.getGreen() + new String(line.getBytes(), "UTF-8") + PrintColor.getNone());
            }
            bufferedReader.close();
            fileReader.close();
            TransferFile.deleteFile(tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareContext(ConfigurableApplicationContext context,
                                ConfigurableEnvironment environment, String banner) {
        context.setEnvironment(environment);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        printBanner(banner);
        startWebServer(context);
        String basePackage = mainApplicationClass.getName();
        basePackage = basePackage.substring(0, basePackage.lastIndexOf("."));
        load(context, new Object[]{basePackage});
        logger.info("prepareContext done.");
        logger.info("PID = " + LogFactory.getPid());
    }

    protected void load(ApplicationContext context, Object[] sources) {
        String mainClass = environment.getProperty("mainApplicationClass", sources[0].toString());
        BeanDefinitionLoader loader =
                createBeanDefinitionLoader(getBeanDefinitionRegistry(context), new Object[]{mainClass});
        loader.load();
    }

    protected BeanDefinitionLoader createBeanDefinitionLoader(
            BeanDefinitionRegistry registry, Object[] sources) {
        return new BeanDefinitionLoader(registry, environment, sources);
    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context) {
        if (context instanceof BeanDefinitionRegistry) {
            return (BeanDefinitionRegistry) context;
        }
        if (context instanceof AbstractApplicationContext) {
            return (BeanDefinitionRegistry) ((AbstractApplicationContext) context)
                    .getBeanFactory();
        }
        throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
    }

    private void startWebServer(ConfigurableApplicationContext context){
        String server = context.getEnvironment().getProperty("summer.boot.server");
        String port = context.getEnvironment().getProperty("summer.boot.port");
        //System.out.println("[startWebServer] [port] -> " + port);
        HttpServer httpServer = ServerSelector.getServer(server, port == null? null : port);
        httpServer.setContext(context);
        httpServer.listening();
    }

    protected void afterRefresh(ConfigurableApplicationContext context) {
        logger.info("SummerApplication started...");
        //openBrowser();
    }

    protected void openBrowser(){
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Runtime.getRuntime().exec("cmd /c start http://localhost:" + DEFAULT_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String location = "banner.txt";
        new SummerApplication().printBanner(location);
    }

    protected ConfigurableApplicationContext createApplicationContext() {
        Class<?> contextClass = this.applicationContextClass;
        if (contextClass == null) {
            try {
                switch (this.webApplicationType) {
                    case SERVLET:
                        contextClass = Class.forName(DEFAULT_CONTEXT_CLASS);
                        break;
                }
            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException(
                        "Unable create a default ApplicationContext, " + "please specify an ApplicationContextClass", ex);
            }
        }
        return (ConfigurableApplicationContext) BeanUtils.instantiateClass(contextClass);
    }

    private void refreshContext(ConfigurableApplicationContext context) {
        refresh(context);
        logger.info("refreshContext done...");
    }

    protected void refresh(ApplicationContext applicationContext) {
        Assert.isInstanceOf(AbstractApplicationContext.class, applicationContext);
        ((AbstractApplicationContext) applicationContext).refresh();
    }
}
