package com.fps.service.util;

/**
 * Created by macbookpro on 2/16/17.
 */

public class Report {
    Long id;
    String name;
    String query;
    Integer report_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReport_type() {
        return report_type;
    }

    public void setReport_type(Integer report_type) {
        this.report_type = report_type;
    }

    @Override
    public String toString() {
        return "Report{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", query='" + query + '\'' +
            ", report_type=" + report_type +
            '}';
    }
}
