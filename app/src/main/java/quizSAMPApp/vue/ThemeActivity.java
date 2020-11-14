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

import java.util.List;

import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Question;

public class ThemeActivity extends AppCompatActivity {

    //private GridView themeGrid;

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


        /*ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        if(viewHolder.getItemViewType() != target.getItemViewType()) return false;
                        moveTheme(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(viewThemes.getContext());
                        builder.setMessage("Êtes vous certains de supprimer la catégorie " + "?");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Supprimer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        removeTheme(viewHolder.getAdapterPosition());
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
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(viewThemes);*/
    }

    public void addTheme(String name, List<Question> q) {
        themes.insertTheme(name);
        //if(adapter.hasObservers()) adapter.notifyItemInserted(adapter.getItemCount());
        adapter.swapCursor(themes.getThemeValuesCursor());
    }

    public void removeTheme(int pos) {
        //themes.remove(pos);
        if(adapter.hasObservers()) adapter.notifyItemRemoved(pos);
        else {
            themes.deleteThemeById(pos);
            adapter.swapCursor(themes.getThemeValuesCursor());
        }
    }

    public void moveTheme(int indexSource, int indexCible) {
        /*Theme theme = themes.get(indexSource);
        themes.remove(indexSource);
        themes.add(indexCible, theme);
        if(adapter.hasObservers()) adapter.notifyItemMoved(indexSource, indexCible);*/
    }

    @Override
    public void onDestroy() {
        themes.close();
        super.onDestroy();
    }
}