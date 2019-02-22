package io.grisu.pojo.supportingclasses;

import java.util.Map;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class ParameterizedPojo<T> extends AbstractPojo {

    @Property(name = "mapOfT")
    public Map<String, T> mapOfT;

}
