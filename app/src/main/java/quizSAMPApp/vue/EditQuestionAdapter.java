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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsamp.R;

import java.util.List;

import quizSAMPApp.database.QuizSAMPDBDescription;
import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Theme;

public class EditQuestionAdapter extends RecyclerView.Adapter<EditQuestionAdapter.ViewHolder> {

    private Context ctx;
    private Cursor cursor;

    public EditQuestionAdapter(Context context, Cursor c) {
        this.cursor = c;
        this.ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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

    public EditQuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.question_item_edit, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        if(!cursor.moveToPosition(pos)) return;
        String themeName = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_ASSOCIATE_THEME));
        if(!themeName.equals(((EditQuestionActivity)ctx).getThemeName())) return;

        String question = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_QUESTION));
        int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
        holder.tvQuestion.setText(question);

        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditQuestionActivity) ctx).alertRemoveQuestion(holder);
            }
        });

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
        etAddQuestion.setHint(question);

        etAddAnswer1 = addQuestionDialog.findViewById(R.id.et_add_answer1);
        etAddAnswer2 = addQuestionDialog.findViewById(R.id.et_add_answer2);
        etAddAnswer3 = addQuestionDialog.findViewById(R.id.et_add_answer3);
        etAddAnswer4 = addQuestionDialog.findViewById(R.id.et_add_answer4);
        etCorrectAnswer = addQuestionDialog.findViewById(R.id.et_add_correctAnswer);
        bValidateQuestion = addQuestionDialog.findViewById(R.id.b_validate_theme);

        bEditQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddQuestion.getText().clear();
                addQuestionDialog.show();
            }
        });

        bValidateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddQuestion.getText().toString().isEmpty()) etAddQuestion.setError("Aucune question saisie!");
                if(etAddAnswer1.getText().toString().isEmpty()) etAddAnswer1.setError("Les réponses 1 et 2 sont obligatoires!");
                if(etAddAnswer2.getText().toString().isEmpty()) etAddAnswer2.setError("Les réponses 1 et 2 sont obligatoires!");
                if(etCorrectAnswer.getText().toString().isEmpty()) etCorrectAnswer.setError("Vous devez indiquer quelle réponse est valide!");
                if(etAddAnswer3.getText().toString().isEmpty() && etCorrectAnswer.getText().toString().equals("3")) etCorrectAnswer.setError("La réponse 3 est vide: elle ne peut pas être la bonne réponse!");
                if(etAddAnswer4.getText().toString().isEmpty() && etCorrectAnswer.getText().toString().equals("4")) etCorrectAnswer.setError("La réponse 4 est vide: elle ne peut pas être la bonne réponse!");

                else {
                    try {
                        //updateQuestion(themeName, etAddQuestion.getText().toString(), etAddAnswer1.getText().toString(), etAddAnswer2.getText().toString(), etAddAnswer3.getText().toString(), etAddAnswer4.getText().toString(), Integer.parseInt(etCorrectAnswer.getText().toString()));
                        addQuestionDialog.dismiss();
                    } catch (SQLiteConstraintException e) {
                        etAddQuestion.setError("Impossible d'ajouter ce thème.");
                    }
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
