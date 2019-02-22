package io.grisu.pojo;

import java.util.Date;
import java.util.UUID;

import io.grisu.pojo.supportingclasses.AnotherPojo;
import io.grisu.pojo.supportingclasses.MyTestingClass;
import io.grisu.pojo.supportingclasses.PojoForPropertyNames;
import io.grisu.pojo.utils.PojoUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractPojoTest {

    @Test
    public void getFieldTypes() {
        MyTestingClass myTestingClass = new MyTestingClass();
        Assert.assertEquals(UUID.class, myTestingClass.__getTypeOf("key"));
        Assert.assertEquals(String.class, myTestingClass.__getTypeOf("value"));
    }

    @Test
    public void shouldTwoEntitiesBeEqual() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = new MyTestingClass().setKeyColumn(key).setValueColumn("value");
        MyTestingClass b = new MyTestingClass().setKeyColumn(key).setValueColumn("value");
        assertTrue(a.equals(b));
    }

    @Test
    public void shouldTwoEntitiesNotEqual() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = new MyTestingClass().setKeyColumn(key).setValueColumn("value");
        MyTestingClass b = new MyTestingClass().setKeyColumn(key).setValueColumn("eulav");
        assertFalse(a.equals(b));
    }

    @Test
    public void shouldEntityNotEqualToNull() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = new MyTestingClass().setKeyColumn(key).setValueColumn("value");
        assertFalse(a.equals(null));
    }

    @Test
    public void shouldNotReturnAnyChangedProperty() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = new MyTestingClass();
        Assert.assertEquals(0, PojoUtils.changedProperties(a).size());
    }

    @Test
    public void shouldReturnOneChangedProperty() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = (MyTestingClass) ((MyTestingClass) new MyTestingClass().put("key", key)).put("value", "value");
        a.__resetHashes();
        a.setValueColumn("new value");
        Assert.assertEquals(1, PojoUtils.changedProperties(a).size());
        Assert.assertEquals("value", PojoUtils.changedProperties(a).toArray()[0]);
    }

    @Test
    public void shouldReportChangedPropertiesThroughSetters() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = new MyTestingClass().setKeyColumn(key).setValueColumn("value");
        Assert.assertEquals(2, PojoUtils.changedProperties(a).size());
    }

    @Test
    public void shouldResetChangedProperties() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = (MyTestingClass) new MyTestingClass().put("key", key);
        a.setValueColumn("new value");
        a.__resetHashes();
        Assert.assertEquals(0, PojoUtils.changedProperties(a).size());
    }

    @Test
    public void shouldAccessABooleanFromIsProperty() {
        PojoForPropertyNames pojoForPropertyNames = new PojoForPropertyNames().setCool(true);
        Assert.assertTrue((Boolean) pojoForPropertyNames.get("cool"));
    }

    @Test
    public void shouldAccessABooleanFromGetter() {
        PojoForPropertyNames pojoForPropertyNames = new PojoForPropertyNames().setLessCool(true);
        Assert.assertTrue((Boolean) pojoForPropertyNames.get("lessCool"));
    }

    @Test
    public void shouldTestEquality() {
        Assert.assertEquals(new MyTestingClass(), new MyTestingClass());

        final UUID uuid = UUID.randomUUID();
        Assert.assertEquals(new MyTestingClass().setKeyColumn(uuid), new MyTestingClass().setKeyColumn(uuid));
        Assert.assertNotEquals(new MyTestingClass().setKeyColumn(uuid), new MyTestingClass().setKeyColumn(UUID.randomUUID()));

        final String string = "test String";
        Assert.assertEquals(new MyTestingClass().setValueColumn(string), new MyTestingClass().setValueColumn(string));
        Assert.assertNotEquals(new MyTestingClass().setValueColumn(string), new MyTestingClass().setValueColumn("another"));

        final AnotherPojo anotherPojo = new AnotherPojo().setDummyVar("dd");
        final AnotherPojo anotherPojo2 = new AnotherPojo().setDummyVar("dd");
        Assert.assertEquals(new MyTestingClass().setPojo(anotherPojo), new MyTestingClass().setPojo(anotherPojo2));
        Assert.assertNotEquals(new MyTestingClass().setPojo(anotherPojo), new AnotherPojo());

        final Date date = new Date();
        final Date date2 = new Date();
        date2.setTime(1);
        Assert.assertEquals(new MyTestingClass().setCreatedAt(date), new MyTestingClass().setCreatedAt(date));
        Assert.assertNotEquals(new MyTestingClass().setCreatedAt(date), new MyTestingClass().setCreatedAt(date2));
    }

    @Test
    public void shouldTestHashCodeForNulls() {
        final MyTestingClass myTestingClass = new MyTestingClass().setValueColumn("v");
        Assert.assertNotNull(myTestingClass.hashCode());
    }

}
