package com.summerframework.boot.web.webmvc;

import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: BootHttpRequestHandlerAdapter
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultHandlerAdapter implements BootHandlerAdapter {


    private static final LogFactory logger = new LogFactory(DefaultHandlerAdapter.class);

    public DefaultHandlerAdapter() {
        init();
    }

    private void init() {
        nullMap.put("处理结果", "已处理");
        errorMap.put("处理结果", "发生错误");
        errorMap.put("建议", "稍后再试或联系管理员");
        exceptionMap.put("处理结果", "发生异常");
        exceptionMap.put("建议", "稍后再试或联系管理员");
    }

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof BootHandlerMapping);
    }

    @Override
    public ModelAndView handle(HttpServerRequest request, HttpServerResponse response, Object handler) throws Exception {
        return invokeHandlerMethod(request, response, (BootHandlerMapping) handler);
    }

    protected ModelAndView invokeHandlerMethod(HttpServerRequest request,
                                               HttpServerResponse response, BootHandlerMapping handlerMapping) {
        try {
            Object handleResult = handlerMapping.getHandlerMapping()
                    .invoke(handlerMapping.getController(),
                            getParamValues(request, response, handlerMapping));
            if (handleResult == null || handleResult instanceof Void) {
                return defaultNull;
            } else if (handlerMapping.getHandlerMapping().getReturnType() == ModelAndView.class) {
                return (ModelAndView) handleResult;
            } else {
                defaultNull.getModel().clear();
                defaultNull.getModel().put("处理结果", handleResult.toString());
                return defaultNull;
            }
        } catch (Throwable e) {
            String ex = e.getMessage();
            logger.info(ex);
            e.printStackTrace();
            defaultException.getModel().put("异常信息", ex);
            return defaultException;
        }
    }

    protected Object[] getParamValues(HttpServerRequest request,
                                      HttpServerResponse response, BootHandlerMapping handlerMapping) {
        Class<?>[] paramsTypes = handlerMapping.getHandlerMapping().getParameterTypes();
        logger.info("paramsTypes.length = [" + paramsTypes.length + "]");
        return this.getParameterValues(request, response, paramsTypes,
                getParameterIndexMappings(handlerMapping, paramsTypes));
    }

    private Map<String, Integer> getParameterIndexMappings(BootHandlerMapping handlerMapping, Class<?>[] paramsTypes){
        Map<String, Integer> paramIndex = new HashMap<>();
        this.getHandlerMappingParamIndex(handlerMapping, paramIndex);
        this.getHandlerMappingServletParamIndex(paramsTypes, paramIndex);
        return paramIndex;
    }

    private void getHandlerMappingServletParamIndex(Class<?>[] paramsTypes, Map<String, Integer> paramIndex) {
        for (int i = 0; i < paramsTypes.length; i++) {
            Class<?> paramsType = paramsTypes[i];
            if (paramsType == HttpServerRequest.class || paramsType == HttpServerResponse.class) {
                paramIndex.put(paramsType.getName(), i);
            }
        }
    }


    private void getHandlerMappingParamIndex(BootHandlerMapping handlerMapping, Map<String, Integer> paramIndex) {
        Annotation[][] parameterMatrix = handlerMapping.getHandlerMapping().getParameterAnnotations();
        for (int i = 0; i < parameterMatrix.length; i++) {
            for (Annotation annotation : parameterMatrix[i]) {
                if (AnnotationConfigUtils.isContentAnnotation(annotation, AnnotationConfigUtils.PARAM_ANNOTATIONS)) {
                    String paramName = AnnotationConfigUtils.getParamAnnotationValue(annotation);
                    if (null != paramName && !StringUtils.NONE_SPACE.equals(paramName)) {
                        paramIndex.put(paramName.trim(), i);
                    }
                }
            }
        }
    }

    private Object[] getParameterValues(HttpServerRequest request,
                                        HttpServerResponse response,
                                        Class<?>[] paramsTypes, Map<String, Integer> paramIndex){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Object[] paramValues = new Object[paramsTypes.length];
        for (Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
            logger.info("parameter.getKey() - [" + parameter.getKey() + "], parameter.getValue() - [" + parameter.getValue() + "]");
            if (!paramIndex.containsKey(parameter.getKey())) {
                continue;
            }
            if (paramIndex.containsKey(HttpServerRequest.class.getName())) {
                int reqIndex = paramIndex.get(HttpServerRequest.class.getName());
                paramValues[reqIndex] = request;
            }
            if (paramIndex.containsKey(HttpServerResponse.class.getName())) {
                int respIndex = paramIndex.get(HttpServerResponse.class.getName());
                paramValues[respIndex] = response;
            }
            int index = paramIndex.get(parameter.getKey());
            //logger.info("index = [" + index + "]; value = [" + parameter.getValue() + "]");
            String value =
                    String.valueOf(parameter.getValue())
                            .replaceAll("\\[|\\]", StringUtils.NONE_SPACE).replaceAll("\\s", StringUtils.COMMA)
                    /*Arrays.toString()*/;
            //logger.info("index = [" + index + "]; value = [" + value + "]");
            paramValues[index] = parameterConversion(value, paramsTypes[index]);
        }
        return paramValues;
    }

    /**
     * Parameter conversion
     *
     * @param value
     * @param paramsType
     * @return
     */
    private Object parameterConversion(String value, Class<?> paramsType) {
        logger.info("parameterConversion paramsType = [" + paramsType + "]");
        if (String.class == paramsType) {
            return value;
        } else if (Integer.class == paramsType) {
            return Integer.valueOf(value);
        } else if (Double.class == paramsType) {
            return Double.valueOf(value);
        } else {
            return value;
        }
    }
}
