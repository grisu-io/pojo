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
        MyTestingClass myTestingClass = AbstractPojo.instance(MyTestingClass.class);
        Assert.assertEquals(UUID.class, myTestingClass.__getTypeOf("key"));
        Assert.assertEquals(String.class, myTestingClass.__getTypeOf("value"));
    }

    @Test
    public void shouldTwoEntitiesBeEqual() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = AbstractPojo.instance(MyTestingClass.class).setKeyColumn(key).setValueColumn("value");
        MyTestingClass b = AbstractPojo.instance(MyTestingClass.class).setKeyColumn(key).setValueColumn("value");
        assertTrue(a.equals(b));
    }

    @Test
    public void shouldTwoEntitiesNotEqual() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = AbstractPojo.instance(MyTestingClass.class).setKeyColumn(key).setValueColumn("value");
        MyTestingClass b = AbstractPojo.instance(MyTestingClass.class).setKeyColumn(key).setValueColumn("eulav");
        assertFalse(a.equals(b));
    }

    @Test
    public void shouldEntityNotEqualToNull() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = AbstractPojo.instance(MyTestingClass.class).setKeyColumn(key).setValueColumn("value");
        assertFalse(a.equals(null));
    }

    @Test
    public void shouldNotReturnAnyChangedProperty() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = AbstractPojo.instance(MyTestingClass.class);
        Assert.assertEquals(0, PojoUtils.changedProperties(a).size());
    }

    @Test
    public void shouldReturnOneChangedProperty() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = (MyTestingClass) ((MyTestingClass) AbstractPojo.instance(MyTestingClass.class).put("key", key)).put("value", "value");
        a.__resetHashes();
        a.setValueColumn("new value");
        Assert.assertEquals(1, PojoUtils.changedProperties(a).size());
        Assert.assertEquals("value", PojoUtils.changedProperties(a).toArray()[0]);
    }

    @Test
    public void shouldReportChangedPropertiesThroughSetters() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = AbstractPojo.instance(MyTestingClass.class).setKeyColumn(key).setValueColumn("value");
        Assert.assertEquals(2, PojoUtils.changedProperties(a).size());
    }

    @Test
    public void shouldResetChangedProperties() {
        final UUID key = UUID.randomUUID();
        MyTestingClass a = (MyTestingClass) AbstractPojo.instance(MyTestingClass.class).put("key", key);
        a.setValueColumn("new value");
        a.__resetHashes();
        Assert.assertEquals(0, PojoUtils.changedProperties(a).size());
    }

    @Test
    public void shouldAccessABooleanFromIsProperty() {
        PojoForPropertyNames pojoForPropertyNames = AbstractPojo.instance(PojoForPropertyNames.class).setCool(true);
        Assert.assertTrue((Boolean) pojoForPropertyNames.get("cool"));
    }

    @Test
    public void shouldAccessABooleanFromGetter() {
        PojoForPropertyNames pojoForPropertyNames = AbstractPojo.instance(PojoForPropertyNames.class).setLessCool(true);
        Assert.assertTrue((Boolean) pojoForPropertyNames.get("lessCool"));
    }

    @Test
    public void shouldTestEquality() {
        Assert.assertEquals(AbstractPojo.instance(MyTestingClass.class), AbstractPojo.instance(MyTestingClass.class));

        final UUID uuid = UUID.randomUUID();
        Assert.assertEquals(AbstractPojo.instance(MyTestingClass.class).setKeyColumn(uuid), AbstractPojo.instance(MyTestingClass.class).setKeyColumn(uuid));
        Assert.assertNotEquals(AbstractPojo.instance(MyTestingClass.class).setKeyColumn(uuid), AbstractPojo.instance(MyTestingClass.class).setKeyColumn(UUID.randomUUID()));

        final String string = "test String";
        Assert.assertEquals(AbstractPojo.instance(MyTestingClass.class).setValueColumn(string), AbstractPojo.instance(MyTestingClass.class).setValueColumn(string));
        Assert.assertNotEquals(AbstractPojo.instance(MyTestingClass.class).setValueColumn(string), AbstractPojo.instance(MyTestingClass.class).setValueColumn("another"));

        final AnotherPojo anotherPojo = AbstractPojo.instance(AnotherPojo.class).setDummyVar("dd");
        final AnotherPojo anotherPojo2 = AbstractPojo.instance(AnotherPojo.class).setDummyVar("dd");
        Assert.assertEquals(AbstractPojo.instance(MyTestingClass.class).setPojo(anotherPojo), AbstractPojo.instance(MyTestingClass.class).setPojo(anotherPojo2));
        Assert.assertNotEquals(AbstractPojo.instance(MyTestingClass.class).setPojo(anotherPojo), AbstractPojo.instance(AnotherPojo.class));

        final Date date = new Date();
        final Date date2 = new Date();
        date2.setTime(1);
        Assert.assertEquals(AbstractPojo.instance(MyTestingClass.class).setCreatedAt(date), AbstractPojo.instance(MyTestingClass.class).setCreatedAt(date));
        Assert.assertNotEquals(AbstractPojo.instance(MyTestingClass.class).setCreatedAt(date), AbstractPojo.instance(MyTestingClass.class).setCreatedAt(date2));
    }

    @Test
    public void shouldTestHashCodeForNulls() {
        final MyTestingClass myTestingClass = AbstractPojo.instance(MyTestingClass.class).setValueColumn("v");
        Assert.assertNotNull(myTestingClass.hashCode());
    }

}
