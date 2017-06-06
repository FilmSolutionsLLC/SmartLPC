package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Captions.
 */
@Entity
@Table(name = "captions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "captions")
public class Captions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "caption_text")
    private String captionText;

    @Column(name = "caption_dttm")
    private ZonedDateTime captionDttm;

    @ManyToOne
    private Projects project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaptionText() {
        return captionText;
    }

    public void setCaptionText(String captionText) {
        this.captionText = captionText;
    }

    public ZonedDateTime getCaptionDttm() {
        return captionDttm;
    }

    public void setCaptionDttm(ZonedDateTime captionDttm) {
        this.captionDttm = captionDttm;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Captions captions = (Captions) o;
        if(captions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, captions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Captions{" +
            "id=" + id +
            ", captionText='" + captionText + "'" +
            ", captionDttm='" + captionDttm + "'" +
            '}';
    }
}
