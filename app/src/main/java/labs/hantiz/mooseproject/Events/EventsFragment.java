package labs.hantiz.mooseproject.Events;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import labs.hantiz.mooseproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements View.OnClickListener{

    private List<Event> passedListEvents = new ArrayList<>();
    private EventAdapter eventAdapter;
    private RecyclerView eventList;
    private Button buttonStartDate;
    private Button buttonEndDate;
    private ImageButton buttonSearchEvent;
    private FragmentManager fragManager;
    private String startDate;
    private String endDate;
    private SwipeRefreshLayout srl;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

            Log.d("shared pref list", key);
            SharedPreferences sp =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            switch(key){
                case "startDate":
                    buttonStartDate.setText(sp.getString("startDate", "Today "));
                    break;
                case "endDate":
                    buttonEndDate.setText(sp.getString("endDate", "Select end date"));
                    break;
            }

        }
    };

    public EventsFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        //Setting the title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Events");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(listener);

        //Get component from view
        buttonStartDate = (Button) rootView.findViewById(R.id.buttonStartDate);
        buttonStartDate.setOnClickListener(this);
        buttonStartDate.setStateListAnimator(null);

        buttonEndDate = (Button) rootView.findViewById(R.id.buttonEndDate);
        buttonEndDate.setOnClickListener(this);
        buttonEndDate.setStateListAnimator(null);

        buttonSearchEvent = (ImageButton) rootView.findViewById(R.id.buttonSearchEvent);
        buttonSearchEvent.setOnClickListener(this);
        buttonSearchEvent.setStateListAnimator(null);

        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                final Runnable r = new Runnable()
                {
                    public void run()
                    {
                        if(checkInternetConnection()){
                            searchEvent();
                        }
                        srl.setRefreshing(false);
                    }
                };

                handler.postDelayed(r, 2500);

            }
        });

        checkInternetConnection();

        if(getArguments() != null)
            passedListEvents = (ArrayList<Event>) getArguments().get("passedList");
        else{
            ArrayList<Event> emptyArray = new ArrayList<Event>();
            passedListEvents = emptyArray;
        }

        //Binding the recycler view with an adapter loaded with the results array
        eventList = (RecyclerView) rootView.findViewById(R.id.eventList);
        eventAdapter = new EventAdapter((ArrayList<Event>) passedListEvents);
        eventList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        eventList.setAdapter(eventAdapter);
        eventList.setLayoutManager(llm);
        eventList.setItemAnimator(new DefaultItemAnimator());

        searchEvent();

        return rootView;
    }

    public Boolean checkInternetConnection(){

        Boolean isInternetOn = false;
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            buttonStartDate.setEnabled(true);
            buttonEndDate.setEnabled(true);
            buttonSearchEvent.setEnabled(true);
            isInternetOn = true;
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Data fetching denied")
                    .setMessage("Turn on your internet display the events.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

            /*
            buttonStartDate.setEnabled(false);
            buttonEndDate.setEnabled(false);
            buttonSearchEvent.setEnabled(false);
            */
            isInternetOn = false;
        }

        return isInternetOn;
    }

    public String formatDateForCardView(String date){
        String finalDate = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempDate = format.parse(date);

            finalDate = format.format(tempDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return finalDate;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonStartDate:
                StartDatePickerFragment dpf = new StartDatePickerFragment();
                dpf.show(getFragmentManager(), "startDate");
                eventAdapter.notifyDataSetChanged();
                break;
            case R.id.buttonEndDate:
                EndDatePickerFragment dpf2 = new EndDatePickerFragment();
                dpf2.show(getFragmentManager(), "endDate");
                eventAdapter.notifyDataSetChanged();
                break;
            case R.id.buttonSearchEvent:
                final Handler handler = new Handler();
                final Runnable r = new Runnable()
                {
                    public void run()
                    {
                        if(checkInternetConnection()){
                            searchEvent();
                        }
                        srl.setRefreshing(false);
                    }
                };

                handler.postDelayed(r, 500);
                if(checkInternetConnection()){
                    searchEvent();
                }
                break;
        }
    }

    public void searchEvent(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        this.startDate = preferences.getString("startDate", "0");
        this.endDate = preferences.getString("endDate", "0");
        new retrieveEventTask().execute();
    }


    public String defaultURLQueryBuilder(String startDate, String endDate){

        String finalURL = "https://api.hel.fi/linkedevents/v1/event/?start=" + startDate + "&end=" + endDate;

        Log.d("Searched URL", finalURL);

        return finalURL;
    }


    private class retrieveEventTask extends AsyncTask<URL, Integer, String> {
        HttpURLConnection urlConnection = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        retrieveEventTask(){

        }

        protected String doInBackground(URL... urls) {
            URL retrieveURL;
            passedListEvents = new ArrayList<>();

            try {
                retrieveURL = new URL(defaultURLQueryBuilder(startDate, endDate));
                urlConnection = (HttpURLConnection) retrieveURL.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                        Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                    }
                    Log.d("result", String.valueOf(buffer));

                    JSONObject json= (JSONObject) new JSONTokener(String.valueOf(buffer)).nextValue();
                    JSONArray json2 = json.getJSONArray("data");
                    Event eventToAdd;
                    for(int i=0; i < json2.length(); i++){
                        JSONObject currentEvent = json2.getJSONObject(i);
                        eventToAdd = new Event();

                        //Blank properties
                        String title = "Unknown";
                        String startDate = "Unknown";
                        String endDate = "Unknown";
                        String location = "Helsinki";
                        String description = "Unknown";
                        String id = "Unknown";


                        //Fetching the properties and values
                        JSONObject objectName = (JSONObject) currentEvent.get("name");
                        if(objectName != null){
                            if(objectName.has("en")){
                                title = objectName.getString("en");
                            }else if(objectName.has("fi")){
                                title = objectName.getString("fi");
                            }
                        }


                        JSONObject objectDesc = (JSONObject) currentEvent.get("short_description");
                        if(objectDesc != null) {
                            if (objectDesc.has("en")) {
                                description = objectDesc.getString("en");
                            } else if (objectDesc.has("fi")) {
                                description = objectDesc.getString("fi");
                            }
                        }

                        if(currentEvent.get("start_time") != null)
                            startDate = formatDateForCardView(currentEvent.getString("start_time"));

                        if(currentEvent.get("end_time") != null)
                            endDate = formatDateForCardView(currentEvent.getString("end_time"));

                        if(currentEvent.get("id") != null)
                            id = (String) currentEvent.getString("id");

                        eventToAdd = new Event(title, startDate, endDate, id, description, location);
                        passedListEvents.add(eventToAdd);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        protected void onPostExecute(String result) {

            Log.d("task finished", String.valueOf(passedListEvents.size()));
            for (int i = 0; i < passedListEvents.size(); i++){
                Log.d("event " + i, String.valueOf(passedListEvents.get(i)));
            }
            eventAdapter = new EventAdapter((ArrayList<Event>) passedListEvents);
            eventList.setAdapter(eventAdapter);
            eventAdapter.notifyDataSetChanged();
        }
    }

}
