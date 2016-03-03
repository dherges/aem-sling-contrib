/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.resources.rest;

import org.apache.sling.api.resource.*;

import java.util.Collections;
import java.util.Iterator;

/**
 * A Sling resorce that is baced by a plain java object T ... adaptTo(T)
 */
public class GenericObjectResource<T> extends AbstractResource implements Resource {

    private final ResourceResolver resolver;
    private final String path;
    private final String resourceType;
    private final ValueMap valueMap;
    private final T object;
    private final ResourceMetadata resourceMetadata;

    public GenericObjectResource(ResourceResolver resolver, String path, String resourceType, ValueMap valueMap, T object) {
        this.resolver = resolver;
        this.path = path;
        this.resourceType = resourceType;
        this.object = object;
        this.valueMap = valueMap;

        resourceMetadata = new ResourceMetadata();
        resourceMetadata.setResolutionPath(path);
    }

    /**
     * Returns the full Sling resource path.
     *
     * @return Example: '/sling/resources/rest/abc'
     */
    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return object.getClass().getName();
    }

    @Override
    public Resource getParent() {
        return null;
    }

    @Override
    public Iterator<Resource> listChildren() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterable<Resource> getChildren() {
        return Collections.emptySet();
    }

    @Override
    public Resource getChild(String s) {
        return null;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public String getResourceSuperType() {
        return "";
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public boolean isResourceType(String s) {
        return resourceType.equals(s);
    }

    @Override
    public ResourceMetadata getResourceMetadata() {
        return resourceMetadata;
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return resolver;
    }

    @Override
    public ValueMap getValueMap() {
        return valueMap;
    }

    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (type.isAssignableFrom(object.getClass())) {
            //noinspection unchecked
            return (AdapterType) object;
        }

        return super.adaptTo(type);
    }

    /**
     * Returns the plain java object that this resource holds
     *
     * @return Java object, typed with generic T param
     */
    public T getObject() {
        return object;
    }

}
