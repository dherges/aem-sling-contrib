/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.routing.sinatra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        final Optional<Route.Match> matchingRoute = routeList.stream()
                .map((route) -> {
                    Route.Match match = route.matches(relativePath);
                    if (match.isMatch()) {
                        return match;
                    } else {
                        return null;
                    }
                })
                .filter((match) -> match != null)
                .findFirst();

        return matchingRoute.isPresent() ? matchingRoute.get() : Route.Match.noMatch(inputPath);
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
