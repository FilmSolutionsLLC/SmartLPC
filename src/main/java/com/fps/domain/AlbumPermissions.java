package com.fps.domain;

import javax.persistence.*;

@Entity
@Table(name = "album_permissions")
public class AlbumPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long album_id;
    private Long contact_id;
    private Integer permission;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(Long album_id) {
        this.album_id = album_id;
    }

    public Long getContact_id() {
        return contact_id;
    }

    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlbumPermissions(Long id, Long album_id, Long contact_id, Integer permission, String name) {
        this.id = id;
        this.album_id = album_id;
        this.contact_id = contact_id;
        this.permission = permission;
        this.name = name;
    }

    public AlbumPermissions() {
    }

    @Override
    public String toString() {
        return "AlbumPermissions{" +
            "id=" + id +
            ", album_id=" + album_id +
            ", contact_id=" + contact_id +
            ", permission=" + permission +
            ", name='" + name + '\'' +
            '}';
    }
}

