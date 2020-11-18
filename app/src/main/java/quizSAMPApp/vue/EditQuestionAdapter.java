package quizSAMPApp.vue;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizsamp.R;
import quizSAMPApp.database.QuizSAMPDBDescription;
import quizSAMPApp.database.QuizSAMPDBHelper;

public class EditQuestionAdapter extends RecyclerView.Adapter<EditQuestionAdapter.ViewHolder> {

    private final Context ctx;
    private Cursor cursor;

    public EditQuestionAdapter(Context context, Cursor c) {
        this.cursor = c;
        this.ctx = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvQuestion;
        private final Button bDelete;
        private final Button bEditQuestion;

        public ViewHolder(View v) {
            super(v);
            tvQuestion = v.findViewById(R.id.t_question);
            bDelete = v.findViewById(R.id.b_deleteQ);
            bEditQuestion = v.findViewById(R.id.b_edit_question);
        }
    }

    @NonNull
    public EditQuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.question_item_edit, parent, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        if(!cursor.moveToPosition(pos)) return;
        String themeName = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME));
        if(!themeName.equals(((EditQuestionActivity)ctx).getThemeName())) return;

        String question = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_QUESTION));
        int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
        holder.tvQuestion.setText(question);

        holder.bDelete.setOnClickListener(v -> ((EditQuestionActivity) ctx).alertRemoveQuestion(holder));

        editQuestion(holder.bEditQuestion, cursor);

        holder.itemView.setTag(id);
    }

    public void editQuestion(Button bEditQuestion, Cursor cursor){
        EditText etAddQuestion;
        EditText etAddAnswer1;
        EditText etAddAnswer2;
        EditText etAddAnswer3;
        EditText etAddAnswer4;
        EditText etCorrectAnswer;
        Button bValidateQuestion;
        Dialog addQuestionDialog;

        addQuestionDialog = new Dialog((EditQuestionActivity)ctx);
        addQuestionDialog.setContentView(R.layout.add_question_dialog);
        addQuestionDialog.setCancelable(true);
        addQuestionDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        etAddQuestion = addQuestionDialog.findViewById(R.id.et_add_question);
        String question = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_QUESTION));
        etAddQuestion.setText(question);

        etAddAnswer1 = addQuestionDialog.findViewById(R.id.et_add_answer1);
        String answer1 = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER1));
        etAddAnswer1.setText(answer1);

        etAddAnswer2 = addQuestionDialog.findViewById(R.id.et_add_answer2);
        String answer2 = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER2));
        etAddAnswer2.setText(answer2);

        etAddAnswer3 = addQuestionDialog.findViewById(R.id.et_add_answer3);
        String answer3 = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER3));
        if(answer3 != null) etAddAnswer3.setText(answer3);
        else etAddAnswer3.getText().clear();

        etAddAnswer4 = addQuestionDialog.findViewById(R.id.et_add_answer4);
        String answer4 = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_ANSWER4));
        if(answer4 != null) etAddAnswer4.setText(answer4);
        else etAddAnswer4.getText().clear();

        etCorrectAnswer = addQuestionDialog.findViewById(R.id.et_add_correctAnswer);
        int correctAnswer = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_CORRECT_ANSWER));
        String s_correctAnswer = Integer.toString(correctAnswer);
        etCorrectAnswer.setText(s_correctAnswer);

        bValidateQuestion = addQuestionDialog.findViewById(R.id.b_validate_theme);
        String txtButton = "Editer";
        bValidateQuestion.setText(txtButton);

        bEditQuestion.setOnClickListener(v -> addQuestionDialog.show());

        bValidateQuestion.setOnClickListener(v -> {
            if(etAddQuestion.getText().toString().isEmpty()) etAddQuestion.setError("Aucune question saisie!");
            else if(etAddAnswer1.getText().toString().isEmpty()) etAddAnswer1.setError("Les réponses 1 et 2 sont obligatoires!");
            else if(etAddAnswer2.getText().toString().isEmpty()) etAddAnswer2.setError("Les réponses 1 et 2 sont obligatoires!");
            else if(etCorrectAnswer.getText().toString().isEmpty()) etCorrectAnswer.setError("Vous devez indiquer quelle réponse est valide!");
            else if(Integer.parseInt(etCorrectAnswer.getText().toString()) < 0 || Integer.parseInt(etCorrectAnswer.getText().toString()) > 4) etCorrectAnswer.setError("Aucune réponse ne correspond au numéro indiqué");
            else if(etAddAnswer3.getText().toString().isEmpty() && etCorrectAnswer.getText().toString().equals("3")) etCorrectAnswer.setError("La réponse 3 est vide: elle ne peut pas être la bonne réponse!");
            else if(etAddAnswer4.getText().toString().isEmpty() && etCorrectAnswer.getText().toString().equals("4")) etCorrectAnswer.setError("La réponse 4 est vide: elle ne peut pas être la bonne réponse!");
            else {
                try {
                    int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
                    ((EditQuestionActivity)ctx).updateQuestion(id, etAddQuestion.getText().toString(), etAddAnswer1.getText().toString(), etAddAnswer2.getText().toString(), etAddAnswer3.getText().toString(), etAddAnswer4.getText().toString(), Integer.parseInt(etCorrectAnswer.getText().toString()));
                    addQuestionDialog.dismiss();
                } catch (SQLiteConstraintException e) {
                    etAddQuestion.setError("Impossible d'ajouter ce thème.");
                }
            }
        });
    }

    public void swapCursor(Cursor newCursor) {
        if(cursor != null) cursor.close();
        cursor = newCursor;
        if(newCursor != null) notifyDataSetChanged();
    }

    public int getItemCount() {
        return QuizSAMPDBHelper.getQuestionsProfilesCount(((EditQuestionActivity)ctx).getThemeName());
    }
}
