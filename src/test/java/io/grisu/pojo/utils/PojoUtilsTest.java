package io.grisu.pojo.utils;

import java.util.*;

import io.grisu.pojo.supportingclasses.*;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class PojoUtilsTest {

   @Test
   public void shouldInstanceAPojoFromAnother() {
      UUID uuid = UUID.randomUUID();
      Date date = new Date();
      MyTestingClass fromEntity = new MyTestingClass().setKeyColumn(uuid).setCreatedAt(date);
      MyTestingClass instanced = PojoUtils.instancePojoFrom(MyTestingClass.class, fromEntity);

      Assert.assertEquals(uuid, instanced.getKeyColumn());
      Assert.assertEquals(date, instanced.getCreatedAt());
      TestCase.assertNull(instanced.getRoles());
      TestCase.assertNull(instanced.getValueColumn());
   }

   @Test
   public void shouldInstanceAPojoWithDifferentClassFromAnother() {
      UUID uuid = UUID.randomUUID();
      Date date = new Date();
      MyTestingClass fromEntity = new MyTestingClass().setKeyColumn(uuid).setCreatedAt(date);
      AnotherPojo instanced = PojoUtils.instancePojoFrom(AnotherPojo.class, fromEntity);

      Assert.assertEquals(uuid, instanced.getKeyProperty());
      TestCase.assertNull(instanced.getDummyVar());
   }

   @Test
   public void shouldInstanceAPojoWithWrapperInstanceClass() {
      UUID uuid = UUID.randomUUID();
      Date date = new Date();
      AnotherPojoAgain pojo = new AnotherPojoAgain().setDummyVar("dummy");

      MyTestingClassAgain fromEntity = new MyTestingClassAgain()
         .setKeyColumn(uuid)
         .setCreatedAt(date)
         .setPojo(pojo)
         .setListOfPojos(Arrays.asList(new AnotherPojoAgain().setDummyVar("a"), new AnotherPojoAgain().setDummyVar("b")));
      MyTestingClass instanced = PojoUtils.instancePojoFrom(MyTestingClass.class, fromEntity);

      Assert.assertEquals(AnotherPojo.class, instanced.getPojo().getClass());
      Assert.assertEquals("dummy", instanced.getPojo().getDummyVar());
      Assert.assertEquals(2, instanced.getListOfPojos().size());
      Assert.assertEquals(AnotherPojo.class, instanced.getListOfPojos().get(0).getClass());
      Assert.assertEquals("a", instanced.getListOfPojos().get(0).getDummyVar());
      Assert.assertEquals("b", instanced.getListOfPojos().get(1).getDummyVar());
   }

   @Test
   public void shouldCloneAPojo() {
      UUID uuid = UUID.randomUUID();
      Date date = new Date();
      List<TestPojo> innerPojos = Arrays.asList(new TestPojo().setString("inner"));

      TestPojo testPojo = new TestPojo();

      testPojo.setUuid(uuid);
      testPojo.setDate(date);
      testPojo.setInner(innerPojos);

      TestPojo clonePojo = PojoUtils.instancePojoFrom(TestPojo.class, testPojo);

      assertFalse(testPojo == clonePojo);
      Assert.assertEquals(testPojo, clonePojo);
   }

}
