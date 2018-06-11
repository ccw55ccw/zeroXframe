package com.zerox.starter.server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.ProtectionDomain;

public class EmbedMe {
    public static void main(String[] args) throws Exception {

        int port = 8080;

        Server server = new Server(port);
        server.setStopAtShutdown(true);

        ProtectionDomain protectionDomain = EmbedMe.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();

        String warDir = location.toExternalForm();

        System.out.println("===================="+warDir);

        WebAppContext context = new WebAppContext();
        context.setDescriptor(warDir + "WEB-INF/web.xml");
        context.setConfigurations(new Configuration[] {
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(), new TagLibConfiguration(),
                new PlusConfiguration(), new MetaInfConfiguration(),
                new FragmentConfiguration(), new EnvConfiguration() });

        context.setContextPath("/");
        context.setParentLoaderPriority(true);

        String currentDir = new File(location.getPath()).getParent();
        File workDir = new File(currentDir, "webapp");
        context.setTempDirectory(workDir);

        System.out.println("--------------------"+workDir.getAbsolutePath());

        InputStream webappUrl = ClassLoader.getSystemClassLoader().getResourceAsStream("/webapp/x-demo.war");
        if (webappUrl != null) {
            System.out.println("=========================== is not null");
        } else {
            System.out.println("=============================== null");
        }

        server.setHandler(context);
        server.start();
        server.dump(System.err);
        server.join();
    }
}