/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.models.resources.impl;

import de.spektrakel.sling.models.resources.demo.TestModel;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.ComponentContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Service
@Properties({
        @Property(name = ResourceProvider.ROOTS, value = "/sling/models")
})
public class ModelResourceProvider implements ResourceProvider {

    ModelPackageBundleListener listener;

    @Activate
    protected void activate(final ComponentContext ctx) {
        this.listener = new ModelPackageBundleListener(ctx.getBundleContext(), this);
    }

    @Deactivate
    protected void deactivate() {
        this.listener.unregisterAll();
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, HttpServletRequest request, String path) {
        return getResource(resourceResolver, path);
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, String path) {
        String relativePath = path.substring("/sling/models".length());

        if (relativePath.startsWith("/my/test/")) { // TODO: relativePath.matches(pattern of TestModel)
            String namePattern = relativePath.substring("/my/test/".length());

            String resourceType = "sling/model/resource"; // TODO: obtain from annotation

            Map<String, Object> props = new HashMap<String, Object>();
            props.put(":name", namePattern);

            return new ModelResource(resourceResolver, path, resourceType, new ValueMapDecorator(props), TestModel.class);
        }

        return null;
    }

    @Override
    public Iterator<Resource> listChildren(Resource parent) {
        return null;
    }

    public static class ModelResource extends AbstractResource implements Resource {

        private final ResourceResolver resolver;
        private final String path;
        private final String resourceType;
        private final ValueMap valueMap;
        private final Class<?> clazz;
        private final ResourceMetadata resourceMetadata;

        public ModelResource(ResourceResolver resolver, String path, String resourceType, ValueMap valueMap, Class<?> clazz) {
            this.resolver = resolver;
            this.path = path;
            this.resourceType = resourceType;
            this.clazz = clazz;
            this.valueMap = valueMap;

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

        public Class<?> getTargetClass() {
            return clazz;
        }

    }
}
