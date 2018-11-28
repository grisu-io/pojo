package io.grisu.pojo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.Gettable;
import io.grisu.pojo.Pojo;
import io.grisu.pojo.Puttable;
import io.grisu.pojo.annotations.Property;

public class PojoUtils {

   public static <T> Collection<String> changedProperties(Pojo pojo) {
      return pojo.keySet()
         .stream()
         .filter(key -> pojo.__hasChanged(key))
         .collect(Collectors.toList());
   }

   public static <T extends Pojo> T instancePojoFrom(Class<T> pojoClass, Gettable from) {
      try {
         return copyIntoPojoFrom(pojoClass.getConstructor().newInstance(), from);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static <T extends Pojo> T copyIntoPojoFrom(T to, Map from) {
      return copyIntoPojoFrom(to, name -> from.get(name));
   }

   @SuppressWarnings("unchecked")
   public static <T extends Pojo> T copyIntoPojoFrom(T to, Gettable from) {
      if (from != null) {
         final Map<String, Type> mapOfClassForProperties = Arrays.stream(to.getClass().getDeclaredFields())
            .filter(field -> field.getAnnotation(Property.class) != null)
            .collect(Collectors.toMap(f -> f.getAnnotation(Property.class).name(), Field::getGenericType));

         for (String property : to.keySet()) {
            try {
               Object value = from.get(property);

               if (value != null) {
                  if (to instanceof AbstractPojo) {
                     final Type type = mapOfClassForProperties.get(property);
                     if (type instanceof Class) {
                        Class _clazz = (Class<?>) type;
                        if (Pojo.class.isAssignableFrom(_clazz)) {
                           value = instancePojoFrom(_clazz, (Pojo) value);
                        }
                     } else {
                        ParameterizedType pType = (ParameterizedType) type;
                        Class _clazz = (Class) pType.getRawType();
                        final Type[] genericsTypes = ReflectionUtils.getGenericsTypes(pType);

                        Function<Type, Boolean> isPojoAssignable = (_type -> _type instanceof Class && Pojo.class.isAssignableFrom((Class) _type));

                        if (Set.class.isAssignableFrom(_clazz)) {
                           if (isPojoAssignable.apply(genericsTypes[0])) {
                              value = ((Set<?>) value).stream().map(v -> instancePojoFrom((Class) genericsTypes[0], (Pojo) v)).collect(Collectors.toSet());
                           }
                        } else if (Collection.class.isAssignableFrom(_clazz)) {
                           if (isPojoAssignable.apply(genericsTypes[0])) {
                              value = ((Collection<?>) value).stream().map(v -> instancePojoFrom((Class) genericsTypes[0], (Pojo) v)).collect(Collectors.toList());
                           }
                        } else if (Map.class.isAssignableFrom(_clazz)) {
                           Function fK = (isPojoAssignable.apply(genericsTypes[0])) ?
                              k -> instancePojoFrom((Class) genericsTypes[0], (Pojo) k) :
                              k -> k;

                           Function fV = (isPojoAssignable.apply(genericsTypes[1])) ?
                              v -> instancePojoFrom((Class) genericsTypes[1], (Pojo) v) :
                              v -> v;

                           if (isPojoAssignable.apply(genericsTypes[1])) {
                              value = ((Map<?, ?>) value).keySet().stream().collect(Collectors.toMap(fK, fV));
                           }
                        } else if (Pojo.class.isAssignableFrom(_clazz)) {
                           value = instancePojoFrom(_clazz, (Pojo) value);
                        }

                     }

                  }

               }
               to.put(property, value);
            } catch (Exception e) {
               // left empty on purpose
            }
         }
      }
      return to;
   }

   public static <T extends Pojo> Map copyFromPojo(Map to, T from) {
      copyFromPojo((name, value) -> to.put(name, value), from);
      return to;
   }

   public static <T extends Pojo> Puttable copyFromPojo(Puttable to, T from) {
      if (to != null && from != null) {
         from.keySet().stream().forEach(k -> {
            Object v = from.get(k);
            if (v != null) {
               to.put(k, from.get(k));
            }
         });
      }
      return to;
   }

   public static Set<String> checkForNullProperties(Pojo pojo, String... keys) {
      Collection<String> k;
      if (keys != null && keys.length > 0) {
         k = new ArrayList<>();
         Collections.addAll(k, keys);
      } else {
         k = pojo.keySet();
      }

      if (pojo != null) {
         return pojo.keySet()
            .stream()
            .filter(key -> pojo.get(key) == null && k.contains(key))
            .collect(Collectors.toSet());
      }

      return null;
   }
}
