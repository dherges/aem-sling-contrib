/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.shark.impl;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Method;

@SlingServlet(
        resourceTypes = {"shark"},
        methods = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "GENERIC"}
)
public class SharkSlingServlet extends SlingAllMethodsServlet {

    public SharkApp sharkApp = new SharkApp();

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doOptions(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doTrace(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doGeneric(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }


    private void handle(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        Class<?> targetClass = ((SharkResourceProvider.SharkSlingResource) request.getResource()).getTargetClass();

        Object instance = sharkApp.createInstance(request, targetClass);
        Method route = sharkApp.findRoute(instance, request.getRequestURI(), request.getMethod());
        Object result = sharkApp.invokeRoute(route, instance, request, response);

        sharkApp.writeResultToResponse(result, response);
    }

}
