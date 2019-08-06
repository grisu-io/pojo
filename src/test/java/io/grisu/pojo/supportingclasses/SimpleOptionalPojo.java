package io.grisu.pojo.supportingclasses;

import java.util.Optional;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class SimpleOptionalPojo extends AbstractPojo {

    @Property(name = "key")
    private Optional<String> key;

    public Optional<String> getKey() {
        return key;
    }

    public SimpleOptionalPojo setKey(Optional<String> key) {
        this.key = key;
        return this;
    }

}