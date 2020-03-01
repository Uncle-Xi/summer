package com.summerframework.web.webmvc.servlet.mvc;

import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.webmvc.servlet.HandlerAdapter;
import com.summerframework.web.webmvc.servlet.HandlerMapping;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: DefaultServletHttpRequestHandler
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpRequestHandlerAdapter implements HandlerAdapter {


    public HttpRequestHandlerAdapter() {
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
        return (handler instanceof HandlerMapping);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return invokeHandlerMethod(request, response, (HandlerMapping) handler);
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                               HttpServletResponse response, HandlerMapping handlerMapping) {
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
            System.out.println(ex);
            e.printStackTrace();
            defaultException.getModel().put("异常信息", ex);
            return defaultException;
        }
    }

    protected Object[] getParamValues(HttpServletRequest request,
                                      HttpServletResponse response, HandlerMapping handlerMapping) {
        Class<?>[] paramsTypes = handlerMapping.getHandlerMapping().getParameterTypes();
        System.out.printf("paramsTypes.length = [%s][%s] >>>>> \n", paramsTypes.length, paramsTypes);
        return this.getParameterValues(request, response, paramsTypes,
                getParameterIndexMappings(handlerMapping, paramsTypes));
    }

    private Map<String, Integer> getParameterIndexMappings(HandlerMapping handlerMapping, Class<?>[] paramsTypes){
        Map<String, Integer> paramIndex = new HashMap<>();
        this.getHandlerMappingParamIndex(handlerMapping, paramIndex);
        this.getHandlerMappingServletParamIndex(paramsTypes, paramIndex);
        return paramIndex;
    }

    private void getHandlerMappingServletParamIndex(Class<?>[] paramsTypes, Map<String, Integer> paramIndex) {
        for (int i = 0; i < paramsTypes.length; i++) {
            Class<?> paramsType = paramsTypes[i];
            if (paramsType == HttpServletRequest.class || paramsType == HttpServletResponse.class) {
                paramIndex.put(paramsType.getName(), i);
            }
        }
    }


    private void getHandlerMappingParamIndex(HandlerMapping handlerMapping, Map<String, Integer> paramIndex) {
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

    private Object[] getParameterValues(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Class<?>[] paramsTypes, Map<String, Integer> paramIndex){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Object[] paramValues = new Object[paramsTypes.length];
        for (Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
            if (!paramIndex.containsKey(parameter.getKey())) {
                continue;
            }
            if (paramIndex.containsKey(HttpServletRequest.class.getName())) {
                int reqIndex = paramIndex.get(HttpServletRequest.class.getName());
                paramValues[reqIndex] = request;
            }
            if (paramIndex.containsKey(HttpServletResponse.class.getName())) {
                int respIndex = paramIndex.get(HttpServletResponse.class.getName());
                paramValues[respIndex] = response;
            }
            int index = paramIndex.get(parameter.getKey());
            String value = Arrays.toString(parameter.getValue())
                    .replaceAll("\\[|\\]", StringUtils.NONE_SPACE)
                    .replaceAll("\\s", StringUtils.COMMA);
            System.out.printf("index = [%s]; value = [%s].\n", index, value);
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
        System.out.printf("parameterConversion paramsType = [%s].\n", paramsType);
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
