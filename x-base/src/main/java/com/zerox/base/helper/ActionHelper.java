package com.zerox.base.helper;

import com.zerox.base.annotation.RequestMapping;
import com.zerox.base.web.Handler;
import com.zerox.base.web.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionHelper {

    public static final Map<Request, Handler> requestHandlerMap = new HashMap<>();

    static {
        initMap();
    }

    private static void initMap(){
        List<Class> actionClassList = ClassHelper.actionClassList;
        for (Class aClass : actionClassList) {
            String actionPath = null;
            String httpMethod = null;
            if (aClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping actionReq = (RequestMapping) aClass.getAnnotation(RequestMapping.class);
                if (actionReq != null) {
                    actionPath = actionReq.value();
                    httpMethod = actionReq.method().name();
                }
            }
            for (Method method : aClass.getMethods()) {
                Request request = new Request();
                request.setRequestMethod(httpMethod);
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodReq = method.getAnnotation(RequestMapping.class);
                    if (methodReq != null) {
                        if (methodReq.method() != null) {
                            request.setRequestMethod(methodReq.method().name());
                        }
                        if (actionPath != null) {
                            request.setRequestPath(actionPath + methodReq.value());
                        }
                    }
                }

                Handler handler = new Handler();
                handler.setControllerClass(aClass);
                handler.setHandleMethod(method);

                requestHandlerMap.put(request, handler);
            }

        }
    }
}
