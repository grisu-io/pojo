package io.grisu.pojo.utils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grisu.pojo.Pojo;
import io.grisu.pojo.serializer.MapToPojo;
import io.grisu.pojo.serializer.PojoToMap;

public class JSONUtils {

   protected static final ObjectMapper jsonMapper;

   static {
      jsonMapper = new ObjectMapper();
      jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   }

   public static byte[] encode(Object param) {
      try {
         return jsonMapper.writeValueAsBytes(PojoToMap.convert(param));
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static String encodeAsString(Object param) {
      return new String(encode(param));
   }

   public static <T> T decode(byte[] bytes, Type type) {
      try {
         Class _clazz = null;

         if (type instanceof ParameterizedType) {
            _clazz = (Class) ((ParameterizedType) type).getRawType();
         } else {
            _clazz = (Class) type;
         }

         if (Pojo.class.isAssignableFrom(_clazz)) {
            _clazz = Map.class;
         }

         return (T) MapToPojo.convert(jsonMapper.readValue(bytes, _clazz), type);
      } catch (IOException e) {
         try {
            return (T) jsonMapper.readValue(bytes, Map.class);
         } catch (Exception e2) {
            e2.printStackTrace();
         }
         throw new RuntimeException(e);
      }
   }

   public static Object[] decodeAsParams(byte[] bytes, Type[] types) {
      final List<Object> outList = new ArrayList<>();

      try {
         final List<Object> params = jsonMapper.readValue(bytes, List.class);
         for (int i = 0; i < types.length; i++) {
            final Object o = MapToPojo.convert(params.get(i), types[i]);
            outList.add(o);
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      }

      return outList.toArray();
   }

}
