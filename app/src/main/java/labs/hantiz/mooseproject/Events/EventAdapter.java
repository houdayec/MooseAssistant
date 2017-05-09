package labs.hantiz.mooseproject.Events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import labs.hantiz.mooseproject.R;

/**
 * Created by Corentin on 21/04/2017.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> listEvents;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView titleEvent, startDateEvent, endDateEvent, descriptionEvent, locationEvent;

        ViewHolder(View v) {
            super(v);
            titleEvent = (TextView) v.findViewById(R.id.titleEvent);
            startDateEvent = (TextView) v.findViewById(R.id.startDateEvent);
            endDateEvent = (TextView) v.findViewById(R.id.endDateEvent);
            descriptionEvent = (TextView) v.findViewById(R.id.descriptionEvent);
            locationEvent = (TextView) v.findViewById(R.id.locationEvent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    EventAdapter(ArrayList<Event> listEvents) {
        this.listEvents = listEvents;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event currentEvent = listEvents.get(position);
        holder.titleEvent.setText(currentEvent.getTitle());
        holder.startDateEvent.setText(currentEvent.getStartDate());
        holder.endDateEvent.setText(currentEvent.getEndDate());
        holder.descriptionEvent.setText(currentEvent.getDescription());
        holder.locationEvent.setText(currentEvent.getLocation());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        try {
            return listEvents.size();
        } catch (Exception ex) {
            return 0;
        }
    }

}
