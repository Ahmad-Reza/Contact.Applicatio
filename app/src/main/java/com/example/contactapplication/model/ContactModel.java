package com.example.contactapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactModel implements Parcelable {
    private int id;
    private String name;
    private String number;

    public ContactModel(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    protected ContactModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        number = in.readString();
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel in) {
            return new ContactModel(in);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setName(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(number);
    }
}
