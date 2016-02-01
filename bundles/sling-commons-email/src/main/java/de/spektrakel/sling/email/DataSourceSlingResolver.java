/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.email;

import org.apache.commons.mail.DataSourceResolver;

/**
 * A DataSourceResolver that allows reading content from Sling repository.
 *
 * Usage: <code>resourceResolver.adaptTo(DataSourceSlingResolver.class)</code>
 *
 * @link https://commons.apache.org/proper/commons-email/userguide.html
 */
public interface DataSourceSlingResolver extends DataSourceResolver {

    String PREFIX = "sling://";

    /**
     * Returns true, if <code>url</code> start with "sling://"
     */
    boolean isSlingUrl(String url);

    /**
     * Converts the given resource <code>path</code> to a "sling://" URL
     */
    String toSlingUrl(String path);

}
