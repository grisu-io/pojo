package io.grisu.pojo;

import io.grisu.pojo.supportingclasses.EnumTestPojoA;
import io.grisu.pojo.supportingclasses.EnumTestPojoB;
import io.grisu.pojo.supportingclasses.TestEnum;
import io.grisu.pojo.supportingclasses.TestEnum2;
import io.grisu.pojo.utils.PojoUtils;
import org.junit.Assert;
import org.junit.Test;

public class EnumTest {

    @Test
    public void shouldCopyTwoDifferentEnumTypes() {

        EnumTestPojoA pojoA = new EnumTestPojoA().setEnumeration(TestEnum.A);

        EnumTestPojoB pojoB = PojoUtils.instancePojoFrom(EnumTestPojoB.class, pojoA);

        Assert.assertEquals(TestEnum2.A, pojoB.getEnumeration2());
    }

}