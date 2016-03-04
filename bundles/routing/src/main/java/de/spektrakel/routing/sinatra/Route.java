/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.routing.sinatra;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Route {

    private final String path;
    private final String[] pathSegments;
    private final String[] pathParams;

    public Route(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must start with a slash!");
        }
        if (path.endsWith("/")) {
            throw new IllegalArgumentException("Path must not end with a slash!");
        }
        this.path = path;

        pathSegments = splitSegments(path);
        pathParams = findParams(pathSegments);
    }

    public String getPath() {
        return path;
    }

    public String[] getPathSegments() {
        return pathSegments;
    }

    public String[] getPathParams() {
        return pathParams;
    }

    public boolean hasPathParam(String name) {
        return Arrays.stream(pathParams)
                .anyMatch((part) -> part.equals(name));
    }

    public Match matches(String inputPath) {
        final String[] inputSegments = splitSegments(inputPath);

        final Map<String, String> params = new HashMap<>();
        final int decision = (inputSegments.length == pathSegments.length) ?
                IntStream.range(0, inputSegments.length)
                    .map((idx) -> {
                        String is = inputSegments[idx];
                        String ps = pathSegments[idx];

                        if (isParam(ps)) {
                            params.put(ps, is);

                            return 0; // 0 = segment matches
                        } else {
                            return ps.equals(is) ? 0 : 1; // 0 = segment matches; 1 = no match;
                        }
                    })
                    .sum()
                : 2; // 2 = array lengths don't match

        return new Match(inputPath, this, params, decision == 0);
    }


    public static class Match {

        private final String input;
        private final Route route;
        private final Map<String, String> params;
        private final boolean decision;

        private Match(String input, Route route, Map<String, String> params, boolean decision) {
            this.input = input;
            this.route = route;
            this.params = params;
            this.decision = decision;
        }

        public String input() {
            return input;
        }

        public Route route() {
            return route;
        }

        public String param(String name) {
            return params.get(name);
        }

        public Set<String> paramNames() {
            return params.keySet();
        }

        public boolean decision() {
            return decision;
        }
    }

    /** Splits into path segments */
    private static String[] splitSegments(String path) {
        return Arrays.stream(path.split("/"))
                .filter(Route::notEmpty)
                .toArray(String[]::new);
    }

    /** Strips a trailing slash, ensures a starting slash */
    private static String[] findParams(String[] parts) {
        return Arrays.stream(parts)
                .filter(Route::isParam)
                .toArray(String[]::new);
    }

    private static boolean notEmpty(String part) {
        return part != null && part.length() > 0;
    }

    private static boolean isParam(String pathSegment) {
        return pathSegment.startsWith(":");
    }

}
