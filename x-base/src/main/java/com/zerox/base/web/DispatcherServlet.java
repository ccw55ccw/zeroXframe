package com.zerox.base.web;

import com.zerox.base.common.XLogger;
import com.zerox.base.helper.ActionHelper;
import com.zerox.base.helper.InitHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

        InitHelper.init();

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
        String method = req.getMethod().toUpperCase();
        Request request = new Request(method, path);
        Handler handler = ActionHelper.requestHandlerMap.get(request);
        System.out.println(handler);
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);

    }
}
