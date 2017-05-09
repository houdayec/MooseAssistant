package labs.hantiz.mooseproject.Events;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Corentin on 21/04/2017.
 */
class Event implements Parcelable {

    private String title;
    private String startDate;
    private String endDate;
    private String description;
    private String location;
    private String id;

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    Event(){

    }

    Event(String title, String startDate, String endDate, String id, String description, String location) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.location = location;
        this.id = id;
    }

    private Event(Parcel in) {
        id = in.readString();
        title = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        description = in.readString();
        location = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(description);
        dest.writeString(location);
    }

    public int describeContents() {
        return 0;
    }
}
