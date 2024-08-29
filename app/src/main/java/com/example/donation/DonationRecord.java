package com.example.donation;

public class DonationRecord {
    private String date;
    private int units;

    public DonationRecord(String date, int units) {
        this.date = date;
        this.units = units;
    }

    public String getDate() {
        return date;
    }

    public int getUnits() {
        return units;
    }
}
