package com.testcompany.datalayer.models;


import android.os.Parcel;
import android.os.Parcelable;

public class PatientDisorder implements Parcelable {

    private String name;
    private String severity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.severity);
    }

    public PatientDisorder() {
    }

    protected PatientDisorder(Parcel in) {
        this.name = in.readString();
        this.severity = in.readString();
    }

    public static final Creator<PatientDisorder> CREATOR = new Creator<PatientDisorder>() {
        @Override
        public PatientDisorder createFromParcel(Parcel source) {
            return new PatientDisorder(source);
        }

        @Override
        public PatientDisorder[] newArray(int size) {
            return new PatientDisorder[size];
        }
    };
}
