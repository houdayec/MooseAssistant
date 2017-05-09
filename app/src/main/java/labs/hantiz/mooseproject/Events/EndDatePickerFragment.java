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

/**
 * Created by Corentin on 08/05/2017.
 */
public class EndDatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

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
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.setTime(defaultDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.d("end date", String.valueOf(year + " " + month + " " + day));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = sdf.format(new Date(year-1900, month, day));
        Log.d("end date formatted", endDate);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("endDate",endDate);
        editor.commit();

    }
}
