package com.testcompany.datalayer.models;

import java.util.List;

public class PatientData {

    private List<PatientModel> patient_list = null;
    private int total_count;
    private int current_page;
    private int page_offset;

    public List<PatientModel> getPatient_list() {
        return patient_list;
    }

    public void setPatient_list(List<PatientModel> patient_list) {
        this.patient_list = patient_list;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPage_offset() {
        return page_offset;
    }

    public void setPage_offset(int page_offset) {
        this.page_offset = page_offset;
    }

}
