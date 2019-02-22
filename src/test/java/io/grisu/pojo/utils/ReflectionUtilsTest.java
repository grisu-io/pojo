package io.grisu.pojo.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflectionUtilsTest {

    @Test
    public void ensureMethodsWithDifferentSignaturesHaveDifferentHashes() {
        final Map<Integer, Method> testMethod = Stream.of(this.getClass().getDeclaredMethods())
            .filter(m -> m.getName().equals("testMethod"))
            .collect(Collectors.toMap(m -> ReflectionUtils.computeSignatureHash(m), m -> m));

        assertEquals(6, testMethod.keySet().size());
    }

    @Test
    public void getMethodParametersTypes() {

        final Method method = Stream.of(this.getClass().getDeclaredMethods())
            .filter(m -> m.getName().equals("testMethodWithType"))
            .findFirst().get();


        final Type[] genericParameterTypes = method.getGenericParameterTypes();
    }

    private void testMethod() {
    }

    private void testMethod(String s) {
    }

    private void testMethod(Integer i) {
    }

    private void testMethod(List l) {
    }

    private void testMethod(String s, Integer i, List l) {
    }

    private void testMethod(List l, String s, Integer i) {
    }

    private void testMethodWithType(List<Integer> listOfIntegers) {
    }

}
