package io.grisu.pojo.supportingclasses;

import java.util.Map;

import io.grisu.pojo.annotations.Property;
import io.grisu.pojo.AbstractPojo;

public class ParameterizedPojo<T> extends AbstractPojo {

   @Property(name = "mapOfT")
   public Map<String, T> mapOfT;

}
