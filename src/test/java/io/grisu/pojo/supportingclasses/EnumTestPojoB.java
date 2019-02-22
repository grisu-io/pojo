package io.grisu.pojo.supportingclasses;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class EnumTestPojoB extends AbstractPojo {

    @Property(name = "property", serializer = true, serializerClass = TestEnum.class)
    private TestEnum2 enumeration2;

    public TestEnum2 getEnumeration2() {
        return enumeration2;
    }

    public EnumTestPojoB setEnumeration2(TestEnum2 enumeration) {
        this.enumeration2 = enumeration;
        return this;
    }

    public TestEnum2 _propertyIn(TestEnum testEnum) {
        return TestEnum2.valueOf(testEnum.name());
    }

}
