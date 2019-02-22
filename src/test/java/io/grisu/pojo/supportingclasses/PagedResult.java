package io.grisu.pojo.supportingclasses;

import java.util.List;

import io.grisu.pojo.AbstractPojo;
import io.grisu.pojo.annotations.Property;

public class PagedResult<T> extends AbstractPojo {

    @Property(name = "count")
    private Long count = 0L;

    @Property(name = "results")
    private List<T> results = null;

    public List<T> getResults() {
        return results;
    }

    public PagedResult<T> setResults(List<T> results) {
        this.results = results;
        return this;
    }

    public PagedResult<T> setCount(Long count) {
        this.count = count;
        return this;
    }

    public Long getCount() {
        return count;
    }
}
