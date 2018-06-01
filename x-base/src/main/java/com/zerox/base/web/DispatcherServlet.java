package com.zerox.base.web;

import cn.hutool.json.JSONUtil;
import com.zerox.base.bean.BeanContextContainer;
import com.zerox.base.bean.BeanObject;
import com.zerox.base.common.XLogger;
import com.zerox.base.context.ApplicationContext;
import com.zerox.base.helper.ActionHelper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(urlPatterns = "/", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) {

        new ApplicationContext(null);

        ServletContext sc = config.getServletContext();
        ServletRegistration jspServlet = sc.getServletRegistration("jsp");
        jspServlet.addMapping("/jsp/*");
        ServletRegistration defaultServlet = sc.getServletRegistration("default");
        defaultServlet.addMapping("/asset/*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        XLogger.debug(req.getRequestURI());
        String path = req.getRequestURI();
        String methodStr = req.getMethod().toUpperCase();
        Request request = new Request(methodStr, path);
        Handler handler = ActionHelper.requestHandlerMap.get(request);
        if (handler != null) {
            BeanObject controller = BeanContextContainer.getContainer().get(handler.getControllerClass().getName());
            if (controller != null) {
                Method method = handler.getHandleMethod();
                Object result = null;
                try {
                    String params = req.getParameterValues("params")[0];
                    result = method.invoke(controller.getBeanObj(), params);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    XLogger.error("dispatcher invoke method error {}", e);
                }
                if (result != null) {
                    if (result instanceof ModelAndView) {
                        handleJsonResp(resp, result);
                    } else {
                        handleJsonResp(resp, result);
                    }
                } else {
                    handleErrorJspResp(req, resp);
                }
            } else {
                handleErrorJspResp(req, resp);
            }
        } else {
            handleErrorJspResp(req, resp);
        }

    }

    private void handleJsonResp(HttpServletResponse response, Object result){
        String jsonRes = JSONUtil.toJsonStr(result);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(jsonRes);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            XLogger.error("write json response error {}", e);
        }

    }

    private void handleJspResp(HttpServletRequest req, HttpServletResponse resp, String result){
        try {
            req.getRequestDispatcher("/jsp/"+result+".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            XLogger.error("dispatcher handle jsp error {}", e);
        }
    }

    private void handleErrorJspResp(HttpServletRequest req, HttpServletResponse resp){
        handleJspResp(req, resp, "error");
    }
}
