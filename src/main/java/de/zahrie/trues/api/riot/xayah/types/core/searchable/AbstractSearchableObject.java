package de.zahrie.trues.api.riot.xayah.types.core.searchable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractSearchableObject implements SearchableObject {
    private static final Map<Class<?>, Object> locks = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Multimap<Class<?>, Method>> searchable = new ConcurrentHashMap<>();

    @Override
    public boolean contains(final Object item) {
        if(item == null) {
            return false;
        }

        final Collection<Method> targets = getSearchTypes().get(item.getClass());
        if(targets == null || targets.isEmpty()) {
            return false;
        }

        for(final Method method : targets) {
            final Object result;
            try {
                result = method.invoke(this);
            } catch(final InvocationTargetException e) {
                if(e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException)e.getCause();
                } else {
                    throw new RuntimeException(e.getCause());
                }
            } catch(IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException(e);
            }

            if(item == result) {
                return true;
            } else if(item.equals(result)) {
                return true;
            } else if(result instanceof final SearchableObject obj) {
              if(obj.contains(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Multimap<Class<?>, Method> getSearchTypes() {
        final Class<?> clazz = this.getClass();

        Multimap<Class<?>, Method> searchTypes = searchable.get(clazz);
        if(searchTypes == null) {
            Object lock = locks.get(clazz);
            if(lock == null) {
                synchronized(locks) {
                  lock = locks.computeIfAbsent(clazz, k -> new Object());
                }
            }

            synchronized(lock) {
                searchTypes = searchable.get(clazz);
                if(searchTypes == null) {
                    final ImmutableMultimap.Builder<Class<?>, Method> builder = ImmutableMultimap.builder();
                    for(final Method method : clazz.getMethods()) {
                        if(method.isAnnotationPresent(Searchable.class)) {
                            final Searchable annotation = method.getAnnotation(Searchable.class);
                            for(final Class<?> c : annotation.value()) {
                                builder.put(c, method);
                            }
                        }
                    }
                    searchTypes = builder.build();
                    searchable.put(clazz, searchTypes);
                }
            }
        }
        return searchTypes;
    }
}
