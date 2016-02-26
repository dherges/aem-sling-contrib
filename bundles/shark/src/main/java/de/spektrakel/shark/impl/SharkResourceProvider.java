/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.shark.impl;


import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

@Component
@Service
@Properties({
        @Property(name = ResourceProvider.ROOTS, value = "/shark/app")
})
public class SharkResourceProvider implements ResourceProvider {

    public SharkApp sharkApp = new SharkApp();

    @Override
    public Resource getResource(ResourceResolver resourceResolver, HttpServletRequest httpServletRequest, String path) {
        return getResource(resourceResolver, path);
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, String path) {
        Class<?> clazz = sharkApp.findResource(path);

        return new SharkSlingResource(resourceResolver, path, clazz);
    }

    @Override
    public Iterator<Resource> listChildren(Resource resource) {
        return null;
    }


    public static class SharkSlingResource extends AbstractResource implements Resource {

        private final ResourceResolver resolver;
        private final String path;
        private final Class<?> clazz;
        private final ResourceMetadata resourceMetadata;

        public SharkSlingResource(ResourceResolver resolver, String path, Class<?> clazz) {
            this.resolver = resolver;
            this.path = path;
            this.clazz = clazz;

            resourceMetadata = new ResourceMetadata();
            resourceMetadata.setResolutionPath(path);
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public String getName() {
            return clazz.getName();
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
            return "shark";
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
            return "shark".equals(s);
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
            return new ValueMapDecorator(new HashMap<>());
        }

        public Class<?> getTargetClass() {
            return clazz;
        }

    }

}
