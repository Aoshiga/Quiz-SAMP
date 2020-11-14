package quizSAMPApp.vue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsamp.R;

import java.util.List;

import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Question;
import quizSAMPApp.modele.Theme;

public class EditQuestionActivity extends AppCompatActivity {

    //private GridView themeGrid;

    private RecyclerView viewQuestions;
    private EditQuestionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String themeName;

    QuizSAMPDBHelper questions;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EditQuestionActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theme); // Just a list of item so we can you same for question

        themeName = getThemeName();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thème: " + themeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questions = new QuizSAMPDBHelper(this);

        viewQuestions = (RecyclerView) findViewById(R.id.rv_themeView);
        layoutManager = new LinearLayoutManager(this);
        viewQuestions.setLayoutManager(layoutManager);
        adapter = new EditQuestionAdapter(this, questions.getQuestionsValuesCursor(themeName));
        viewQuestions.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        if(viewHolder.getItemViewType() != target.getItemViewType()) return false;
                        moveQuestion(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        alertRemoveQuestion(viewHolder);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(viewQuestions);

        addQuestion();
        //editQuestion();
    }

    public void addQuestion(){
        ImageButton bAddQuestion;
        EditText etAddQuestion;
        EditText etAddAnswer1;
        EditText etAddAnswer2;
        EditText etAddAnswer3;
        EditText etAddAnswer4;
        EditText etCorrectAnswer;
        Button bValidateQuestion;
        Dialog addQuestionDialog;

        bAddQuestion = findViewById(R.id.b_add_theme);

        addQuestionDialog = new Dialog(EditQuestionActivity.this);
        addQuestionDialog.setContentView(R.layout.add_question_dialog);
        addQuestionDialog.setCancelable(true);
        addQuestionDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        etAddQuestion = addQuestionDialog.findViewById(R.id.et_add_question);
        etAddAnswer1 = addQuestionDialog.findViewById(R.id.et_add_answer1);
        etAddAnswer2 = addQuestionDialog.findViewById(R.id.et_add_answer2);
        etAddAnswer3 = addQuestionDialog.findViewById(R.id.et_add_answer3);
        etAddAnswer4 = addQuestionDialog.findViewById(R.id.et_add_answer4);
        etCorrectAnswer = addQuestionDialog.findViewById(R.id.et_add_correctAnswer);
        bValidateQuestion = addQuestionDialog.findViewById(R.id.b_validate_theme);

        bAddQuestion.setOnClickListener(new View.OnClickListener() {
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
                        addQuestion(themeName, etAddQuestion.getText().toString(), etAddAnswer1.getText().toString(), etAddAnswer2.getText().toString(), etAddAnswer3.getText().toString(), etAddAnswer4.getText().toString(), Integer.parseInt(etCorrectAnswer.getText().toString()));
                        addQuestionDialog.dismiss();
                    } catch (SQLiteConstraintException e) {
                        etAddQuestion.setError("Impossible d'ajouter ce thème.");
                    }
                }
            }
        });
    }

    /*public void editQuestion(){
        Button bEditQuestion;
        EditText etAddQuestion;
        EditText etAddAnswer1;
        EditText etAddAnswer2;
        EditText etAddAnswer3;
        EditText etAddAnswer4;
        EditText etCorrectAnswer;
        Button bValidateQuestion;
        Dialog addQuestionDialog;

        bEditQuestion = findViewById(R.id.b_edit_question);

        addQuestionDialog = new Dialog(EditQuestionActivity.this);
        addQuestionDialog.setContentView(R.layout.add_question_dialog);
        addQuestionDialog.setCancelable(true);
        addQuestionDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        etAddQuestion = addQuestionDialog.findViewById(R.id.et_add_question);
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
                        updateQuestion(themeName, etAddQuestion.getText().toString(), etAddAnswer1.getText().toString(), etAddAnswer2.getText().toString(), etAddAnswer3.getText().toString(), etAddAnswer4.getText().toString(), Integer.parseInt(etCorrectAnswer.getText().toString()));
                        addQuestionDialog.dismiss();
                    } catch (SQLiteConstraintException e) {
                        etAddQuestion.setError("Impossible d'ajouter ce thème.");
                    }
                }
            }
        });
    }*/

    public String getThemeName()
    {
        return getIntent().getStringExtra("THEMENAME");
    }

    public void addQuestion(String themeName, String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer) {
        questions.insertQuestion(themeName, question, answer1, answer2, answer3, answer4, correctAnswer);
        adapter.swapCursor(questions.getQuestionsValuesCursor(themeName));
    }

    public void alertRemoveQuestion(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder builder = new AlertDialog.Builder(viewQuestions.getContext());
        builder.setMessage("Êtes vous certains de supprimer la question " + "?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Supprimer",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeQuestion(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void removeQuestion(int pos) {
        questions.deleteQuestionById(getThemeName(), pos);
        adapter.swapCursor(questions.getQuestionsValuesCursor(getThemeName()));
    }

    public void moveQuestion(int indexSource, int indexCible) {
        /*Theme theme = questions.get(indexSource);
        questions.remove(indexSource);
        questions.add(indexCible, theme);
        if(adapter.hasObservers()) adapter.notifyItemMoved(indexSource, indexCible);*/
    }

    /*@Override
    public void onDestroy() {
        questions.close();
        super.onDestroy();
    }*/
}