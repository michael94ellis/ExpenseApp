package com.mellis.expenseapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Mellis on 6/23/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DateSelectionCallback callback;

    public interface DateSelectionCallback{
        public void dispatchNewDate(String date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DateSelectionCallback) {
            callback = (DateSelectionCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DateSelectionCallback");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        callback.dispatchNewDate(String.format("%02d",month) + "/" + String.format("%02d",day) + "/" + year);
    }
}