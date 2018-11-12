package io.grisu.pojo.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grisu.pojo.Pojo;
import org.apache.commons.lang3.reflect.TypeUtils;

public class ReflectionUtils {

    public static Type[] guessBestTypes(Object[] params) {
        return Stream.of(params)
            .map(p -> {
                if (p == null) {
                    return Object.class;
                }

                Class _clazz = p.getClass();

                if (Pojo.class.isAssignableFrom(_clazz)) {
                    return _clazz;
                }

                if (Map.class.isAssignableFrom(_clazz)) {
                    Map map = (Map) p;
                    return TypeUtils.parameterize(_clazz, new Type[] { guessBestClass(map.keySet()), guessBestClass(map.values()) });
                }

                if (Collection.class.isAssignableFrom(_clazz)) {
                    if (_clazz.equals(Arrays.asList().getClass())) {
                        _clazz = ArrayList.class;
                    }
                    return TypeUtils.parameterize(_clazz, new Type[] { guessBestClass((Collection) p) });
                }
                return _clazz;
            })
            .toArray(Type[]::new);
    }

    public static Class guessBestClass(Collection values) {
        return guessBestClass(values, null);
    }

    public static Class guessBestClass(Collection values, Type type) {
        if (type == null || !(type instanceof Class)) {
            final Optional<Object> optionalElementOfCollection = values.stream().filter(p -> p != null).findAny();
            if (optionalElementOfCollection.isPresent()) {
                return optionalElementOfCollection.get().getClass();
            }
            return Object.class;
        }
        return (Class) type;
    }

    public static Type[] getGenericsTypes(ParameterizedType pType) {
        return Stream.of(pType.getActualTypeArguments())
            .toArray(Type[]::new);
    }

    public static Object instance(Class _clazzClass) {
        Class instanceClass = null;

        if (Map.class.isAssignableFrom(_clazzClass)) {
            if (_clazzClass.isInterface()) {
                instanceClass = HashMap.class;
            } else {
                try {
                    _clazzClass.getConstructor().newInstance();
                    instanceClass = _clazzClass;
                } catch (Exception e) {
                    instanceClass = HashMap.class;
                }
            }
        }

        if (Collection.class.isAssignableFrom(_clazzClass)) {
            if (_clazzClass.isInterface()) {
                if (Set.class.isAssignableFrom(_clazzClass)) {
                    instanceClass = HashSet.class;
                } else {
                    instanceClass = ArrayList.class;
                }
            } else {
                try {
                    _clazzClass.getConstructor().newInstance();
                    instanceClass = _clazzClass;
                } catch (Exception e) {
                    instanceClass = ArrayList.class;
                }
            }
        }

        try {
            return instanceClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Integer computeSignatureHash(Method m) {
        String s = m.getName();

        s = s + Stream.of(m.getParameterTypes())
            .map(p -> p.getName())
            .collect(Collectors.joining(","));

        return s.hashCode();
    }
}
