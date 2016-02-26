/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.shark.impl;

import de.spektrakel.shark.demo.Magician;
import de.spektrakel.shark.demo.Thing;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SharkApp {


    public Class<?> findResource(String path) {


        Class<?> resourceClass = null;
        if (path.contains("/my/thing")) {
            resourceClass = Thing.class;
        } else if (path.contains("/my/magician")) {
            resourceClass = Magician.class;
        }

        return resourceClass;

        /** TODO
        // find the @Resource class for this request
        Class<?> resourceClass = resourceClasses
                .find((Resource resource) {
            // "/my/thing" -> Thing.class ... /my/magician -> Magician.class
            return request.getUri() matches resource.baseUri
        })

        // request.adaptTo(resourceClass), thus using sling models dependency injection
        Object instance = slingModels.createInstance(from=request, adapTo=resourceClass);
        **/
    }

    public Method findRoute(Object object, String uri, String method) {

        try {
            if ("GET".equals(method)) {
                    return Thing.class.getMethod("dor", SlingHttpServletRequest.class, SlingHttpServletResponse.class);
            } else if ("POST".equals(method)) {
                return Thing.class.getMethod("die", SlingHttpServletRequest.class, SlingHttpServletResponse.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;

        /** TODO
        // find the @Route method for this request
        Method method = instance.getClass().getDeclaredMethods()
                .filter((Method m) {
            return m.getDeclaredAnnotationsByType(Route.class) is not empty
        })
        .find((Method m) {
            // method=GET -> Thing.do() ... method=POST -> Thing.die()
            return Route annotation matches request.uri() and request.method())
        })
        **/
    }

    public Object createInstance(SlingHttpServletRequest request, Class<?> targetClass) {
        // here, Sling Models dependency injection comes to life
        Object slingModelObject = request.getResource().adaptTo(targetClass);

        if (slingModelObject == null) { // TODO ... hacky fallback here...
            try {
                slingModelObject = targetClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return slingModelObject;
    }

    public Object invokeRoute(Method route, Object instance, SlingHttpServletRequest request, SlingHttpServletResponse response) {

        // invoke the method so that we get a result object
        try {
            return route.invoke(instance, request, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void writeResultToResponse(Object result, SlingHttpServletResponse response) {

        try {
            response.getWriter().append(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
