/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.shark.impl;

import de.spektrakel.shark.Resource;
import de.spektrakel.shark.Route;

import java.lang.reflect.Method;

public class Dispatcher {

    public void bootstrap() {

        allResourceClasses = collect(classes with @Resource annotation)

        allResourceClasses
            .each(annotatedClass, () {
                slingModels.registerClass(annotatedClass)
            })
    }


    public Object findResource() {

        // find the @Resource class for this request
        Class<?> resourceClass = resourceClasses
                .find((Resource resource) {
                    // "/my/thing" -> Thing.class ... /my/magician -> Magician.class
                    return request.getUri() matches resource.baseUri
                })

        // request.adaptTo(resourceClass), thus using sling models dependency injection
        Object instance = slingModels.createInstance(from=request, adapTo=resourceClass);

        return instance;
    }


    public void findScript(Object instance) {
        // find the @Route method for this request
        Method method = instance.getClass().getDeclaredMethods()
                .filter((Method m) {
                    return m.getDeclaredAnnotationsByType(Route.class) is not empty
                })
                .find((Method m) {
                    // method=GET -> Thing.do() ... method=POST -> Thing.die()
                    return Route annotation matches request.uri() and request.method())
                });

        // invoke the method so that we get a result object
        Object result = method.invoke(instance, request, response);

        // serialize the output to a HTTP response
        writeResultToResponse(result, response);
    }

}
