package com.mellis.expenseapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mellis on 6/20/2017.
 */

public class Expense implements Parcelable {
    String name;
    String date;
    String amount;

    public Expense(String name, String amount, String date) {
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object obj) {
        Expense e2 = (Expense)obj;
        if(this.getName().equals(e2.getName())
            &&this.getAmount().equals(e2.getAmount())
            &&this.getDate().equals(e2.getDate())){
            return true;
        }
        return false;
    }

    protected Expense(Parcel in) {
        name = in.readString();
        date = in.readString();
        amount = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(amount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}