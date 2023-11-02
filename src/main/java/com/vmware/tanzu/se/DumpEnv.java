package com.vmware.tanzu.se;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DumpEnv extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<html><body><h1>Environment Variables</h1><table><tr><th>Name</th><th>Value</th></tr>");
        Map<String,String> env = System.getenv();
        for(Map.Entry<?,?> entry : env.entrySet()) {
          response.getWriter().println("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>");
        }
        response.getWriter().println("</table><h1>Java System Properties</h1><table><tr><th>Name</th><th>Value</th></tr>");
        Properties systemProps = System.getProperties();
        for(Map.Entry<Object,Object> entry : systemProps.entrySet()) {
          response.getWriter().println("<tr><td>" + entry.getKey().toString() + "</td><td>" + entry.getValue().toString() + "</td></tr>");
        }
        response.getWriter().println("</table></body></html>");
    }

    public static void main(String[] args) throws Exception
    {
        String portEnvVar = System.getenv("PORT");
        int port = 8080;
        if(portEnvVar != null) {
          port = Integer.parseInt(portEnvVar);
        } 
        Server server = new Server(port);
        server.setHandler(new DumpEnv());

        server.start();
        server.join();
    }
}