package io.grisu.pojo.supportingclasses;

import java.util.*;

import io.grisu.pojo.annotations.Property;
import io.grisu.pojo.AbstractPojo;

public class TestPojo<T> extends AbstractPojo {

   @Property(name = "string")
   private String string;

   @Property(name = "uuid")
   private UUID uuid;

   @Property(name = "date")
   private Date date;

   @Property(name = "number")
   private Long number;

   @Property(name = "userStatus")
   private UserStatus userStatus;

   @Property(name = "listOfStringByInterface")
   private List<String> listOfStringByInterface;

   @Property(name = "listOfStringByClass")
   private ArrayList<String> listOfStringByClass;

   @Property(name = "setOfStringByInterface")
   private Set<String> setOfStringByInterface;

   @Property(name = "setOfStringByClass")
   private HashSet<String> setOfStringByClass;

   @Property(name = "setOfEnumByInterface")
   private Set<UserStatus> setOfEnumByInterface;

   @Property(name = "setOfEnumByClass")
   private HashSet<UserStatus> setOfEnumByClass;

   @Property(name = "mapOfStringByInterface")
   private Map<String, Object> mapOfStringByInterface;

   @Property(name = "mapOfStringByClass")
   private LinkedHashMap<String, Object> mapOfStringByClass;

   @Property(name = "inner")
   private List<TestPojo> inner;

   @Property(name = "listOfType")
   private List<T> listOfType;

   public String getString() {
      return string;
   }

   public TestPojo setString(String property) {
      this.string = property;
      return this;
   }

   public UUID getUuid() {
      return uuid;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Long getNumber() {
      return number;
   }

   public void setNumber(Long number) {
      this.number = number;
   }

   public UserStatus getUserStatus() {
      return userStatus;
   }

   public void setUserStatus(UserStatus userStatus) {
      this.userStatus = userStatus;
   }

   public List<String> getListOfStringByInterface() {
      return listOfStringByInterface;
   }

   public void setListOfStringByInterface(List<String> listOfStringByInterface) {
      this.listOfStringByInterface = listOfStringByInterface;
   }

   public ArrayList<String> getListOfStringByClass() {
      return listOfStringByClass;
   }

   public void setListOfStringByClass(ArrayList<String> listOfStringByClass) {
      this.listOfStringByClass = listOfStringByClass;
   }

   public Set<String> getSetOfStringByInterface() {
      return setOfStringByInterface;
   }

   public void setSetOfStringByInterface(Set<String> setOfStringByInterface) {
      this.setOfStringByInterface = setOfStringByInterface;
   }

   public HashSet<String> getSetOfStringByClass() {
      return setOfStringByClass;
   }

   public void setSetOfStringByClass(HashSet<String> setOfStringByClass) {
      this.setOfStringByClass = setOfStringByClass;
   }

   public Set<UserStatus> getSetOfEnumByInterface() {
      return setOfEnumByInterface;
   }

   public void setSetOfEnumByInterface(Set<UserStatus> setOfEnumByInterface) {
      this.setOfEnumByInterface = setOfEnumByInterface;
   }

   public HashSet<UserStatus> getSetOfEnumByClass() {
      return setOfEnumByClass;
   }

   public void setSetOfEnumByClass(HashSet<UserStatus> setOfEnumByClass) {
      this.setOfEnumByClass = setOfEnumByClass;
   }

   public Map<String, Object> getMapOfStringByInterface() {
      return mapOfStringByInterface;
   }

   public void setMapOfStringByInterface(Map<String, Object> mapOfStringByInterface) {
      this.mapOfStringByInterface = mapOfStringByInterface;
   }

   public HashMap<String, Object> getMapOfStringByClass() {
      return mapOfStringByClass;
   }

   public void setMapOfStringByClass(LinkedHashMap<String, Object> mapOfStringByClass) {
      this.mapOfStringByClass = mapOfStringByClass;
   }

   public List<TestPojo> getInner() {
      return inner;
   }

   public void setInner(List<TestPojo> inner) {
      this.inner = inner;
   }

   public List<T> getListOfType() {
      return listOfType;
   }

   public void setListOfType(List<T> listOfType) {
      this.listOfType = listOfType;
   }
}
