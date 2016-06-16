package foo.sling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * A sinatra-inspired routing that uses a mapping table like this:
 *
 * (path) -> Callback.class
 * - '/api/products' -> ProductList.class
 * - '/api/product/:id' -> Product.class
 *
 * @link http://www.sinatrarb.com/intro.html#Routes
 */
public class Router {

    private final Map<String, Class<?>> mappingTable = new HashMap<>();


    public void register(String path, Class<?> target) {
        mappingTable.put(path, target);
    }

    public void unregister(String path) {
        mappingTable.remove(path);
    }

    public List<String> paths() {
        return mappingTable.keySet().stream().collect(Collectors.toList());
    }

    public List<Class<?>> targets() {
        return mappingTable.values().stream().collect(Collectors.toList());
    }

    public Optional<Class<?>> match(String path) {
        return mappingTable.entrySet().stream()
                .filter(e -> e.getKey().startsWith(path)) // TODO <-- improve route matching!!!
                .map(Map.Entry::getValue)
                .findFirst();
    }

}
