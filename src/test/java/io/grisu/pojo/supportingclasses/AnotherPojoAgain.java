package io.grisu.pojo.supportingclasses;

import java.util.UUID;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class AnotherPojoAgain extends AbstractPojo {

    @Property(name = "key")
    private UUID keyProperty;

    @Property(name = "dummy")
    private String dummyVar;

    public UUID getKeyProperty() {
        return keyProperty;
    }

    public void setKeyProperty(UUID key) {
        this.keyProperty = key;
    }

    public String getDummyVar() {
        return dummyVar;
    }

    public AnotherPojoAgain setDummyVar(String dummyVar) {
        this.dummyVar = dummyVar;
        return this;
    }
}
