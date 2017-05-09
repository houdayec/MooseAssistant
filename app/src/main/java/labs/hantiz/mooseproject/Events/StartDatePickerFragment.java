package labs.hantiz.mooseproject.Events;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import labs.hantiz.mooseproject.R;

/**
 * Created by Corentin on 08/05/2017.
 */
public class StartDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Date defaultDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String defaultDateString = preferences.getString("startDate", "0");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            defaultDate = format.parse(defaultDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date formatted picker", String.valueOf(defaultDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(defaultDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Log.d("def date picker", String.valueOf(defaultDate.getYear() + "" + defaultDate.getMonth() + "" + defaultDate.getDay()));

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("start date", String.valueOf(year + " " + month + " " + dayOfMonth));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(new Date(year - 1900, month, dayOfMonth));
        Log.d("start date formatted", startDate);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("startDate",startDate);
        editor.commit();

    }
}
