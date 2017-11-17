package com.fps.domain;


import javax.persistence.*;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "album_owner")
    private String album_owner;

    @Column(name = "album_name")
    private String album_name;

    @Column(name = "album_permissions")
    private String album_permissions;

    @Column(name = "album_descriptions")
    private String album_descriptions;

    @Column(name = "album_type")
    private Integer album_type;

    @Column(name = "parent_id")
    private Long parent_id;

    @ManyToOne
    private Projects project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbum_owner() {
        return album_owner;
    }

    public void setAlbum_owner(String album_owner) {
        this.album_owner = album_owner;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_permissions() {
        return album_permissions;
    }

    public void setAlbum_permissions(String album_permissions) {
        this.album_permissions = album_permissions;
    }

    public String getAlbum_descriptions() {
        return album_descriptions;
    }

    public void setAlbum_descriptions(String album_descriptions) {
        this.album_descriptions = album_descriptions;
    }

    public Integer getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(Integer album_type) {
        this.album_type = album_type;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
    }
}
