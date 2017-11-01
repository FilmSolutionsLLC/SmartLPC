package com.fps.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkOrdersAdminRelation.
 */
@Entity
@Table(name = "work_orders_admin_relation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workordersadminrelation")
public class WorkOrdersAdminRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnore
    private WorkOrder workOrder;

    @ManyToOne
    private User admin_user;

    @ManyToOne
    private RelationType relation_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public User getAdmin_user() {
        return admin_user;
    }

    public void setAdmin_user(User user) {
        this.admin_user = user;
    }

    public RelationType getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(RelationType relationType) {
        this.relation_type = relationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrdersAdminRelation workOrdersAdminRelation = (WorkOrdersAdminRelation) o;
        if (workOrdersAdminRelation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workOrdersAdminRelation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkOrdersAdminRelation{" +
            "id=" + id +
            ", workOrder=" + workOrder +
            ", admin_user=" + admin_user +
            ", relation_type=" + relation_type +
            '}';
    }
}
