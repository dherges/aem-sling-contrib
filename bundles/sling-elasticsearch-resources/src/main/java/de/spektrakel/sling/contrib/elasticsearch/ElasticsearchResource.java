/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.contrib.elasticsearch;

import org.apache.sling.api.resource.AbstractResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Iterator;

public class ElasticsearchResource implements Resource {

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Resource getParent() {
        return null;
    }

    @Override
    public Iterator<Resource> listChildren() {
        return null;
    }

    @Override
    public Iterable<Resource> getChildren() {
        return null;
    }

    @Override
    public Resource getChild(String s) {
        return null;
    }

    @Override
    public String getResourceType() {
        return null;
    }

    @Override
    public String getResourceSuperType() {
        return null;
    }

    @Override
    public boolean isResourceType(String s) {
        return false;
    }

    @Override
    public ResourceMetadata getResourceMetadata() {
        return null;
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return null;
    }

    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> adapterTypeClass) {
        return null;
    }
}
