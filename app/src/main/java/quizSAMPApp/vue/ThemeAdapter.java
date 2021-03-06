package quizSAMPApp.vue;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsamp.R;

import java.io.Serializable;
import java.util.List;

import quizSAMPApp.database.QuizSAMPDBDescription;
import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Theme;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    private final List<Theme> themeList;
    private final Context ctx;
    private final Cursor cursor;

    public ThemeAdapter(Context context, Cursor c, List<Theme> lt) {
        this.cursor = c;
        this.ctx = context;
        this.themeList = lt;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView themeName;
        public Button bStart;

        public ViewHolder(View v) {
            super(v);
            themeName = v.findViewById(R.id.t_themeName);
            bStart = v.findViewById(R.id.b_start);
        }
    }

    @NonNull
    public ThemeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.theme_item, parent, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        final Theme t = themeList.get(pos);

        if(!cursor.moveToPosition(pos)) return;
        String name = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_THEME_NAME));
        int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
        holder.themeName.setText(name);

        if(t.getQuestionList().isEmpty()) {
            holder.bStart.setVisibility(View.GONE);
        }

        holder.themeName.setOnClickListener(v -> Toast.makeText(ctx,name,Toast.LENGTH_LONG).show());

        holder.bStart.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, QuestionActivity.class);
            intent.putExtra("QUESTION", (Serializable) t.getQuestionList());
            ctx.startActivity(intent);
        });

        holder.itemView.setTag(id);
    }

    public int getItemCount() {
        return QuizSAMPDBHelper.getThemesProfilesCount();
    }
}
