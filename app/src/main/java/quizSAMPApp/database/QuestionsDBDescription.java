package quizSAMPApp.database;

import android.provider.BaseColumns;

public class QuestionsDBDescription implements BaseColumns {
    private QuestionsDBDescription() {
    }

    public static final String TABLE_NAME = "questionsList";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER1 = "answer1";
    public static final String COLUMN_ANSWER2 = "answer2";
    public static final String COLUMN_ANSWER3 = "answer3";
    public static final String COLUMN_ANSWER4 = "answer4";
    public static final int COLUMN_CORRECT_ANSWER = 1;
}

