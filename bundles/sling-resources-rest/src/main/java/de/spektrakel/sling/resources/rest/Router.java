/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.resources.rest;

import java.util.Collections;
import java.util.List;

public class Router {

    private final String basePath;
    private final List<Route> routeList;

    private Router(String basePath, List<Route> routeList) {
        this.basePath = basePath;
        this.routeList = Collections.unmodifiableList(routeList);
    }

    public String getBasePath() {
        return basePath;
    }

    public List<Route> getRouteList() {
        return routeList;
    }



    public static class Builder {

        private String basePath;
        private List<Route> routes;

        public Builder basePath(String basePath) {
            this.basePath = basePath;

            return this;
        }

        public Builder addRoute(Route route) {
            routes.add(route);

            return this;
        }

        public Router build() {
            return new Router(basePath, routes);
        }
    }

}
