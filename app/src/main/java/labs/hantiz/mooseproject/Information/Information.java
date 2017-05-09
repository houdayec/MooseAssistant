package labs.hantiz.mooseproject.Information;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Corentin on 28/04/2017.
 */
public class Information implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String family;

    public Information(String title, String description, String family) {
        this.title = title;
        this.description = description;
        this.family = family;
    }

    public Information(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    private Information(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        family = in.readString();
    }

    public static final Parcelable.Creator<Information> CREATOR = new Parcelable.Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(family);
    }

    public int describeContents() {
        return 0;
    }
}
