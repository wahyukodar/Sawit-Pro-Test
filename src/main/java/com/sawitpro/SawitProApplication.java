package com.sawitpro;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class SawitProApplication {

    public static void main(String[] args) throws Exception {
        int port = 7000;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(OCRServlet.class, "/ocr");
        server.setHandler(servletHandler);
        server.start();
    }

}
