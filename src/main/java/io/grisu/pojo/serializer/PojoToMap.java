package io.grisu.pojo.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grisu.pojo.Pojo;
import io.grisu.pojo.utils.ReflectionUtils;

public class PojoToMap {
   @SuppressWarnings("unchecked")
   public static <U> U convert(Object o) {
      if (o == null) {
         return null;
      }

      if (o instanceof Object[]) {
         return (U) Arrays.stream((Object[]) o)
            .map(PojoToMap::convert)
            .collect(Collectors.toList())
            .toArray(new Object[] {});
      }

      if (o instanceof Pojo) {
         return (U) convert((Pojo) o);
      }

      if (o instanceof List) {
         return (U) ((Collection<?>) o).stream().map(PojoToMap::convert).collect(Collectors.toList());
      }

      if (o instanceof Set) {
         return (U) ((Collection<?>) o).stream().map(PojoToMap::convert).collect(Collectors.toSet());
      }

      if (o instanceof Map) {
         return (U) ((Map<?, ?>) o).keySet().stream()
            .filter(k -> ((Map) o).get(k) != null)
            .collect(Collectors.toMap(k -> k, k -> convert(((Map) o).get(k))));
      }

      return (U) o;
   }

   private static <T extends Pojo> Map<String, Object> convert(T pojo) {

      final Collection<String> props = pojo.keySet()
         .stream()
         .filter(p -> pojo.get(p) != null)
         .collect(Collectors.toList());

      Map<String, Object> outputMap = new HashMap<>();

      props.forEach(p -> {
         Object value = pojo.get(p);
         outputMap.put(p, convert(value));

         Type propertyType = pojo.__getTypeOf(p);

         if (propertyType instanceof ParameterizedType) {
            final List<Type> actualTypeArguments = Stream.of(((ParameterizedType) propertyType).getActualTypeArguments())
               .collect(Collectors.toList());

            if (actualTypeArguments.stream().anyMatch(type -> type instanceof TypeVariable)) {

               String __types = null;
               if (Collection.class.isAssignableFrom(value.getClass())) {
                  __types = ReflectionUtils.guessBestClass((Collection) value, actualTypeArguments.get(0)).getName();
               } else if (Map.class.isAssignableFrom(value.getClass())) {
                  __types = ReflectionUtils.guessBestClass(((Map) value).keySet(), actualTypeArguments.get(0)).getName() + "," +
                     ReflectionUtils.guessBestClass(((Map) value).values(), actualTypeArguments.get(1)).getName();
               }

               outputMap.put(p + "__java_reification", __types);
            }
         }

      });

      return outputMap;
   }

}
