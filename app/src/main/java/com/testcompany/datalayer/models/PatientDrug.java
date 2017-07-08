package com.testcompany.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;


public class PatientDrug implements Parcelable {

    private String drug_name;

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int getDrug_date_updated() {
        return drug_date_updated;
    }

    public void setDrug_date_updated(int drug_date_updated) {
        this.drug_date_updated = drug_date_updated;
    }

    private int dosage;
    private int drug_date_updated;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.drug_name);
        dest.writeInt(this.dosage);
        dest.writeInt(this.drug_date_updated);
    }

    public PatientDrug() {
    }

    protected PatientDrug(Parcel in) {
        this.drug_name = in.readString();
        this.dosage = in.readInt();
        this.drug_date_updated = in.readInt();
    }

    public static final Creator<PatientDrug> CREATOR = new Creator<PatientDrug>() {
        @Override
        public PatientDrug createFromParcel(Parcel source) {
            return new PatientDrug(source);
        }

        @Override
        public PatientDrug[] newArray(int size) {
            return new PatientDrug[size];
        }
    };
}
