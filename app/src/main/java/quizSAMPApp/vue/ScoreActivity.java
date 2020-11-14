package quizSAMPApp.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizsamp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import quizSAMPApp.modele.Question;

public class ScoreActivity extends AppCompatActivity {

    private TextView point;
    private TextView percent;
    private Button retry;
    private Button return_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        point = findViewById(R.id.t_point);
        percent = findViewById(R.id.t_percent);
        retry = findViewById(R.id.b_retry);
        return_menu = findViewById(R.id.b_return_menu);

        String s_score = getIntent().getStringExtra("SCORE");
        point.setText(s_score);
        String s_percent = getIntent().getStringExtra("PERCENT");
        percent.setText(s_percent);

        return_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ThemeActivity.class);
                ScoreActivity.this.startActivity(intent);
                ScoreActivity.this.finish();
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, QuestionActivity.class);
                intent.putExtra("QUESTION", getIntent().getSerializableExtra("QUESTION"));
                ScoreActivity.this.startActivity(intent);
                ScoreActivity.this.finish();
            }
        });
    }
}