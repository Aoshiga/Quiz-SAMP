package quizSAMPApp.vue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizsamp.R;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView point = findViewById(R.id.t_point);
        TextView percent = findViewById(R.id.t_percent);
        Button retry = findViewById(R.id.b_retry);
        Button return_menu = findViewById(R.id.b_return_menu);

        String s_score = getIntent().getStringExtra("SCORE");
        point.setText(s_score);
        String s_percent = getIntent().getStringExtra("PERCENT");
        percent.setText(s_percent);

        return_menu.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, ThemeActivity.class);
            ScoreActivity.this.startActivity(intent);
            ScoreActivity.this.finish();
        });

        retry.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, QuestionActivity.class);
            intent.putExtra("QUESTION", getIntent().getSerializableExtra("QUESTION"));
            ScoreActivity.this.startActivity(intent);
            ScoreActivity.this.finish();
        });
    }
}