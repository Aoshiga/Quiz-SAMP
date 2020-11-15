package quizSAMPApp.vue;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.quizsamp.R;


import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Question;

public class ThemeActivity extends AppCompatActivity {

    private RecyclerView viewThemes;
    private ThemeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    QuizSAMPDBHelper themes;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, ThemeActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ThemeActivity.this.finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditThemeActivity.class);
                this.startActivityForResult(intent, 1);
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
        setContentView(R.layout.activity_theme);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thèmes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        themes = new QuizSAMPDBHelper(this);

        viewThemes = (RecyclerView) findViewById(R.id.rv_themeView);
        layoutManager = new LinearLayoutManager(this);
        viewThemes.setLayoutManager(layoutManager);
        adapter = new ThemeAdapter(this, themes.getThemeValuesCursor(), themes.getAll());
        viewThemes.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        themes.close();
        super.onDestroy();
    }
}