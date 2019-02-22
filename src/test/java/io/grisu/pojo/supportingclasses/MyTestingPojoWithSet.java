package io.grisu.pojo.supportingclasses;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class MyTestingPojoWithSet extends AbstractPojo {

    @Property(name = "myCollection", serializer = true, serializerClass = Set.class)
    private Collection<TestEnum> myCollection;

    public Collection<TestEnum> getMyCollection() {
        return myCollection;
    }

    public MyTestingPojoWithSet setMyCollection(Collection<TestEnum> myCollection) {
        this.myCollection = new HashSet<TestEnum>(myCollection);
        return this;
    }

    public Set<String> _myCollectionOut(Collection<TestEnum> enums) {
        return enums.stream().map(e -> e.name()).collect(Collectors.toSet());
    }

    public Collection<TestEnum> _myCollectionIn(Set<String> enums) {
        return enums.stream().map(e -> TestEnum.valueOf(e)).collect(Collectors.toList());
    }

}
