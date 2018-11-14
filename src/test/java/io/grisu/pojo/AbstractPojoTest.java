package io.grisu.pojo;

import java.util.UUID;

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
}
