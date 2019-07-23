package io.grisu.pojo.serializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.supportingclasses.TestPojo;
import io.grisu.pojo.supportingclasses.UserStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PojoToMapTests {

    @Test
    public void shouldConvertPojoStringAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        testPojo.setString("value");

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals("value", convert.get("string"));
    }

    @Test
    public void shouldConvertPojoUUIDAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        UUID uuid = UUID.randomUUID();
        testPojo.setUuid(uuid);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(uuid, convert.get("uuid"));
    }

    @Test
    public void shouldConvertPojoDateAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        Date date = new Date();
        testPojo.setDate(date);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(date, convert.get("date"));
    }

    @Test
    public void shouldConvertPojoNumberAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        Long number = 2L;
        testPojo.setNumber(number);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(number, convert.get("number"));
    }

    @Test
    public void shouldConvertPojoListOfStringByInterfaceAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        List<String> list = new ArrayList<>();
        list.add("str1");
        list.add("str2");
        list.add("str3");

        testPojo.setListOfStringByInterface(list);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(list, convert.get("listOfStringByInterface"));
    }

    @Test
    public void shouldConvertPojoListOfStringByClassAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        ArrayList<String> list = new ArrayList<>();
        list.add("str1");
        list.add("str2");
        list.add("str3");

        testPojo.setListOfStringByClass(list);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(list, convert.get("listOfStringByClass"));
    }

    @Test
    public void shouldConvertPojoSetOfStringByInterfaceAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        Set<String> set = new HashSet<>();
        set.add("str1");
        set.add("str2");
        set.add("str3");

        testPojo.setSetOfStringByInterface(set);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(set, convert.get("setOfStringByInterface"));
    }

    @Test
    public void shouldConvertPojoSetOfStringByClassAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        HashSet<String> set = new HashSet<>();
        set.add("str1");
        set.add("str2");
        set.add("str3");

        testPojo.setSetOfStringByClass(set);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(set, convert.get("setOfStringByClass"));
    }

    @Test
    public void shouldConvertPojoSetOfEnumByInterfaceAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        Set<UserStatus> set = new HashSet<>();
        set.add(UserStatus.ACTIVE);
        set.add(UserStatus.FROZEN);

        testPojo.setSetOfEnumByInterface(set);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(set, convert.get("setOfEnumByInterface"));
    }

    @Test
    public void shouldConvertPojoSetOfEnumByClassAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        HashSet<UserStatus> set = new HashSet<>();
        set.add(UserStatus.ACTIVE);
        set.add(UserStatus.FROZEN);

        testPojo.setSetOfEnumByClass(set);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(set, convert.get("setOfEnumByClass"));
    }

    @Test
    public void shouldConvertPojoMapOfStringByInterfaceAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("k1", AbstractPojo.instance(TestPojo.class).setString("inner pojo"));

        testPojo.setMapOfStringByInterface(map);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals("inner pojo", ((Map) ((Map) convert.get("mapOfStringByInterface")).get("k1")).get("string"));
    }

    @Test
    public void shouldConvertPojoListOfPojoByClassAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        ArrayList<TestPojo> list = new ArrayList<>();
        list.add(AbstractPojo.instance(TestPojo.class).setString("inner pojo"));

        testPojo.setInner(list);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals("inner pojo", ((Map) ((List) convert.get("inner")).get(0)).get("string"));
    }

    @Test
    public void shouldConvertPojoListOfTypeAttributeIntoMap() {
        TestPojo<String> testPojo = AbstractPojo.instance(TestPojo.class);
        ArrayList<String> list = new ArrayList<>();
        list.add("str1");
        list.add("str2");
        list.add("str3");

        testPojo.setListOfType(list);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(2, convert.keySet().size());
        assertEquals(list, convert.get("listOfType"));
        assertEquals("java.lang.String", convert.get("listOfType__java_reification"));
    }

    @Test
    public void shouldConvertPojoLocalDateAttributeIntoMap() {
        TestPojo testPojo = AbstractPojo.instance(TestPojo.class);
        LocalDate localDate = LocalDate.of(1972, 9, 1);
        testPojo.setLocalDate(localDate);

        final Map<String, Object> convert = PojoToMap.convert(testPojo);

        assertEquals(1, convert.keySet().size());
        assertEquals(localDate.format(DateTimeFormatter.ISO_DATE), convert.get("localDate"));
    }

}
