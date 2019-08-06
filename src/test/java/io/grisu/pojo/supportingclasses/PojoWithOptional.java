package io.grisu.pojo.supportingclasses;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class PojoWithOptional extends AbstractPojo {

    @Property(name = "string")
    private Optional<String> string;

    @Property(name = "integer")
    private Optional<Integer> integer;

    @Property(name = "date")
    private Optional<LocalDate> date;

    @Property(name = "boolean")
    private Optional<Boolean> bool;

    @Property(name = "inner")
    private Optional<List<SimpleOptionalPojo>> inner;

    public Optional<String> getString() {
        return string;
    }

    public PojoWithOptional setString(Optional<String> string) {
        this.string = string;
        return this;
    }

    public Optional<Integer> getInteger() {
        return integer;
    }

    public PojoWithOptional setInteger(Optional<Integer> integer) {
        this.integer = integer;
        return this;
    }

    public Optional<LocalDate> getDate() {
        return date;
    }

    public PojoWithOptional setDate(Optional<LocalDate> date) {
        this.date = date;
        return this;
    }

    public Optional<Boolean> getBool() {
        return bool;
    }

    public PojoWithOptional setBool(Optional<Boolean> bool) {
        this.bool = bool;
        return this;
    }

    public Optional<List<SimpleOptionalPojo>> getInner() {
        return inner;
    }

    public PojoWithOptional setInner(Optional<List<SimpleOptionalPojo>> inner) {
        this.inner = inner;
        return this;
    }

}
