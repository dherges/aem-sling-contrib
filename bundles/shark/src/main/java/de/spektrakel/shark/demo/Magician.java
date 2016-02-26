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

import javax.inject.Inject;

@Resource(baseUri = "/my/magician")
public class Magician {

    @Inject // we could use sling models as dependency injection here
    Object someAlmightyService;

    @Route(uri = ":id:", method = "POST")
    public Object postThisThingAround(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // POST /my/magician/:id: is handled here ...

        return new Response.Builder()
                .status(201)
                .build();
    }

    @Route(uri = ":id:", method = "GET")
    public Object getThatThing(long id, SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // GET /my/magician/:id:

        return someAlmightyService.findById(id);
    }

    @Route(method = "GET")
    public Object getAListOfThings(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // GET /my/magician

        return someAlmightyService.findAllTheThings();
    }

}
