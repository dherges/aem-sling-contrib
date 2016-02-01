/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.email.impl;

import de.spektrakel.sling.email.DataSourceSlingResolver;
import de.spektrakel.sling.email.SlingResourceDataSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.resolver.DataSourceBaseResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;

import javax.activation.DataSource;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@Model(adaptables = ResourceResolver.class, adapters = DataSourceSlingResolver.class)
public class DataSourceSlingResolverImpl extends DataSourceBaseResolver implements DataSourceSlingResolver {
    private static final String JCR_MIMETYPE = "jcr:mimeType";

    protected final ResourceResolver resourceResolver;

    @Inject
    @Source("osgi-services")
    protected MimeTypeService mimeTypeService;

    @Inject
    public DataSourceSlingResolverImpl(ResourceResolver resourceResolver) {
        super();

        this.resourceResolver = resourceResolver;
    }

    public DataSourceSlingResolverImpl(ResourceResolver resourceResolver, boolean isLenient) {
        super(isLenient);

        this.resourceResolver = resourceResolver;
    }

    @Override
    public DataSource resolve(String url) throws IOException {
        return resolve(url, isLenient());
    }

    @Override
    public DataSource resolve(String url, boolean isLenient) throws IOException {

        try {
            if (isSlingUrl(url)) {
                String path = url.substring(PREFIX.length());

                Resource resource = resourceResolver.getResource(path);
                if (resource == null) {
                    throw new ResourceNotFoundException("Resource not found, path=" + path);
                }

                InputStream inputStream = resource.adaptTo(InputStream.class);
                if (inputStream == null) {
                    throw new ResourceNotFoundException("Cannot adapt resource to input stream, path=" + path);
                }

                String name = resource.getName();

                String mimeType = resource.getValueMap().get(JCR_MIMETYPE, String.class);
                if (StringUtils.isBlank(mimeType) && mimeTypeService != null) {
                    mimeType = mimeTypeService.getMimeType(FilenameUtils.getExtension(name));
                }

                return new SlingResourceDataSource(resource, name, mimeType);
            }

        } catch (Exception e) {
            if (isLenient) {
                return null;
            } else {
                throw new IOException(e);
            }
        }

        return null;
    }

    @Override
    public boolean isSlingUrl(String url) {
        return url.startsWith(PREFIX);
    }

    @Override
    public String toSlingUrl(String path) {
        return PREFIX.concat(path);
    }

}
