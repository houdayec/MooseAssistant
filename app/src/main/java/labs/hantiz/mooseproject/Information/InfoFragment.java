package labs.hantiz.mooseproject.Information;


import android.app.ActionBar;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import labs.hantiz.mooseproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    private DatabaseHandler db;
    private ArrayList<Information> passedListInfo = new ArrayList<>();
    private InfoAdapter infoAdapter;
    private RecyclerView infoList;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        db = new DatabaseHandler(getActivity());



        if(getArguments() != null){
            passedListInfo = (ArrayList<Information>) getArguments().get("passedList");
        }


        infoList = (RecyclerView) rootView.findViewById(R.id.eventList);
        infoAdapter = new InfoAdapter(passedListInfo);
        infoList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        infoList.setAdapter(infoAdapter);
        infoList.setLayoutManager(llm);
        infoList.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

}
