package labs.hantiz.mooseproject.Information;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 28/04/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Init variables
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myDatabase";

    // Tables names
    private static final String TABLE_INFORMATION = "information";

    // Tables columns
    // Artists
    private static final String ID_INFORMATION = "id_info";
    private static final String TITLE_INFORMATION = "name_info";
    private static final String DESC_INFORMATION = "desc_info";
    private static final String FAMILY_INFORMATION = "fam_info";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creating the tables

        //Information
        String CREATE_TABLE_ARTISTS = "CREATE TABLE " + TABLE_INFORMATION + "("
                + ID_INFORMATION + " INTEGER PRIMARY KEY," + TITLE_INFORMATION + " TEXT," + DESC_INFORMATION + " TEXT," + FAMILY_INFORMATION + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_ARTISTS);

        //Inserting static data
        //Hoas inserts
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Pick up your keys','You have to go to HOAS center to pick them up.','HOAS')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Drop your keys','Put your keys in a envelop and drop them in the HOAS center mailbox','HOAS')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Final cleaning','You should be ready and have a cleaned appartment from the 18th May','HOAS')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Feedback','You can give your opinion and comments on our facebook page and website','HOAS')");

        //Metropolia inserts
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Opening hours','The university is open from 8 to 20 during weekdays.','METROPOLIA')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Restauration','You can eat at the sodexo cafeteria during weekdays.','METROPOLIA')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Activities','By subscribing to metka sport you can have access to the gym and the sport hall.','METROPOLIA')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Nurse','A nurse is available for you at the university during weekdays except on thursday','METROPOLIA')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Badge','If you lose your badge please see with securitas.','METROPOLIA')");

        //Helsinki inserts
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Rock church','The rock church is open everyday.','HELSINKI')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Sample','This area is correspond to the information description.','HELSINKI')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Sample','This area is correspond to the information description.','HELSINKI')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Sample','This area is correspond to the information description.','HELSINKI')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_INFORMATION + " ("+ TITLE_INFORMATION +","+DESC_INFORMATION+","+FAMILY_INFORMATION+") VALUES('Suomi 100','This year, finland is celebrating the 100th year of independancy with events','HELSINKI')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INFORMATION);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    //CRUD FOR INFORMATION


    public List<Information> getAllInformation(){

        List<Information> listInfos = new ArrayList<Information>();

        String selectAllQuery = "SELECT * FROM " + TABLE_INFORMATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()){
            do{
                Information nInfo = new Information();
                nInfo.setId(Integer.parseInt(String.valueOf(cursor.getString(0))));
                nInfo.setTitle(String.valueOf(cursor.getString(1)));
                nInfo.setDescription(String.valueOf(cursor.getString(2)));
                nInfo.setFamily(String.valueOf(cursor.getString(3)));

                listInfos.add(nInfo);
            } while(cursor.moveToNext());
        }

        return listInfos;
    }

    public List<Information> getAllInformationFor(String family){

        List<Information> listInfos = new ArrayList<Information>();

        String selectAllQuery = "SELECT * FROM " + TABLE_INFORMATION + " WHERE " + FAMILY_INFORMATION + " = '" + family+"'";
        Log.d("selectAllQuery", selectAllQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()){
            do{
                Information nInfo = new Information();
                nInfo.setId(Integer.parseInt(String.valueOf(cursor.getString(0))));
                nInfo.setTitle(String.valueOf(cursor.getString(1)));
                nInfo.setDescription(String.valueOf(cursor.getString(2)));
                nInfo.setFamily(String.valueOf(cursor.getString(3)));

                listInfos.add(nInfo);
            } while(cursor.moveToNext());
        }

        return listInfos;
    }

    public int getInfoCount() {
        Integer counter = 0;
        String countQuery = "SELECT  * FROM " + TABLE_INFORMATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        if(cursor != null && !cursor.isClosed()){
            counter = cursor.getCount();
            cursor.close();
        }

        // return count
        return counter;
    }


}