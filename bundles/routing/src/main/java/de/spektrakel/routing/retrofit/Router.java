/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.routing.retrofit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Router {

    private final String basePath;
    private final List<Route> routeList;

    private Router(String basePath, List<Route> routeList) {
        this.basePath = basePath;
        this.routeList = Collections.unmodifiableList(routeList);
    }

    public String basePath() {
        return basePath;
    }

    public List<Route> routeList() {
        return routeList;
    }

    public Route.Match findMatch(String inputPath) {
        final String relativePath = inputPath.startsWith(basePath) ?
                inputPath.substring(basePath.length(), inputPath.length()) :
                inputPath;

        final Route matchingRoute = routeList.stream()
                .filter((route) -> route.matches(relativePath).decision())
                .findFirst()
                .get();

        return matchingRoute.matches(relativePath); // TODO ... this is a bit ugly because we call matches() twice
    }

    public static class Builder {

        private String basePath;
        private List<Route> routes = new ArrayList<>();

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
