/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.shark.demo;

import de.spektrakel.shark.Resource;
import de.spektrakel.shark.Route;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

@Resource(baseUri = "/my/thing")
public class Thing {

    @Route(method = "GET")
    public Object dor(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        return "Hello GET!";
    }

    @Route(method = "POST")
    public Object die(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        return "Bye bye, POST!";
    }

}
