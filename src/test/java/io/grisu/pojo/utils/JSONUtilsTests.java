package io.grisu.pojo.utils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

import io.grisu.pojo.supportingclasses.NodeSubType;
import io.grisu.pojo.supportingclasses.PagedResult;
import io.grisu.pojo.supportingclasses.ParameterizedPojo;
import io.grisu.pojo.supportingclasses.TestPojo;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONUtilsTests {

    @Test
    public void shouldEncodeAndDecodeStringParameter() throws Exception {
        Object[] params = new Object[] { "String param" };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(1, decodedParams.length);
        assertEquals("String param", decodedParams[0]);
    }

    @Test
    public void shouldEncodeAndDecodeEnum() throws Exception {
        Object[] params = new Object[] { NodeSubType.models };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        Assert.assertEquals(NodeSubType.models, decodedParams[0]);
    }

    @Test
    public void shouldEncodeAndDecodeUUIDParameter() throws Exception {
        final UUID uuid = UUID.randomUUID();
        Object[] params = new Object[] { uuid };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(1, decodedParams.length);
        assertEquals(uuid, decodedParams[0]);
    }

    @Test
    public void shouldEncodeAndDecodeListOfStringsParameter() throws Exception {
        List<String> list = Arrays.asList("string 1", "string 2");
        Object[] params = new Object[] { list };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(1, decodedParams.length);
        assertEquals("string 1", ((List<String>) decodedParams[0]).get(0));
        assertEquals("string 2", ((List<String>) decodedParams[0]).get(1));
    }

    @Test
    public void shouldEncodeAndDecodeSetOfStringsParameter() throws Exception {
        Set<String> list = new HashSet<>();
        list.addAll(Arrays.asList("string 1", "string 2"));
        Object[] params = new Object[] { list };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(1, decodedParams.length);
        assertTrue(((Set<String>) decodedParams[0]).contains("string 1"));
        assertTrue(((Set<String>) decodedParams[0]).contains("string 2"));
    }

    @Test
    public void shouldEncodeAndDecodeMapOfParameters() throws Exception {
        TestPojo pojo = new TestPojo().setString("test pojo");
        Map<String, TestPojo> map = new HashMap<>();
        map.put("mypojo", pojo);
        Object[] params = new Object[] { map };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(1, decodedParams.length);
        assertEquals("test pojo", ((Map<String, TestPojo>) decodedParams[0]).get("mypojo").getString());
    }

    @Test
    public void shouldEncodeAndDecodeParameterizedPojo() {
        ParameterizedPojo<TestPojo> pojo = new ParameterizedPojo<>();

        pojo.mapOfT = new HashMap<>();
        pojo.mapOfT.put("A", new TestPojo().setString("AA"));

        Object[] params = new Object[] { pojo };
        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(1, decodedParams.length);
        ParameterizedPojo pr = (ParameterizedPojo) decodedParams[0];

        Map<String, TestPojo> i = pr.mapOfT;
        assertEquals(TestPojo.class, i.get("A").getClass());
    }

    @Test
    public void shouldEncodeAndDecodePagedResults() throws Exception {
        TestPojo pojo = new TestPojo();
        pojo.setString("My property");

        final PagedResult<TestPojo> pagedResult = new PagedResult().setResults(Arrays.asList(pojo));

        Object[] params = new Object[] { pagedResult };
        final byte[] bytes = JSONUtils.encode(params);

        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));
        PagedResult<TestPojo> pr = ((PagedResult<TestPojo>) decodedParams[0]);
        assertEquals("My property", pr.getResults().get(0).getString());
    }

    @Test
    public void shouldEncodeAndDecodeComplexParameters() throws Exception {
        final UUID uuid = UUID.randomUUID();
        Date date = new Date();
        TestPojo pojo = new TestPojo().setString("inner");
        TestPojo pojo2 = new TestPojo().setString("inner2");
        Map<String, TestPojo> map = new HashMap<>();
        map.put("mypojo", pojo2);
        List<String> list = Arrays.asList("string 1", "string 2");
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("str1", "str2", "str3"));
        Object[] params = new Object[] { "String param", uuid, date, pojo, map, list, null, set };

        final byte[] bytes = JSONUtils.encode(params);
        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));

        assertEquals(8, decodedParams.length);
        assertEquals("String param", decodedParams[0]);
        assertEquals(uuid, decodedParams[1]);
        assertEquals(date, decodedParams[2]);
        assertEquals("inner", ((TestPojo) decodedParams[3]).getString());
        assertEquals("inner2", ((TestPojo) ((Map<String, Object>) decodedParams[4]).get("mypojo")).getString());
        assertEquals("string 1", ((List<String>) decodedParams[5]).get(0));
        assertEquals("string 2", ((List<String>) decodedParams[5]).get(1));
        assertNull(decodedParams[6]);
        assertTrue(Set.class.isAssignableFrom(decodedParams[7].getClass()));
        assertTrue(((Set) decodedParams[7]).contains("str1"));
        assertTrue(((Set) decodedParams[7]).contains("str2"));
        assertTrue(((Set) decodedParams[7]).contains("str3"));
    }

    @Test
    public void shouldEncodeAndDecodeLocalDates() throws Exception {
        Object[] params = new Object[] { LocalDate.of(1972, 9, 1) };
        final byte[] bytes = JSONUtils.encode(params);

        final Object[] decodedParams = JSONUtils.decodeAsParams(bytes, ReflectionUtils.guessBestTypes(params));
        assertEquals(1, decodedParams.length);
        assertEquals(LocalDate.of(1972, 9, 1), decodedParams[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeAsParamsShouldCheckSignature_1() {
        JSONUtils.decodeAsParams(
            "[\"aaa\", \"bbb\", \"ccc\"]".getBytes(),
            new Type[] { String.class, String.class }
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeAsParamsShouldCheckSignature_2() {
        JSONUtils.decodeAsParams(
            "[\"aaa\", \"bbb\"]".getBytes(),
            new Type[] { String.class, String.class, String.class }
        );
    }
}
