package foo.sling;

import foo.sling.api.Path;
import foo.sling.common.DynamicResourceProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Properties;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;


/** sling internals of the api implementation */
@Component(immediate = true)
@Properties({
        @Property(
                label = "Root paths",
                description = "Root paths this Sling Resource Provider will respond to",
                name = ResourceProvider.ROOTS,
                value = {"/api"})
})
@Service(value = {ApiResourceProvider.class, ResourceProvider.class})
public class ApiResourceProvider extends DynamicResourceProvider implements ResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ApiResourceProvider.class);

    private final Router router = new Router();


    @Override
    protected boolean valid(String path) {
        return path.startsWith("/api") && !path.endsWith(".json"); // TODO <-- strip last path segments
    }

    @Override
    protected ResourceData resourceAt(String path) {
        Optional<Class<?>> target = router.match(path);
        if (target.isPresent()) {
            return resource(target.get());
        }

        return null;
    }

    private ResourceData resource(Class<?> adaptTarget) {
        ResourceMetadata metadata = new ResourceMetadata();
        metadata.put("targetType", adaptTarget);

        return new ResourceData("procato-workbench/api", new HashMap<>(), metadata);
    }

    @Override
    protected String resourceType() {
        return "procato-workbench/api";
    }

    public Router router() {
        return router;
    }

    private BundleListener listener;

    @Activate
    protected void activate(final ComponentContext ctx) {
        listener = new BundleListener(ctx.getBundleContext(), this);
        listener.listen();
    }

    @Deactivate
    protected void deactivate() {
        listener.stop();
    }


    private static class BundleListener implements BundleTrackerCustomizer {

        private final BundleTracker bundleTracker;
        private final ApiResourceProvider provider;

        private BundleListener(BundleContext bundleContext, ApiResourceProvider provider) {
            bundleTracker = new BundleTracker(bundleContext, Bundle.ACTIVE, this);
            this.provider = provider;
        }

        public void listen() {
            bundleTracker.open();
        }

        public void stop() {
            bundleTracker.close();
        }


        @Override
        public Object addingBundle(Bundle bundle, BundleEvent bundleEvent) {
            // TODO ..
            List<String> registeredPaths = new ArrayList<>();

            Dictionary<?, ?> headers = bundle.getHeaders();
            String packageList = PropertiesUtil.toString(headers.get("Dynamic-Resources-Packages"), "");
            packageList = StringUtils.deleteWhitespace(packageList);
            if (StringUtils.isEmpty(packageList)) {
                return null;
            }
            LOG.debug("Dynamic-Resource-Packages Header in bundle={}", bundle.getSymbolicName());

            String[] packages = packageList.split(",");
            for (String singlePackage : packages) {
                LOG.debug("Scanning for classes in package={}", singlePackage);

                @SuppressWarnings("unchecked")
                Enumeration<URL> classUrls = bundle.findEntries("/" + singlePackage.replace('.', '/'), "*.class", true);

                if (classUrls == null) {
                    LOG.warn("No classes found in package {}, ignoring", singlePackage);
                    continue;
                }

                while (classUrls.hasMoreElements()) {
                    URL url = classUrls.nextElement();
                    String className = toClassName(url);
                    try {
                        Class<?> classType = bundle.loadClass(className);
                        Path pathAnnotation = classType.getAnnotation(Path.class);
                        if (pathAnnotation != null) {
                            provider.router().register(pathAnnotation.value(), classType);
                            registeredPaths.add(pathAnnotation.value());
                        }
                    } catch (ClassNotFoundException e) {
                        LOG.warn("Unable to load class", e);
                    } catch (NoClassDefFoundError err) {
                        LOG.error("NoClassDefFoundError while loading class", err);
                    }
                }
            }

            return registeredPaths; // <-- the tracking object
        }

        @Override
        public void modifiedBundle(Bundle bundle, BundleEvent bundleEvent, Object trackingObject) {
            // TODO ..

        }

        @Override
        public void removedBundle(Bundle bundle, BundleEvent bundleEvent, Object trackingObject) {
            // TODO ..
            List<String> registeredPaths = (List<String>) trackingObject;
            for (String path : registeredPaths) {
                provider.router().unregister(path);
            }
        }

        /** Convert class URL to class name */
        private String toClassName(URL url) {
            final String f = url.getFile();
            final String cn = f.substring(1, f.length() - ".class".length());
            return cn.replace('/', '.');
        }
    }

}
