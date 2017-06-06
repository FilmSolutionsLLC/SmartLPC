package com.fps.web.rest.dto;

import com.fps.domain.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by macbookpro on 1/6/17.
 */
public class WorkOrderDTO {

    private WorkOrder workOrder;
    private Set<WorkOrderAbcFile> workOrderAbcFiles = new HashSet<>();
    private Set<WorkOrderAbcHdd> workOrderAbcHdds = new HashSet<>();
    private Set<WorkOrdersAdminRelation> workOrdersAdminRelations = new HashSet<>();

    public WorkOrderDTO() {
    }

    public WorkOrderDTO(WorkOrder workOrder, Set<WorkOrderAbcFile> workOrderAbcFiles, Set<WorkOrderAbcHdd> workOrderAbcHdds, Set<WorkOrdersAdminRelation> workOrdersAdminRelations) {
        this.workOrder = workOrder;
        this.workOrderAbcFiles = workOrderAbcFiles;
        this.workOrderAbcHdds = workOrderAbcHdds;
        this.workOrdersAdminRelations = workOrdersAdminRelations;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Set<WorkOrderAbcFile> getWorkOrderAbcFiles() {
        return workOrderAbcFiles;
    }

    public void setWorkOrderAbcFiles(Set<WorkOrderAbcFile> workOrderAbcFiles) {
        this.workOrderAbcFiles = workOrderAbcFiles;
    }

    public Set<WorkOrderAbcHdd> getWorkOrderAbcHdds() {
        return workOrderAbcHdds;
    }

    public void setWorkOrderAbcHdds(Set<WorkOrderAbcHdd> workOrderAbcHdds) {
        this.workOrderAbcHdds = workOrderAbcHdds;
    }

    public Set<WorkOrdersAdminRelation> getWorkOrdersAdminRelations() {
        return workOrdersAdminRelations;
    }

    public void setWorkOrdersAdminRelations(Set<WorkOrdersAdminRelation> workOrdersAdminRelations) {
        this.workOrdersAdminRelations = workOrdersAdminRelations;
    }

    @Override
    public String toString() {
        return "WorkOrderDTO{" +
            "workOrder=" + workOrder +
            ", workOrderAbcFiles=" + workOrderAbcFiles +
            ", workOrderAbcHdds=" + workOrderAbcHdds +
            ", workOrdersAdminRelations=" + workOrdersAdminRelations +
            '}';
    }
}
