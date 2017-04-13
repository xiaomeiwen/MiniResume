package com.xiaomeiw.resume.miniresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wenxiaomei on 17/4/12.
 */

public class Experience implements Parcelable {

    public String id;
    public String company;
    public String title;
    public Date startDate;
    public Date endDate;

    public List<String> experiences;

    public Experience() {
        id = UUID.randomUUID().toString();
    }

    protected Experience(Parcel in) {
        id = in.readString();
        company = in.readString();
        title = in.readString();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        experiences = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(company);
        dest.writeString(title);
        dest.writeLong(startDate.getTime());
        dest.writeLong(endDate.getTime());
        dest.writeStringList(experiences);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Experience> CREATOR = new Creator<Experience>() {
        @Override
        public Experience createFromParcel(Parcel in) {
            return new Experience(in);
        }

        @Override
        public Experience[] newArray(int size) {
            return new Experience[size];
        }
    };
}

