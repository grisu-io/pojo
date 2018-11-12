package io.grisu.pojo.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilsTypesTests {

   @Test
   public void checkStringType() {
      String string = "my string";

      final Type[] types = ReflectionUtils.guessBestTypes(new Object[] { string });

      assertEquals(1, types.length);
      assertEquals(String.class, types[0]);
   }

   @Test
   public void checkListType() {
      List<String> list = Arrays.asList("string 1", "string 2");

      final Type[] types = ReflectionUtils.guessBestTypes(new Object[] { list });

      assertEquals(1, types.length);
      assertEquals(ArrayList.class, ((ParameterizedType) types[0]).getRawType());
      assertEquals(String.class, ((ParameterizedType) types[0]).getActualTypeArguments()[0]);
   }

   @Test
   public void checkMapType() {
      Map<String, Integer> map = new HashMap<>();
      map.put("mypojo", 10);

      final Type[] types = ReflectionUtils.guessBestTypes(new Object[] { map });

      assertEquals(1, types.length);
      assertEquals(HashMap.class, ((ParameterizedType) types[0]).getRawType());
      assertEquals(String.class, ((ParameterizedType) types[0]).getActualTypeArguments()[0]);
      assertEquals(Integer.class, ((ParameterizedType) types[0]).getActualTypeArguments()[1]);
   }

}
