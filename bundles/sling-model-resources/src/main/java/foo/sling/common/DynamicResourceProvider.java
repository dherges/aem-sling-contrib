package foo.sling.common;

import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class DynamicResourceProvider implements ResourceProvider {

    /** @return true, if the given <code>path</code> is handled by this resource provider */
    protected abstract boolean valid(String path);

    /** @return the resurce at the given path */
    protected abstract ResourceData resourceAt(String path);

    /** @return the common resource type of this resource provider */
    protected abstract String resourceType();

    @Override
    public Resource getResource(ResourceResolver resourceResolver, HttpServletRequest request, String path) {
        return getResource(resourceResolver, path);
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, String path) {
        if (valid(path)) {

            // Note that ResourceMetadata is NOT the data that populates a resources ValueMap; that is done above.
            ResourceData resourceData = resourceAt(path);
            resourceData.metadata.setResolutionPath(path);

            // Create the dynamic resource
            Resource dynamicResource = new DynamicResource(resourceResolver, resourceData.metadata,
                    resourceData.resourceType, resourceType());

            // Properties is the set of data that populates the resources ValueMap (properties)
            return new PropertyResourceWrapper(dynamicResource, resourceData.properties);
        }

        return null;
    }

    @Override
    public Iterator<Resource> listChildren(Resource parent) {
        return null;
    }

    public static class ResourceData {
        public final String resourceType;
        public final Map<String, Object> properties;
        public final ResourceMetadata metadata;

        public ResourceData(String resourceType) {
            this(resourceType, new HashMap<>());
        }

        public ResourceData(String resourceType, Map<String, Object> properties) {
            this(resourceType, properties, new ResourceMetadata());
        }

        public ResourceData(String resourceType, Map<String, Object> properties, ResourceMetadata metadata) {
            this.resourceType = resourceType;
            this.properties = properties;
            this.metadata = metadata;
        }
    }

    public static class PropertyResourceWrapper extends ResourceWrapper {
        private final ValueMap properties;

        public PropertyResourceWrapper(Resource resource, Map<String, Object> properties) {
            super(resource);
            this.properties = new ValueMapDecorator(properties);
        }

        @Override
        public final <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
            if (type != ValueMap.class) {
                return super.adaptTo(type);
            }

            // Return the ValueMap of the properties passed in
            return (AdapterType) this.properties;
        }
    }

    public static class DynamicResource extends SyntheticResource {
        private final String resourceSuperType;

        public DynamicResource(ResourceResolver resourceResolver, ResourceMetadata resourceMetadata,
                               String resourceType, String resourceSuperType) {
            super(resourceResolver, resourceMetadata, resourceType);
            this.resourceSuperType = resourceSuperType;
        }

        @Override
        public String getResourceSuperType() {
            return resourceSuperType;
        }

        @Override
        public final <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
            return super.adaptTo(type);
        }
    }

}
