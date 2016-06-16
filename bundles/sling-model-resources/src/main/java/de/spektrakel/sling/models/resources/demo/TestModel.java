/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.models.resources.demo;

import de.spektrakel.sling.models.resources.ModelResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;

@Model(adaptables = Resource.class)
@ModelResource(path = "my/test/:name")
public class TestModel {

    @Inject
    @Self
    Resource resource;

    public String getName() {
        return resource.getValueMap().get(":name", "");
    }

    public void postToElastic() {
        System.out.println("yeah ... POST /elasticsearch/document/:name:");
    }

}
