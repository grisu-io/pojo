package io.grisu.pojo;

import java.util.Arrays;

import io.grisu.pojo.supportingclasses.MyTestingClass;
import io.grisu.pojo.utils.PojoUtils;
import org.junit.Assert;
import org.junit.Test;

public class ProxyTest {

    @Test
    public void shouldTestForNullValue() {
        final MyTestingClass myTestingClass = AbstractPojo.instance(MyTestingClass.class).setValueColumn(null);
        Assert.assertTrue(myTestingClass.__hasChanged("value"));
        Assert.assertEquals(1, PojoUtils.changedProperties(myTestingClass).size());
        Assert.assertEquals(Arrays.asList("value"), PojoUtils.changedProperties(myTestingClass));
    }

}