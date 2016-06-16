/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.models.resources.impl;


import de.spektrakel.sling.models.resources.ModelResource;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.models.annotations.Model;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;

public class ModelPackageBundleListener implements BundleTrackerCustomizer {

    static final String HEADER = "Sling-Model-Packages";

    private final BundleContext bundleContext;

    private final BundleTracker bundleTracker;


    public ModelPackageBundleListener(BundleContext bundleContext, ModelResourceProvider provider) {
        this.bundleContext = bundleContext;
        this.bundleTracker = new BundleTracker(bundleContext, Bundle.ACTIVE, this);
        this.bundleTracker.open();
    }

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        List<ServiceRegistration> regs = new ArrayList<ServiceRegistration>();

        Dictionary<?, ?> headers = bundle.getHeaders();
        String packageList = PropertiesUtil.toString(headers.get(HEADER), null);
        if (packageList != null) {

            packageList = packageList.trim(); //StringUtils.deleteWhitespace(packageList);
            String[] packages = packageList.split(",");
            for (String singlePackage : packages) {
                @SuppressWarnings("unchecked")
                Enumeration<URL> classUrls = bundle.findEntries("/" + singlePackage.replace('.', '/'), "*.class",
                        true);

                if (classUrls == null) {
                    //log.warn("No adaptable classes found in package {}, ignoring", singlePackage);
                    continue;
                }

                while (classUrls.hasMoreElements()) {
                    URL url = classUrls.nextElement();
                    String className = toClassName(url);
                    try {
                        Class<?> implType = bundle.loadClass(className);
                        Model annotation = implType.getAnnotation(Model.class);
                        ModelResource modelResourceAnnotation = implType.getAnnotation(ModelResource.class);
                        if (annotation != null && modelResourceAnnotation != null) {

                            // TODO: found a @ModelResource ... register with provider

                        }
                    } catch (ClassNotFoundException e) {
                        //log.warn("Unable to load class", e);
                    }

                }
            }
        }
        return regs.toArray(new ServiceRegistration[0]);
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        // TODO ... remoce known @ModelResource from provider
    }

    public synchronized void unregisterAll() {
        this.bundleTracker.close();
    }

    /** Convert class URL to class name */
    private String toClassName(URL url) {
        final String f = url.getFile();
        final String cn = f.substring(1, f.length() - ".class".length());
        return cn.replace('/', '.');
    }

    private String[] toStringArray(Class<?>[] classes) {
        String[] arr = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            arr[i] = classes[i].getName();
        }
        return arr;
    }

}
