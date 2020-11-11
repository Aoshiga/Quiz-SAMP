package quizSAMPApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import quizSAMPApp.modele.Question;
import quizSAMPApp.modele.Theme;

/*
CREATE TABLE themes
(
  themesId INTEGER AUTO_INCREMENT,
  name TEXT NOT NULL,
);

CREATE TABLE Questions
(
  questionId INT AUTO_INCREMENT,
  themesId INT NOT NULL
  question TEXT NOT NULL,
  answer1 TEXT NOT NULL,
  answer2 TEXT NOT NULL,
  answer3 TEXT,
  answer4 TEXT,
  correctAnswer INT
);
 */

public class QuizSAMPDBHelper extends SQLiteOpenHelper {
    private static SQLiteDatabase db;
    private static final String DATABASE_NAME = "quizSAMP.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_THEME =
            "CREATE TABLE " + QuizSAMPDBDescription.THEME_TABLE_NAME + "("
                    + QuizSAMPDBDescription._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QuizSAMPDBDescription.COLUMN_THEME_NAME + " TEXT UNIQUE NOT NULL" + ")"
            ;

    private static final String DATABASE_CREATE_QUESTION =
            "CREATE TABLE " + QuizSAMPDBDescription.QUESTION_TABLE_NAME + "("
                    + QuizSAMPDBDescription._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME + " TEXT NOT NULL, "
                    + QuizSAMPDBDescription.COLUMN_QUESTION + " TEXT NOT NULL, "
                    + QuizSAMPDBDescription.COLUMN_ANSWER1 + " TEXT NOT NULL, "
                    + QuizSAMPDBDescription.COLUMN_ANSWER2 + " TEXT NOT NULL, "
                    + QuizSAMPDBDescription.COLUMN_ANSWER3 + " TEXT, "
                    + QuizSAMPDBDescription.COLUMN_ANSWER4 + " TEXT, "
                    + QuizSAMPDBDescription.COLUMN_CORRECT_ANSWER + " INT NOT NULL " + ")"
            ;

    /*
    SELECT *
    FROM A
    INNER JOIN B on B.f = A.f;
     */
    private static final String DATABASE_THEME_JOIN_QUESTIONS =
            "SELECT * FROM " + QuizSAMPDBDescription.THEME_TABLE_NAME +
                    " INNER JOIN " + QuizSAMPDBDescription.QUESTION_TABLE_NAME +
                    " ON " + QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME +
                    " = " + QuizSAMPDBDescription.COLUMN_THEME_NAME + ";"
            ;

    public QuizSAMPDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        db = database;
        db.execSQL(DATABASE_CREATE_THEME);
        db.execSQL(DATABASE_CREATE_QUESTION);
        //db.execSQL(DATABASE_THEME_JOIN_QUESTIONS);

        /*Add default theme*/
        insertTheme("Informatique");
        insertTheme("Nourriture");
        insertTheme("Musique");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + QuizSAMPDBDescription.THEME_TABLE_NAME);
        onCreate(database);
    }

    public static Cursor getValuesCursor() {
        // ordre des noms
        return db.query(QuizSAMPDBDescription.THEME_TABLE_NAME, null, null, null, null,null, QuizSAMPDBDescription.COLUMN_THEME_NAME);
        // sans ordre
        //return db.query(QuizSAMPDBDescription.THEME_TABLE_NAME,null,null,null,null,null,null);
    }

    public void insertTheme(String name) {
        ContentValues values = new ContentValues();
        values.put(QuizSAMPDBDescription.COLUMN_THEME_NAME, name);
        db.insertOrThrow(QuizSAMPDBDescription.THEME_TABLE_NAME, null, values);
    }

    public List<Theme> getAll() {
        List<Theme> lt = new LinkedList<>();
        Cursor cursor = QuizSAMPDBHelper.getValuesCursor();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.THEME_TABLE_NAME));

            lt.add(new Theme(name, new ArrayList<>()));
            cursor.moveToNext();
        }

        cursor.close();
        return lt;
    }

    public Theme get(int id) {
        Cursor cursor = QuizSAMPDBHelper.getValuesCursor();
        Theme t = new Theme(cursor.getString(id), new ArrayList<>());
        cursor.close();
        return t;
    }

    public void deleteThemeById(int id) {
        db.delete(QuizSAMPDBDescription.THEME_TABLE_NAME, (QuizSAMPDBDescription._ID + " = " + id), null);
    }

    public void close() {
        db.close();
    }

    public static int getProfilesCount() {
        int count = (int)DatabaseUtils.queryNumEntries(db, QuizSAMPDBDescription.THEME_TABLE_NAME);
        return count;
    }

}
