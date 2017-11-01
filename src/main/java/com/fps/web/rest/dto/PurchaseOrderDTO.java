package com.fps.web.rest.dto;

public class PurchaseOrderDTO {

        private String po_number;
        private String po_notes;

        public String getPo_number() {
            return po_number;
        }

        public void setPo_number(String po_number) {
            this.po_number = po_number;
        }

        public String getPo_notes() {
            return po_notes;
        }

        public void setPo_notes(String po_notes) {
            this.po_notes = po_notes;
        }

        public PurchaseOrderDTO(String po_number, String po_notes) {
            this.po_number = po_number;
            this.po_notes = po_notes;
        }

        public PurchaseOrderDTO() {
        }
    }

