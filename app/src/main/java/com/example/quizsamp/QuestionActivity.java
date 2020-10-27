package com.example.quizsamp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView question_nbr;
    private List<Question> questionList;
    private int quesNum;
    private int score;

    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.t_question);
        question_nbr = findViewById(R.id.t_question_number);

        answer1 = findViewById(R.id.b_answer1);
        answer2 = findViewById(R.id.b_answer2);
        answer3 = findViewById(R.id.b_answer3);
        answer4 = findViewById(R.id.b_answer4);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);

        getQuestionList();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        int select_answer = 0;

        switch (v.getId())
        {
            case R.id.b_answer1 :
                select_answer = 1;
                break;

            case R.id.b_answer2 :
                select_answer = 2;
                break;

            case R.id.b_answer3 :
                select_answer = 3;
                break;

            case R.id.b_answer4 :
                select_answer = 4;
                break;

            default:
        }

        response(select_answer, v);
    }

    private void getQuestionList()
    {
        questionList = new ArrayList<>();
        questionList.add(new Question("Le Master en Informatique peut-il se faire en alternance ?", "Oui", "Non", null, null, 1));
        questionList.add(new Question("Qui est le responsable du CMI ? ", "B. Tatibouët", "A. Giorgetti", "F. Dadeau", null, 3));
        questionList.add(new Question("Quelle est la durée maximale d'un stage ?", "12 semaines", "3 mois", "6 mois", "8 mois", 4));

        setQuestion();

        quesNum = 0;

    }

    @SuppressLint("SetTextI18n")
    private void setQuestion()
    {
        /*for (Question q : questionList) {
            question.setText((questionList.get(0)));
        }*/
        question.setText(questionList.get(0).getQuestion());
        answer1.setText(questionList.get(0).getAnswer1());
        answer2.setText(questionList.get(0).getAnswer2());
        answer3.setText(questionList.get(0).getAnswer3());
        answer4.setText(questionList.get(0).getAnswer4());

        if(questionList.get(quesNum).getAnswer3() == null)  answer3.setVisibility(View.INVISIBLE);
        else answer3.setVisibility(View.VISIBLE);
        if(questionList.get(quesNum).getAnswer4() == null)  answer4.setVisibility(View.INVISIBLE);
        else answer4.setVisibility(View.VISIBLE);

        question_nbr.setText(1 + "/" + questionList.size());

    }

    private void response(int select_answer, View view)
    {
        if(select_answer == questionList.get(quesNum).getCorrect_answer()) {
            //Right Answer
            view.setBackgroundColor(Color.GREEN);
            score++;
        } else {
            //Wrong Answer
            view.setBackgroundColor(Color.RED);
            switch (questionList.get(quesNum).getCorrect_answer()) {
                case 1:
                    answer1.setBackgroundColor(Color.GREEN);
                    break;
                case 2:
                    answer2.setBackgroundColor(Color.GREEN);
                    break;
                case 3:
                    answer3.setBackgroundColor(Color.GREEN);
                    break;
                case 4:
                    answer4.setBackgroundColor(Color.GREEN);
                    break;

            }
        }

        Handler handler = new Handler();
        handler.postDelayed(this::changeQuestion, 1500);

        //changeQuestion();
    }

    @SuppressLint("SetTextI18n")
    private void changeQuestion()
    {
        if(quesNum < questionList.size() -1 )
        {
            quesNum ++;
            passQuestion(question, 0, 0);
            passQuestion(answer1, 0, 1);
            passQuestion(answer2, 0, 2);
            passQuestion(answer3, 0, 3);
            passQuestion(answer4, 0, 4);

            question_nbr.setText((quesNum+1) + "/" + questionList.size());


        } else {
            // Go to score activity
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", score + " points");
            intent.putExtra("PERCENT", (int)((float) score/(float) questionList.size() *100.0) + "% de réponses justes");
            startActivity(intent);
            QuestionActivity.this.finish();
        }
    }

    private void passQuestion(View view, final int value, int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(400)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value == 0){
                            switch (viewNum){
                                case 0:
                                    ((TextView)view).setText(questionList.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(quesNum).getAnswer1());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.get(quesNum).getAnswer2());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.get(quesNum).getAnswer3());
                                    if(questionList.get(quesNum).getAnswer3() == null)  answer3.setVisibility(View.INVISIBLE);
                                    else answer3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.get(quesNum).getAnswer4());
                                    if(questionList.get(quesNum).getAnswer4() == null)  answer4.setVisibility(View.INVISIBLE);
                                    else answer4.setVisibility(View.VISIBLE);
                                    break;
                            }

                            if(viewNum != 0) view.setBackgroundColor(Color.parseColor("#28A8E0"));
                            passQuestion(view, 1, viewNum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }


}