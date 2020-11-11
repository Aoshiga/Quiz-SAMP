package quizSAMPApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionsDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private static final String DATABASE_NAME = "quizSAMP.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + QuizSAMPDBDescription.THEME_TABLE_NAME + "("
            + QuizSAMPDBDescription._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuizSAMPDBDescription.COLUMN_THEME_NAME
            + " TEXT NOT NULL, " + QuizSAMPDBDescription.COLUMN_QUESTIONS + " FLOAT NOT NULL " + ");";

    public QuestionsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        this.db = database;
        db.execSQL(DATABASE_CREATE);
        db.execSQL("INSERT INTO " + QuizSAMPDBDescription.THEME_TABLE_NAME + " ( " + QuizSAMPDBDescription.COLUMN_THEME_NAME
                + "," + QuizSAMPDBDescription.COLUMN_QUESTIONS + ")  VALUES ('AVEN D''ORGNAC', -1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + QuizSAMPDBDescription.THEME_TABLE_NAME);
        onCreate(database);
    }

    public Cursor getValuesCursor() {
        // ordre des noms
        // return db.query(GrandSiteBDDesc.TABLE_NAME, null, null, null, null,
        //                  null, GrandSiteBDDesc.COLUMN_NOM);
        // sans ordre
        return db.query(QuizSAMPDBDescription.THEME_TABLE_NAME,null,null,null,null,null,null);
    }

    public void insertTheme(String name) {
        ContentValues values = new ContentValues();
        values.put(QuizSAMPDBDescription.COLUMN_THEME_NAME, name);
        values.put(QuizSAMPDBDescription.COLUMN_QUESTIONS, -1);
        db.insert(QuizSAMPDBDescription.THEME_TABLE_NAME, null, values);
    }
}

