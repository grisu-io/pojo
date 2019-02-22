package io.grisu.pojo.supportingclasses;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class MyTestingClassAgain extends AbstractPojo {

    @Property(name = "key")
    private UUID keyColumn;

    @Property(name = "value")
    private String valueColumn;

    @Property(name = "date")
    private Date createdAt;

    @Property(name = "roles", serializer = true)
    private List<Role> roles;

    @Property(name = "pojo")
    private AnotherPojoAgain pojo;

    @Property(name = "list")
    private List<AnotherPojoAgain> listOfPojos;

    public MyTestingClassAgain() {
    }

    public MyTestingClassAgain setKeyColumn(UUID key) {
        this.keyColumn = key;
        return this;
    }

    public UUID getKeyColumn() {
        return this.keyColumn;
    }

    public String getValueColumn() {
        return valueColumn;
    }

    public MyTestingClassAgain setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public MyTestingClassAgain setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public MyTestingClassAgain setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public String _rolesOut(List<Role> roles) {
        return roles.stream().map(Role::name).collect(Collectors.joining(","));
    }

    public List<Role> _rolesIn(String roles) {
        return Stream.of(roles.split(",")).map(Role::valueOf).collect(Collectors.toList());
    }

    public AnotherPojoAgain getPojo() {
        return pojo;
    }

    public MyTestingClassAgain setPojo(AnotherPojoAgain pojo) {
        this.pojo = pojo;
        return this;
    }

    public List<AnotherPojoAgain> getListOfPojos() {
        return listOfPojos;
    }

    public MyTestingClassAgain setListOfPojos(List<AnotherPojoAgain> listOfPojos) {
        this.listOfPojos = listOfPojos;
        return this;
    }

}
