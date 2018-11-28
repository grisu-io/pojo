package io.grisu.pojo.supportingclasses;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class EnumTestPojoA extends AbstractPojo {

    @Property(name = "property")
    private TestEnum enumeration;

    public TestEnum getEnumeration() {
        return enumeration;
    }

    public EnumTestPojoA setEnumeration(TestEnum enumeration) {
        this.enumeration = enumeration;
        return this;
    }
}
