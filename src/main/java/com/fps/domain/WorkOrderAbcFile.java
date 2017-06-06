package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkOrderAbcFile.
 */
@Entity
@Table(name = "work_order_abc_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workorderabcfile")
public class WorkOrderAbcFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "file_count")
    private Integer file_count;

    @Column(name = "file_size")
    private String file_size;

    @ManyToOne
    private WorkOrder workOrder;

    @ManyToOne
    private Lookups file_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFile_count() {
        return file_count;
    }

    public void setFile_count(Integer file_count) {
        this.file_count = file_count;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Lookups getFile_type() {
        return file_type;
    }

    public void setFile_type(Lookups lookups) {
        this.file_type = lookups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrderAbcFile workOrderAbcFile = (WorkOrderAbcFile) o;
        if(workOrderAbcFile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workOrderAbcFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkOrderAbcFile{" +
            "id=" + id +
            ", file_count='" + file_count + "'" +
            ", file_size='" + file_size + "'" +
            '}';
    }
}
