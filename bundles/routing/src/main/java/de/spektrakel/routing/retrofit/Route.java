/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.routing.retrofit;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route {
    // Upper and lower characters, digits, underscores, and hyphens, starting with a character.
    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

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
        pathParams = findParams(path);
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

        // TODO ... will fail tests for now :-)

        return null;
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
    private static String[] findParams(String path) {
        final Matcher matcher = PARAM_URL_REGEX.matcher(path);

        Set<String> patterns = new LinkedHashSet<>();
        while (matcher.find()) {
            patterns.add(matcher.group(1));
        }

        return patterns.stream().toArray(String[]::new);
    }

    private static boolean notEmpty(String part) {
        return part != null && part.length() > 0;
    }

}
