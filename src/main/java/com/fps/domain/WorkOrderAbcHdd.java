package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkOrderAbcHdd.
 */
@Entity
@Table(name = "work_order_abc_hdd")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workorderabchdd")
public class WorkOrderAbcHdd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "size")
    private String size;

    @Column(name = "drive_number")
    private String drive_number;

    @ManyToOne
    @JsonIgnore
    private WorkOrder workOrder;

    @ManyToOne
    private Lookups hdd_to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDrive_number() {
        return drive_number;
    }

    public void setDrive_number(String drive_number) {
        this.drive_number = drive_number;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Lookups getHdd_to() {
        return hdd_to;
    }

    public void setHdd_to(Lookups lookups) {
        this.hdd_to = lookups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrderAbcHdd workOrderAbcHdd = (WorkOrderAbcHdd) o;
        if(workOrderAbcHdd.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workOrderAbcHdd.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkOrderAbcHdd{" +
            "id=" + id +
            ", size='" + size + "'" +
            ", drive_number='" + drive_number + "'" +
            '}';
    }
}
