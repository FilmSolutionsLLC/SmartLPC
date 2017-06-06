package com.fps.web.rest.dto;

import com.fps.domain.User;

/**
 * Created by macbookpro on 1/8/17.
 */
public class AdminRelationDTO {


    private User admin_user;
    private Integer relation_type;

    public User getAdmin_user() {
        return admin_user;
    }

    public void setAdmin_user(User admin_user) {
        this.admin_user = admin_user;
    }

    public Integer getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(Integer relation_type) {
        this.relation_type = relation_type;
    }

    public AdminRelationDTO() {
    }

    public AdminRelationDTO(User admin_user, Integer relation_type) {
        this.admin_user = admin_user;
        this.relation_type = relation_type;
    }

    @Override
    public String toString() {
        return "AdminRelationDTO{" +
            "admin_user=" + admin_user +
            ", relation_type=" + relation_type +
            '}';
    }
}
