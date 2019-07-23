package io.grisu.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grisu.pojo.annotations.Property;
import io.grisu.pojo.utils.PojoUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractPojo implements Pojo {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Set<String> properties;
    private final Map<String, Field> mapOfFields;
    private final Map<String, Type> mapOfTypes;
    private final Map<String, Method> serializers;
    private final Map<String, Method> deserializers;
    private final Map<String, Integer> hashes;
    private final Set<String> changedProperties;


    @SuppressWarnings("unchecked")
    public static <T extends Pojo> T instance(Class<T> c) {
        return (T) Enhancer.create(PojoUtils.getCGLibUnwrappedClass(c), (MethodInterceptor) (obj, method, args, proxy) -> {
            AbstractPojo pojo = (AbstractPojo) obj;
            String methodName = method.getName();
            if (methodName.startsWith("set")) {
                if (args.length == 1) {
                    final Method setterMethod = pojo.findMethod(method.getName(), args[0]);

                    if (setterMethod != null) {
                        pojo.mapOfFields
                            .keySet()
                            .stream()
                            .filter(s ->
                                pojo.mapOfFields.get(s)
                                    .getName()
                                    .equals(pojo.toLowercase(methodName.substring(3)))
                            ).findFirst().ifPresent(propertyName ->
                            pojo.markAsChanged(propertyName)
                        );
                    }
                }
            }

            return proxy.invokeSuper(obj, args);
        });
    }

    public AbstractPojo() {
        if (!this.getClass().getName().contains("$$EnhancerByCGLIB")) {
            throw new UnsupportedOperationException();
        }

        properties = new LinkedHashSet<>();
        mapOfFields = new HashMap<>();
        mapOfTypes = new HashMap<>();
        serializers = new HashMap<>();
        deserializers = new HashMap<>();
        hashes = new HashMap<>();
        changedProperties = new HashSet<>();

        Class<? extends AbstractPojo> aClass = PojoUtils.getCGLibUnwrappedClass(this.getClass());

        for (Field field : aClass.getDeclaredFields()) {
            final Property annotation = field.getAnnotation(Property.class);
            if (annotation != null) {
                String name = annotation.name();
                properties.add(name);
                mapOfFields.put(name, field);

            /*
              Maps of serializers/deserializers
\           */
                if (annotation.serializer()) {
                    Class serializedValueClass = annotation.serializerClass();
                    try {
                        serializers.put(name, aClass.getDeclaredMethod("_" + name + "Out", field.getType()));
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    }
                    try {
                        deserializers.put(name, aClass.getDeclaredMethod("_" + name + "In", serializedValueClass));
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    }
                    mapOfTypes.put(name, serializedValueClass);
                } else {
                    mapOfTypes.put(name, field.getGenericType());
                }

            /*
              Default values
             */
                if (!annotation.value().isEmpty()) {
                    field.setAccessible(true);
                    try {
                        field.set(this, field.getType().getConstructor(String.class).newInstance(annotation.value()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                }

            }
        }
    }

    @Override
    public Type __getTypeOf(String property) {
        return mapOfTypes.get(property);
    }

    @Override
    public Object put(String name, Object value) {
        changedProperties.add(name);

        if (value != null) {
            if (deserializers.containsKey(name)) {
                try {
                    value = deserializers.get(name).invoke(this, value);
                } catch (Exception e) {
                    log.debug("Deserializer can't be applied on field '" + name + "' for class " + value.getClass() + " (it was expected " + deserializers.get(name).getParameterTypes()[0].getTypeName() + ")");
                }
            }

            if (mapOfFields.containsKey(name)) {
                Field property = mapOfFields.get(name);
                String propertyName = property.getName();
                final Method setterMethod = findMethod("set" + toUppercase(propertyName), value);
                if (setterMethod != null) {
                    try {
                        setterMethod.invoke(this, value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {

                    try {
                        property.set(this, value);
                    } catch (IllegalAccessException e) {
                        log.error("Trying to directly set a property that is not public - " + this.getClass().getName() + "#" + name + "(" + value.getClass() + ")");
                        throw new RuntimeException(e);
                    }
                }
            } else {
                throw new RuntimeException(this.getClass().getName() + " doesn't contain a setter method or a public property for attribute called '" + name + "'");
            }

        }
        return this;
    }

    @Override
    public Object get(Object name) {
        Object value = null;

        final Method getterMethod = findGetter(name);
        if (getterMethod != null) {
            try {
                value = getterMethod.invoke(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Field property = mapOfFields.get(name);
            if (property != null) {
                try {
                    value = property.get(this);
                } catch (IllegalAccessException e) {
                    log.error("Trying to directly read a property that is not public -" + this.getClass().getName() + "#" + name + "()");
                    throw new RuntimeException(e);
                }
            }
        }

        if (value != null && serializers.containsKey(name)) {
            try {
                return serializers.get(name).invoke(this, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    private void markAsChanged(String propertyName) {
        changedProperties.add(propertyName);
    }

    @Override
    public boolean __hasChanged(String property) {
        return changedProperties.contains(property);
    }

    @Override
    public void __resetHashes() {
        keySet().forEach(name -> hashes.put(name, __computeHash(name)));
        changedProperties.clear();
    }

    @Override
    public Set<String> keySet() {
        return properties;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Pojo)) {
            return false;
        }

        Pojo otherPojo = (Pojo) other;

        for (String name : properties) {
            Object thisValue = get(name);
            Object otherValue = otherPojo.get(name);
            if (thisValue == otherValue)
                continue;
            if (thisValue != null && thisValue.equals(otherValue))
                continue;
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "[" + keySet()
            .stream()
            .filter(p -> __hasChanged(p))
            .map(p -> p + ":" + ((get(p) != null) ? get(p).toString() : "null"))
            .collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public int hashCode() {
        return properties.stream()
            .map(this::__computeHash)
            .filter(i -> i != null)
            .reduce(17, (a, b) -> 31 * a + b);
    }

    private Integer __computeHash(String name) {
        Integer hash = null;
        Object value = get(name);
        if (value != null) {
            hash = value.hashCode();
        }
        return hash;
    }

    private Method findMethod(String name, Object value) {
        Stream<Method> methodsWithSameName = Stream.of(this.getClass().getDeclaredMethods())
            .filter(n -> n.getName().equals(name));
        if (value != null) {
            methodsWithSameName = methodsWithSameName.filter(n -> n.getParameterTypes()[0].isAssignableFrom(value.getClass()));
        }
        return methodsWithSameName.findFirst().orElse(null);
    }

    private Method findGetter(Object name) {
        try {
            Field field = mapOfFields.get(name);
            String capitalizedPropertyName = toUppercase(field.getName());
            if (Boolean.class.isAssignableFrom(field.getType())) {
                try {
                    return this.getClass().getDeclaredMethod("is" + capitalizedPropertyName);
                } catch (NoSuchMethodException e) {
                }
            }
            return this.getClass().getDeclaredMethod("get" + capitalizedPropertyName);
        } catch (Exception e) {
            return null;
        }
    }

    private String toUppercase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private String toLowercase(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

}
