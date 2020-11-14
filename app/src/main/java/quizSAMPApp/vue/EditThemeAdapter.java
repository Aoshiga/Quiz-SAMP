package quizSAMPApp.vue;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsamp.R;

import java.io.Serializable;
import java.util.List;

import quizSAMPApp.database.QuizSAMPDBDescription;
import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Theme;

public class EditThemeAdapter extends RecyclerView.Adapter<EditThemeAdapter.ViewHolder> {

    private List<Theme> themeList;
    private Context ctx;
    private Cursor cursor;

    public EditThemeAdapter(Context context, Cursor c) {
        //this.themeList;
        this.cursor = c;
        this.ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView themeName;
        private ImageButton bDelete;
        private Button bEdit;

        public ViewHolder(View v) {
            super(v);
            themeName = v.findViewById(R.id.t_themeName);
            bDelete = v.findViewById(R.id.b_delete);
            bEdit = v.findViewById(R.id.b_edit);
        }
    }

    public EditThemeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.theme_item_edit, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        if(!cursor.moveToPosition(pos)) return;

        String name = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_THEME_NAME));
        int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
        holder.themeName.setText(name);
        holder.themeName.setOnClickListener(v -> { Toast.makeText(ctx,name,Toast.LENGTH_LONG).show(); } );

        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditThemeActivity) ctx).alertRemoveTheme(holder);
            }
        });

        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, EditQuestionActivity.class);
                intent.putExtra("THEMENAME", holder.themeName.getText());
                ctx.startActivity(intent);
            }
        });

        holder.itemView.setTag(id);
    }

    public void swapCursor(Cursor newCursor) {
        if(cursor != null) cursor.close();
        cursor = newCursor;
        if(newCursor != null) notifyDataSetChanged();
    }

    public int getItemCount() {
        return QuizSAMPDBHelper.getThemesProfilesCount();
    }
}
