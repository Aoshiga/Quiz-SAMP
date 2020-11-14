package quizSAMPApp.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.quizsamp.R;

import quizSAMPApp.database.QuizSAMPDBHelper;

public class EditThemeActivity extends AppCompatActivity {

    //private GridView themeGrid;

    private RecyclerView viewThemes;
    private EditThemeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Dialog addThemeDialog;
    private ImageButton bAddTheme;
    private EditText etAddTheme;
    private Button bValidateTheme;

    QuizSAMPDBHelper themes;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK, null);
                EditThemeActivity.this.finish();
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
        setContentView(R.layout.activity_edit_theme);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edition des thèmes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        themes = new QuizSAMPDBHelper(this);

        viewThemes = (RecyclerView) findViewById(R.id.rv_themeView);
        layoutManager = new LinearLayoutManager(this);
        viewThemes.setLayoutManager(layoutManager);
        adapter = new EditThemeAdapter(this, themes.getThemeValuesCursor());
        viewThemes.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        if(viewHolder.getItemViewType() != target.getItemViewType()) return false;
                        moveTheme(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        alertRemoveTheme(viewHolder);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(viewThemes);


        bAddTheme = findViewById(R.id.b_add_theme);

        addThemeDialog = new Dialog(EditThemeActivity.this);
        addThemeDialog.setContentView(R.layout.add_theme_dialog);
        addThemeDialog.setCancelable(true);
        addThemeDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        etAddTheme = addThemeDialog.findViewById(R.id.et_add_theme_name);
        bValidateTheme = addThemeDialog.findViewById(R.id.b_validate_theme);

        bAddTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddTheme.getText().clear();
                addThemeDialog.show();
            }
        });

        bValidateTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddTheme.getText().toString().isEmpty()) etAddTheme.setError("Aucun nom saisie!");
                else {
                    try {
                        addTheme(etAddTheme.getText().toString());
                        addThemeDialog.dismiss();
                    } catch (SQLiteConstraintException e) {
                        etAddTheme.setError("Impossible d'ajouter ce thème.\nCause probable : thème déjà existant.");
                    }
                }
            }
        });
    }

    public void addTheme(String name) {
        themes.insertTheme(name);
        //if(adapter.hasObservers()) adapter.notifyItemInserted(adapter.getItemCount());
        adapter.swapCursor(themes.getThemeValuesCursor());
    }

    public void alertRemoveTheme(RecyclerView.ViewHolder viewHolder){
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

    public void removeTheme(int pos) {
        themes.deleteThemeById(pos);
        adapter.swapCursor(themes.getThemeValuesCursor());
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