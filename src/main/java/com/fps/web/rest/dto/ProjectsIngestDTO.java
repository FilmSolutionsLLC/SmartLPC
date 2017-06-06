package com.fps.web.rest.dto;

/**
 * Created by macbookpro on 5/15/17.
 */
public class ProjectsIngestDTO {
    private Long id;
    private String name;
    private String images_location;
    private String images_location_remote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImages_location_remote() {
        return images_location_remote;
    }

    public void setImages_location_remote(String images_location_remote) {
        this.images_location_remote = images_location_remote;
    }

    public String getImages_location() {
        return images_location;
    }

    public void setImages_location(String images_location) {
        this.images_location = images_location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectsIngestDTO(Long id, String name, String images_location, String images_location_remote) {
        this.id = id;
        this.name = name;
        this.images_location = images_location;
        this.images_location_remote = images_location_remote;
    }

    public ProjectsIngestDTO() {
    }

    @Override
    public String toString() {
        return "ProjectsIngestDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", images_location='" + images_location + '\'' +
            ", images_location_remote='" + images_location_remote + '\'' +
            '}';
    }
}
