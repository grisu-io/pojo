package io.grisu.pojo.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import io.grisu.pojo.supportingclasses.TestPojo;
import io.grisu.pojo.supportingclasses.UserStatus;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapToPojoTests {

    @Test
    public void shouldConvertMapStringAttributeIntoPojo() {
        Map<String, Object> map = new HashMap<>();
        map.put("string", "value");

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals("value", convert.getString());
    }

    @Test
    public void shouldConvertMapUUIDAttributeIntoPojo() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(uuid, convert.getUuid());
    }

    @Test
    public void shouldConvertMapDateAttributeIntoPojo() {
        Date date = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(date, convert.getDate());
    }

    @Test
    public void shouldConvertMapNumberAttributeIntoPojo() {
        Long number = 2L;
        Map<String, Object> map = new HashMap<>();
        map.put("number", number);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(number, convert.getNumber());
    }

    @Test
    public void shouldConvertMapListOfStringByInterfaceAttributeIntoPojo() {
        List<String> list = new ArrayList<>();
        list.add("str1");
        list.add("str2");
        list.add("str3");

        Map<String, Object> map = new HashMap<>();
        map.put("listOfStringByInterface", list);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(list, convert.getListOfStringByInterface());
    }

    @Test
    public void shouldConvertMapListOfStringByClassAttributeIntoPojo() {
        ArrayList<String> list = new ArrayList<>();
        list.add("str1");
        list.add("str2");
        list.add("str3");

        Map<String, Object> map = new HashMap<>();
        map.put("listOfStringByClass", list);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(list, convert.getListOfStringByClass());
    }

    @Test
    public void shouldConvertMapSetOfStringByInterfaceAttributeIntoPojo() {
        Set<String> set = new HashSet<>();
        set.add("str1");
        set.add("str2");
        set.add("str3");

        Map<String, Object> map = new HashMap<>();
        map.put("setOfStringByInterface", set);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(set, convert.getSetOfStringByInterface());
    }

    @Test
    public void shouldConvertPojoSetOfStringByClassAttributeIntoMap() {
        Set<String> set = new HashSet<>();
        set.add("str1");
        set.add("str2");
        set.add("str3");

        Map<String, Object> map = new HashMap<>();
        map.put("setOfStringByClass", set);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(set, convert.getSetOfStringByClass());
    }

    @Test
    public void shouldConvertMapSetOfEnumByInterfaceAttributeIntoPojo() {
        Set<UserStatus> set = new HashSet<>();
        set.add(UserStatus.ACTIVE);
        set.add(UserStatus.FROZEN);

        Map<String, Object> map = new HashMap<>();
        map.put("setOfEnumByInterface", set);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(set, convert.getSetOfEnumByInterface());
    }

    @Test
    public void shouldConvertMapSetOfEnumByClassAttributeIntoPojo() {
        HashSet<UserStatus> set = new HashSet<>();
        set.add(UserStatus.ACTIVE);
        set.add(UserStatus.FROZEN);

        Map<String, Object> map = new HashMap<>();
        map.put("setOfEnumByClass", set);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(set, convert.getSetOfEnumByClass());
    }

    @Test
    public void shouldConvertMapMapOfStringByInterfaceAttributeIntoPojo() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapOfStringByInterface = new HashMap<>();
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("string", "inner pojo");
        mapOfStringByInterface.put("k1", innerMap);
        map.put("mapOfStringByInterface", mapOfStringByInterface);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(mapOfStringByInterface, convert.getMapOfStringByInterface());
    }

    @Test
    public void shouldConvertMapListOfPojoByClassAttributeIntoPojo() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("string", "inner pojo");
        list.add(innerMap);
        map.put("inner", list);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(1, convert.getInner().size());
        assertEquals("inner pojo", ((TestPojo) convert.getInner().get(0)).getString());
    }

    @Test
    public void shouldConvertMapListOfTypeAttributeIntoPojo() {
        Map<String, Object> map = new HashMap<>();
        map.put("listOfType__java_reification", "java.lang.String");
        ArrayList<String> list = new ArrayList<>();
        list.add("str1");
        list.add("str2");
        list.add("str3");
        map.put("listOfType", list);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, TestPojo.class);

        assertEquals(3, convert.getListOfType().size());
        assertEquals(list, convert.getListOfType());
    }

    @Test
    public void shouldConvertParameterizedPojo() {
        ParameterizedType type = TypeUtils.parameterize(TestPojo.class, new Type[] { TestPojo.class });

        Map<String, Object> map = new HashMap<>();
        map.put("listOfType__java_reification", "io.grisu.pojo.supportingclasses.TestPojo");
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("string", "inner pojo");

        list.add(innerMap);
        list.add(innerMap);
        list.add(innerMap);
        map.put("listOfType", list);

        final TestPojo convert = (TestPojo) MapToPojo.convert(map, type);

        assertEquals(3, convert.getListOfType().size());
        assertEquals(TestPojo.class, convert.getListOfType().get(0).getClass());
        assertEquals("inner pojo", ((TestPojo) convert.getListOfType().get(0)).getString());
        assertEquals(TestPojo.class, convert.getListOfType().get(1).getClass());
        assertEquals(TestPojo.class, convert.getListOfType().get(2).getClass());
    }

}
