package ritogaems.tov.util;

// object pool class, providing reusable collection of objects of specified type

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {

    // factory that can provide instances of objects managed within pool
    public interface ObjectFactory<T> {
        T createObject();
    }

    // object factory used to populate pool
    private final ObjectFactory<T> factory;

    // object pool and maximum pool size constant
    private final List<T> pool;
    private final int maxPoolSize;

    // constructor
    public Pool(ObjectFactory<T> factory, int maxPoolSize) {
        this.factory = factory;
        this.maxPoolSize = maxPoolSize;
        pool = new ArrayList<>(maxPoolSize);
    }

    // get object instance - can be a reused instance from pool
    public T get() {
        T object;

        if (pool.isEmpty()) {
            object = factory.createObject();
         } else {
            object = pool.remove(pool.size() - 1);
        }

        return object;
    }

    // add the object to the pool
    public void add(T object) {
        if (pool.size() < maxPoolSize) {
            pool.add(object);
        }
    }
}