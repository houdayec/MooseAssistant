package labs.hantiz.mooseproject.Information;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import labs.hantiz.mooseproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoMainFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private DatabaseHandler db;
    private ArrayList<Information> passedListInfo = new ArrayList<>();
    private InfoAdapter infoAdapter;
    private RecyclerView infoList;

    public InfoMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_info_main, container, false);

        //Setting up the categories spinner
        spinner = (Spinner) rootView.findViewById(R.id.listCategories);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.categories_information, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        db = new DatabaseHandler(getActivity());

        //Loading events
        infoList = (RecyclerView) rootView.findViewById(R.id.eventList);
        infoAdapter = new InfoAdapter(passedListInfo);
        infoList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        infoList.setAdapter(infoAdapter);
        infoList.setLayoutManager(llm);
        infoList.setItemAnimator(new DefaultItemAnimator());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Information");

        // Inflate the layout for this fragment
        return rootView;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        if(((TextView) parent.getChildAt(0)) != null) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }

        switch (parent.getSelectedItem().toString())  {
        case "HOAS":
            passedListInfo = (ArrayList<Information>) db.getAllInformationFor("HOAS");
            break;
        case "METROPOLIA":
            passedListInfo = (ArrayList<Information>) db.getAllInformationFor("METROPOLIA");
            break;
        case "HELSINKI":
            passedListInfo = (ArrayList<Information>) db.getAllInformationFor("HELSINKI");
            break;
        default:
            passedListInfo = (ArrayList<Information>) db.getAllInformationFor("HOAS");
            break;
    }
        infoAdapter.notifyDataSetChanged();
        infoAdapter = new InfoAdapter(passedListInfo);
        Log.d("infoAdapter count", String.valueOf(infoAdapter.getItemCount()));
        infoList.setAdapter(infoAdapter);
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
