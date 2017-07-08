package com.testcompany.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class PatientModel implements Parcelable {

    private int patient_id;
    private String patient_name;
    private int patient_age;
    private String patient_sex;
    private int patient_date_created;
    private int patient_date_updated;

    private boolean isUnderAge = false;
    private boolean hasDisorder = false;
    private boolean hasDrug = false;
    private List<PatientDisorder> patient_disorders = null;
    private List<PatientDrug> patient_drugs = null;
    private int percentage;
    private boolean isBookmarked = false;
    private String DisorderString = "";

    public boolean isUnderAge() {
        return isUnderAge;
    }

    public void setUnderAge(boolean underAge) {
        isUnderAge = underAge;
    }

    public boolean isHasDisorder() {
        return hasDisorder;
    }

    public void setHasDisorder(boolean hasDisorder) {
        this.hasDisorder = hasDisorder;
    }

    public boolean isHasDrug() {
        return hasDrug;
    }

    public void setHasDrug(boolean hasDrug) {
        this.hasDrug = hasDrug;
    }

    public static Creator<PatientModel> getCREATOR() {
        return CREATOR;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public int getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(int patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatient_sex() {
        return patient_sex;
    }

    public void setPatient_sex(String patient_sex) {
        this.patient_sex = patient_sex;
    }

    public int getPatient_date_created() {
        return patient_date_created;
    }

    public void setPatient_date_created(int patient_date_created) {
        this.patient_date_created = patient_date_created;
    }

    public int getPatient_date_updated() {
        return patient_date_updated;
    }

    public void setPatient_date_updated(int patient_date_updated) {
        this.patient_date_updated = patient_date_updated;
    }

    public List<PatientDisorder> getPatient_disorders() {
        return patient_disorders;
    }

    public void setPatient_disorders(List<PatientDisorder> patient_disorders) {
        this.patient_disorders = patient_disorders;
    }

    public List<PatientDrug> getPatient_drugs() {
        return patient_drugs;
    }

    public void setPatient_drugs(List<PatientDrug> patient_drugs) {
        this.patient_drugs = patient_drugs;
    }


    public PatientModel() {
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getDisorderString() {
        return DisorderString;
    }

    public void setDisorderString(String disorderString) {
        DisorderString = disorderString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.patient_id);
        dest.writeString(this.patient_name);
        dest.writeInt(this.patient_age);
        dest.writeString(this.patient_sex);
        dest.writeInt(this.patient_date_created);
        dest.writeInt(this.patient_date_updated);
        dest.writeByte(this.isUnderAge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDisorder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDrug ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.patient_disorders);
        dest.writeTypedList(this.patient_drugs);
        dest.writeInt(this.percentage);
        dest.writeByte(this.isBookmarked ? (byte) 1 : (byte) 0);
        dest.writeString(this.DisorderString);
    }

    protected PatientModel(Parcel in) {
        this.patient_id = in.readInt();
        this.patient_name = in.readString();
        this.patient_age = in.readInt();
        this.patient_sex = in.readString();
        this.patient_date_created = in.readInt();
        this.patient_date_updated = in.readInt();
        this.isUnderAge = in.readByte() != 0;
        this.hasDisorder = in.readByte() != 0;
        this.hasDrug = in.readByte() != 0;
        this.patient_disorders = in.createTypedArrayList(PatientDisorder.CREATOR);
        this.patient_drugs = in.createTypedArrayList(PatientDrug.CREATOR);
        this.percentage = in.readInt();
        this.isBookmarked = in.readByte() != 0;
        this.DisorderString = in.readString();
    }

    public static final Creator<PatientModel> CREATOR = new Creator<PatientModel>() {
        @Override
        public PatientModel createFromParcel(Parcel source) {
            return new PatientModel(source);
        }

        @Override
        public PatientModel[] newArray(int size) {
            return new PatientModel[size];
        }
    };
}
