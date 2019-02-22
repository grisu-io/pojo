package io.grisu.pojo.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grisu.pojo.Pojo;
import io.grisu.pojo.utils.ReflectionUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapToPojo {

    private static final Logger log = LoggerFactory.getLogger(MapToPojo.class);

    public static Object convert(Object value, Type type) {
        if (value == null || value.getClass().equals(type)) {
            return value;
        }

        Class _clazz = null;

        try {
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;
                _clazz = (Class) pType.getRawType();

                if (Set.class.isAssignableFrom(_clazz)) {
                    return handleSet(value, pType);
                } else if (Collection.class.isAssignableFrom(_clazz)) {
                    return handleCollection(value, pType);
                } else if (Pojo.class.isAssignableFrom(_clazz)) {
                    return handlePojo(value, _clazz);
                } else if (Map.class.isAssignableFrom(_clazz)) {
                    return handleMap(value, pType);
                }
            } else {
                _clazz = (Class) type;
            }

            if (_clazz.equals(Object.class)) {
                return value;
            } else if (_clazz.equals(String.class)) {
                return value.toString();
            } else if (_clazz.equals(UUID.class)) {
                return UUID.fromString((String) value);
            } else if (_clazz.equals(Date.class)) {
                return new Date((long) value);
            } else if (_clazz.equals(LocalDate.class)) {
                if (value instanceof Map) {
                    Map map = (Map) value;
                    return LocalDate.of((int) map.get("year"), (int) map.get("monthValue"), (int) map.get("dayOfMonth"));
                }
                if (value instanceof String) {
                    return LocalDate.parse((CharSequence) value, DateTimeFormatter.ISO_DATE);
                }
            } else if (_clazz.equals(Integer.class)) {
                return (value instanceof String) ? Integer.parseInt((String) value) : ((Number) value).intValue();
            } else if (_clazz.equals(Long.class)) {
                return (value instanceof String) ? Long.parseLong((String) value) : ((Number) value).longValue();
            } else if (_clazz.equals(Double.class)) {
                return (value instanceof String) ? Double.parseDouble((String) value) : ((Number) value).doubleValue();
            } else if (Enum.class.isAssignableFrom(_clazz)) {
                return Enum.valueOf(_clazz, (String) value);
            } else if (Set.class.isAssignableFrom(_clazz)) {
                return handleSet(value, _clazz);
            } else if (Collection.class.isAssignableFrom(_clazz)) {
                return handleCollection(value, _clazz);
            } else if (Pojo.class.isAssignableFrom(_clazz)) {
                return handlePojo(value, _clazz);
            } else if (Map.class.isAssignableFrom(_clazz)) {
                return handleMap(value, _clazz);
            }
        } catch (RuntimeException e) {
            log.debug(value.toString() + " - " + type);
            e.printStackTrace();
        }

        throw new RuntimeException("Missing case... Can't convert " + value + " into " + type.getTypeName());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Pojo> T handlePojo(Object object, Class pojoClass) {
        if (object == null) {
            return null;
        }

        Map map = (Map) object;

        T newObject = null;

        try {
            newObject = (T) pojoClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String property : newObject.keySet()) {
            Object value = map.get(property);
            if (value != null) {
                try {
                    Type type = newObject.__getTypeOf(property);

                    String __types = (String) map.get(property + "__java_reification");
                    if (__types != null) {
                        Type[] types = Stream.of((__types)
                            .split(","))
                            .map(c -> {
                                try {
                                    return Class.forName(c);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .collect(Collectors.toList())
                            .toArray(new Type[] {});

                        if (type instanceof ParameterizedType) {
                            type = ((ParameterizedType) type).getRawType();
                        }
                        type = TypeUtils.parameterize(((Class) type), types);
                    }

                    value = convert(value, type);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                    // left empty on purpose
                }
                newObject.put(property, value);
            }
        }

        return newObject;
    }

    @SuppressWarnings("unchecked")
    private static Set handleSet(Object value, Class _clazz) {
        Collection<?> collection = ((Collection<?>) value);

        return (Set) collection.stream()
            .collect(Collectors.toCollection(() -> (Set) ReflectionUtils.instance((Class) _clazz)));
    }

    @SuppressWarnings("unchecked")
    private static Set handleSet(Object value, ParameterizedType parameterizedType) {
        Type setOf = ReflectionUtils.getGenericsTypes(parameterizedType)[0];
        Collection<?> collection = ((Collection<?>) value);

        return (Set) collection.stream()
            .map(s -> convert(s, setOf))
            .collect(Collectors.toCollection(() -> (Set) ReflectionUtils.instance((Class) parameterizedType.getRawType())));
    }

    @SuppressWarnings("unchecked")
    private static Collection handleCollection(Object value, Class _clazz) {
        Collection<?> collection = (Collection) value;

        return (Set) collection.stream()
            .collect(Collectors.toCollection(() -> (Collection) ReflectionUtils.instance((Class) _clazz)));
    }

    @SuppressWarnings("unchecked")
    private static Collection handleCollection(Object value, ParameterizedType parameterizedType) {
        Type setOf = ReflectionUtils.getGenericsTypes(parameterizedType)[0];
        Collection<?> collection = (Collection) value;

        return (Collection) collection.stream()
            .map(s -> convert(s, setOf))
            .collect(Collectors.toCollection(() -> (Collection) ReflectionUtils.instance((Class) parameterizedType.getRawType())));
    }

    @SuppressWarnings("unchecked")
    private static Map handleMap(Object value, Class _clazz) {
        Map map = (Map) value;

        return (Map) ((Set<?>) map.keySet()).stream()
            .collect(Collectors.toMap(k -> k, k -> map.get(k), (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            }, () -> (Map) ReflectionUtils.instance(_clazz)));
    }

    @SuppressWarnings("unchecked")
    private static Map handleMap(Object value, ParameterizedType parameterizedType) {
        Map map = (Map) value;
        final Type[] mapOf = ReflectionUtils.getGenericsTypes(parameterizedType);

        return (Map) ((Set<?>) map.keySet()).stream()
            .collect(Collectors.toMap(k -> convert(k, mapOf[0]), k -> convert(map.get(k), mapOf[1]), (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            }, () -> (Map) ReflectionUtils.instance((Class) parameterizedType.getRawType())));
    }

}
