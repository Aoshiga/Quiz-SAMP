package quizSAMPApp.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizsamp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.main_activity_start_button);
        start.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
            startActivity(intent);
        });
    }
}