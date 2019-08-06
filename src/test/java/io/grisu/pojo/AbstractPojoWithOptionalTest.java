package io.grisu.pojo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.grisu.pojo.supportingclasses.PojoWithOptional;
import io.grisu.pojo.supportingclasses.SimpleOptionalPojo;
import io.grisu.pojo.supportingclasses.TestPojo;
import io.grisu.pojo.utils.PojoUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractPojoWithOptionalTest {

    @Test
    public void shouldReturnedChangedOptionals() {

        final PojoWithOptional pojoWithOptional = new PojoWithOptional();

        assertEquals(0, PojoUtils.changedProperties(pojoWithOptional).size());
        pojoWithOptional.setBool(Optional.of(Boolean.TRUE));

        assertEquals(1, PojoUtils.changedProperties(pojoWithOptional).size());
        assertEquals(Arrays.asList("boolean"), PojoUtils.changedProperties(pojoWithOptional));

        pojoWithOptional.setDate(Optional.of(LocalDate.now()));
        assertEquals(2, PojoUtils.changedProperties(pojoWithOptional).size());
        assertEquals(toSet("boolean", "date"), PojoUtils.changedProperties(pojoWithOptional).stream().collect(Collectors.toSet()));

        pojoWithOptional.setInteger(Optional.of(434));
        assertEquals(3, PojoUtils.changedProperties(pojoWithOptional).size());
        assertEquals(toSet("boolean", "date", "integer"), PojoUtils.changedProperties(pojoWithOptional).stream().collect(Collectors.toSet()));

        pojoWithOptional.setString(Optional.of("test"));
        assertEquals(4, PojoUtils.changedProperties(pojoWithOptional).size());
        assertEquals(toSet("boolean", "date", "integer", "string"), PojoUtils.changedProperties(pojoWithOptional).stream().collect(Collectors.toSet()));

    }

    @Test
    public void shouldCopyPojoWithOptional() {
        final PojoWithOptional pojoWithOptional = new PojoWithOptional();
        pojoWithOptional.setBool(Optional.of(Boolean.TRUE))
            .setDate(Optional.of(LocalDate.now()))
            .setInteger(Optional.of(434))
            .setString(Optional.of("test"))
            .setInner(Optional.of(Arrays.asList(new SimpleOptionalPojo().setKey(Optional.of("my key string")))));

        final PojoWithOptional copiedPojo = PojoUtils.instancePojoFrom(PojoWithOptional.class, pojoWithOptional);

        assertEquals(pojoWithOptional.getBool(), copiedPojo.getBool());
        assertEquals(pojoWithOptional.getDate(), copiedPojo.getDate());
        assertEquals(pojoWithOptional.getInteger(), copiedPojo.getInteger());
        assertEquals(pojoWithOptional.getString(), copiedPojo.getString());
        assertEquals(pojoWithOptional.getInner(), copiedPojo.getInner());

        assertEquals(copiedPojo, pojoWithOptional);
    }

    private <T> Set<T> toSet(T... values) {
        return Arrays.asList(values).stream().collect(Collectors.toSet());
    }

}