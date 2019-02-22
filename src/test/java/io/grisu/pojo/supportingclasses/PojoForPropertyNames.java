package io.grisu.pojo.supportingclasses;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class PojoForPropertyNames extends AbstractPojo {

    @Property(name = "cool")
    private Boolean cool;

    @Property(name = "lessCool")
    private Boolean lessCool;

    public Boolean isCool() {
        return cool;
    }

    public PojoForPropertyNames setCool(Boolean cool) {
        this.cool = cool;
        return this;
    }

    public Boolean getLessCool() {
        return lessCool;
    }

    public PojoForPropertyNames setLessCool(Boolean lessCool) {
        this.lessCool = lessCool;
        return this;
    }

}
