package com.example.donation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DonationHistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DonationRecord> donationRecords;

    public DonationHistoryAdapter(Context context, ArrayList<DonationRecord> donationRecords) {
        this.context = context;
        this.donationRecords = donationRecords;
    }

    @Override
    public int getCount() {
        return donationRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return donationRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_donation_record, parent, false);
        }

        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvUnits = convertView.findViewById(R.id.tvUnits);

        DonationRecord record = donationRecords.get(position);
        tvDate.setText(record.getDate());
        tvUnits.setText(String.valueOf(record.getUnits()));

        return convertView;
    }
}
