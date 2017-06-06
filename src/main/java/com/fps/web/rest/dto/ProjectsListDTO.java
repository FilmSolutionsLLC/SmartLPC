package com.fps.web.rest.dto;

/**
 * Created by macbookpro on 2/9/17.
 */
public class ProjectsListDTO {
    Long id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectsListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProjectsListDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
