package quizSAMPApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
  themesName INT NOT NULL
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

    public QuizSAMPDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        db = database;
        db.execSQL(DATABASE_CREATE_THEME);
        db.execSQL(DATABASE_CREATE_QUESTION);

        /*Add default theme*/
        insertTheme("Informatique");
        insertQuestion("Informatique", "Le Master en Informatique peut-il se faire en alternance ?", "Oui", "Non", null, null, 1);
        insertQuestion("Informatique", "Qui est le responsable du CMI ? ", "B. Tatibouët", "A. Giorgetti", "F. Dadeau", null, 3);
        insertQuestion("Informatique", "Quelle est la durée maximale d'un stage ?", "12 semaines", "3 mois", "6 mois", "8 mois", 4);

        insertTheme("Nourriture");
        insertTheme("Musique");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + QuizSAMPDBDescription.THEME_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + QuizSAMPDBDescription.QUESTION_TABLE_NAME);
        onCreate(database);
    }

    public static Cursor getThemeValuesCursor() {
        // ordre alphabetique
        return db.query(QuizSAMPDBDescription.THEME_TABLE_NAME, null, null, null, null,null, QuizSAMPDBDescription.COLUMN_THEME_NAME);
        // sans ordre
        //return db.query(QuizSAMPDBDescription.THEME_TABLE_NAME,null,null,null,null,null,null);
    }

    public static Cursor getQuestionsValuesCursor(String themeName) {
        String[] projections = new String[] {"*"};
        String select = QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME + "=?";
        String[] selectionArg = new String[] {themeName};
        String groupBy = QuizSAMPDBDescription.COLUMN_QUESTION;
        String orderBy = QuizSAMPDBDescription._ID + "  ASC";
        return db.query(QuizSAMPDBDescription.QUESTION_TABLE_NAME, projections, select, selectionArg, groupBy,null, orderBy);
    }

    public void insertTheme(String name) {
        ContentValues values = new ContentValues();
        values.put(QuizSAMPDBDescription.COLUMN_THEME_NAME, name);
        db.insertOrThrow(QuizSAMPDBDescription.THEME_TABLE_NAME, null, values);
    }

    public void insertQuestion(String themeName, String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer) {
        ContentValues values = new ContentValues();
        values.put(QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME, themeName);
        values.put(QuizSAMPDBDescription.COLUMN_QUESTION, question);
        values.put(QuizSAMPDBDescription.COLUMN_ANSWER1, answer1);
        values.put(QuizSAMPDBDescription.COLUMN_ANSWER2, answer2);
        values.put(QuizSAMPDBDescription.COLUMN_ANSWER3, answer3);
        values.put(QuizSAMPDBDescription.COLUMN_ANSWER4, answer4);
        values.put(QuizSAMPDBDescription.COLUMN_CORRECT_ANSWER, correctAnswer);
        db.insertOrThrow(QuizSAMPDBDescription.QUESTION_TABLE_NAME, null, values);
    }

    public List<Theme> getAll() {
        List<Theme> lt = new LinkedList<>();
        String name;

        Cursor cursor = getThemeValuesCursor();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            name = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_THEME_NAME));

            lt.add(new Theme(name, getQuestionsFromThemeName(name)));
            cursor.moveToNext();
        }

        cursor.close();
        return lt;
    }

    private List<Question> getQuestionsFromThemeName(String themeName){
        List<Question> lq = new LinkedList<>();
        String question;
        String answer1;
        String answer2;
        String answer3;
        String answer4;
        int correctAnswer;

        String[] projections = new String[] {"*"};
        String select = QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME + "=?";
        String[] selectionArg = new String[] {themeName};
        String groupBy = QuizSAMPDBDescription.COLUMN_QUESTION;
        String orderBy = QuizSAMPDBDescription._ID + "  ASC";
        Cursor crs = db.query(QuizSAMPDBDescription.QUESTION_TABLE_NAME, projections, select, selectionArg, groupBy,null, orderBy);

        crs.moveToFirst();
        while (!crs.isAfterLast()) {
            question = crs.getString(crs.getColumnIndex(QuizSAMPDBDescription.COLUMN_QUESTION));
            answer1 = crs.getString(crs.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER1));
            answer2 = crs.getString(crs.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER2));
            answer3 = crs.getString(crs.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER3));
            answer4 = crs.getString(crs.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER4));
            correctAnswer = crs.getInt(crs.getColumnIndex(QuizSAMPDBDescription.COLUMN_CORRECT_ANSWER));

            lq.add(new Question(question, answer1, answer2, answer3, answer4, correctAnswer));
            crs.moveToNext();
        }

        crs.close();
        return lq;
    }

    public Theme getTheme(int id) {
        Cursor cursor = getThemeValuesCursor();
        String name = cursor.getString(id);
        Theme t = new Theme(name, getQuestionsFromThemeName(name));
        cursor.close();
        return t;
    }

    public void deleteThemeById(int id) {
        Cursor cursor = getThemeValuesCursor();
        cursor.moveToPosition(id);
        String tn = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_THEME_NAME));
        db.delete(QuizSAMPDBDescription.THEME_TABLE_NAME, (QuizSAMPDBDescription.COLUMN_THEME_NAME + " = " + '"' + tn + '"'), null);

        String elementToDelete = QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME + " = "
                + '"' + cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_THEME_NAME)) + '"';
        db.delete(QuizSAMPDBDescription.QUESTION_TABLE_NAME, elementToDelete, null);
        cursor.close();
    }

    public void deleteQuestionById(String themeName, int pos) {
        Cursor cursor = getQuestionsValuesCursor(themeName);
        cursor.moveToPosition(pos);
        int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
        db.delete(QuizSAMPDBDescription.QUESTION_TABLE_NAME, (QuizSAMPDBDescription._ID + " = " + id), null);
        cursor.close();
    }

    public void close() {
        db.close();
    }

    public static int getThemesProfilesCount() {
        return (int)DatabaseUtils.queryNumEntries(db, QuizSAMPDBDescription.THEME_TABLE_NAME);
    }

    public static int getQuestionsProfilesCount(String themeName) {
        String select = "SELECT COUNT(*) FROM " + QuizSAMPDBDescription.QUESTION_TABLE_NAME + " WHERE "
                + QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME + " = " + '"' + themeName + '"';
        return (int)DatabaseUtils.longForQuery(db, select, null);
    }

}
