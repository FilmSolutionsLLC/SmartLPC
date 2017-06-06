package com.fps.web.rest.dto;

import java.util.List;

/**
 * Created by macbookpro on 3/24/17.
 */
public class GroupWorkOrderDTO {
    private String owner;

    private List<WorkOrderListDTO> workOrderListDTOs;

    public List<WorkOrderListDTO> getWorkOrderListDTOs() {
        return workOrderListDTOs;
    }

    public void setWorkOrderListDTOs(List<WorkOrderListDTO> workOrderListDTOs) {
        this.workOrderListDTOs = workOrderListDTOs;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "GroupWorkOrder{" +
            "owner='" + owner + '\'' +
            ", workOrderListDTOs=" + workOrderListDTOs +
            '}';
    }
}
