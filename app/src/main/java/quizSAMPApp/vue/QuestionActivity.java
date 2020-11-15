package quizSAMPApp.vue;

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
import com.example.quizsamp.R;
import java.io.Serializable;
import java.util.List;
import quizSAMPApp.modele.Question;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView question_nbr;
    private List<Question> questionList;
    private int quesNum;
    private int score;
    private boolean alreadyClick = false;

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

        if(!alreadyClick) {
            int select_answer = 0;

            switch (v.getId()) {
                case R.id.b_answer1:
                    select_answer = 1;
                    break;

                case R.id.b_answer2:
                    select_answer = 2;
                    break;

                case R.id.b_answer3:
                    select_answer = 3;
                    break;

                case R.id.b_answer4:
                    select_answer = 4;
                    break;

                default:
            }

            alreadyClick = true;
            response(select_answer, v);
        }
    }

    private void getQuestionList()
    {
        questionList = (List<Question>) getIntent().getSerializableExtra("QUESTION");
        setQuestion();
        quesNum = 0;
    }

    private void setQuestion()
    {
        question.setText(questionList.get(0).getQuestion());
        answer1.setText(questionList.get(0).getAnswer1());
        answer2.setText(questionList.get(0).getAnswer2());

        if(questionList.get(quesNum).getAnswer3() == null) {
            answer3.setVisibility(View.INVISIBLE);
        } else {
            if ((questionList.get(quesNum).getAnswer3().isEmpty())) answer3.setVisibility(View.INVISIBLE);
            else {
                answer3.setVisibility(View.VISIBLE);
                answer3.setText(questionList.get(quesNum).getAnswer3());
            }
        }

        if(questionList.get(quesNum).getAnswer4() == null) {
            answer4.setVisibility(View.INVISIBLE);
        } else {
            if (questionList.get(quesNum).getAnswer4().isEmpty()) answer4.setVisibility(View.INVISIBLE);
            else {
                answer4.setVisibility(View.VISIBLE);
                answer4.setText(questionList.get(quesNum).getAnswer4());
            }
        }

        String s = 1 + "/" + questionList.size();
        question_nbr.setText(s);

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
        handler.postDelayed(this::changeQuestion, 1000);
    }

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

            String s = (quesNum+1) + "/" + questionList.size();
            question_nbr.setText(s);


        } else {
            // Go to score activity
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", score + " points");
            intent.putExtra("PERCENT", (int)((float) score/(float) questionList.size() *100.0) + "% de réponses justes");
            intent.putExtra("QUESTION", (Serializable) questionList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionActivity.this.finish();
        }
    }

    private void passQuestion(View view, final int value, int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(200)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value == 0){
                            alreadyClick = false;
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
                                    if(questionList.get(quesNum).getAnswer3() == null) {
                                        answer3.setVisibility(View.INVISIBLE);
                                    } else {
                                        if (questionList.get(quesNum).getAnswer3().isEmpty()) answer3.setVisibility(View.INVISIBLE);
                                        else {
                                            answer3.setVisibility(View.VISIBLE);
                                            ((Button) view).setText(questionList.get(quesNum).getAnswer3());
                                        }
                                    }
                                    break;
                                case 4:
                                    if(questionList.get(quesNum).getAnswer4() == null) {
                                            answer4.setVisibility(View.INVISIBLE);
                                    } else {
                                        if (questionList.get(quesNum).getAnswer4().isEmpty()) answer4.setVisibility(View.INVISIBLE);
                                        else {
                                            ((Button) view).setText(questionList.get(quesNum).getAnswer4());
                                            answer4.setVisibility(View.VISIBLE);
                                        }
                                    }
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