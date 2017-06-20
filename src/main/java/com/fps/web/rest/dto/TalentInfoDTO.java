package com.fps.web.rest.dto;

/**
 * Created by macbookpro on 6/16/17.
 */
public class TalentInfoDTO {

    Long id;
    String type;
    String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TalentInfoDTO(Long id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public TalentInfoDTO() {
    }

    @Override
    public String toString() {
        return "TalentInfoDTO{" +
            "id=" + id +
            ", type='" + type + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
