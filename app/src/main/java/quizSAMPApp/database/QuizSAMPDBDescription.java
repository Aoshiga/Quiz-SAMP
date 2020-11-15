package quizSAMPApp.database;

import android.provider.BaseColumns;

public class QuizSAMPDBDescription implements BaseColumns {
    private QuizSAMPDBDescription() {}

    //Theme table
    public static final String THEME_TABLE_NAME = "themesList";
    public static final String COLUMN_THEME_NAME = "themeName";

    //Question table
    public static final String QUESTION_TABLE_NAME = "questionsList";
    public static final String COLUMN_ASSOCIATE_THEME = "themeName";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER1 = "answer1";
    public static final String COLUMN_ANSWER2 = "answer2";
    public static final String COLUMN_ANSWER3 = "answer3";
    public static final String COLUMN_ANSWER4 = "answer4";
    public static final String COLUMN_CORRECT_ANSWER = "correctAnswer";
}
