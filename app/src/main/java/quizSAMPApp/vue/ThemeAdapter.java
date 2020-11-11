package quizSAMPApp.vue;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsamp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import quizSAMPApp.database.QuizSAMPDBDescription;
import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Theme;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    private List<Theme> themeList;
    private Context ctx;
    private Cursor cursor;

    public ThemeAdapter(Context context, Cursor c) {
        //this.themeList;
        this.cursor = c;
        this.ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView themeName;
        public Button bStart;

        public ViewHolder(View v) {
            super(v);
            themeName = v.findViewById(R.id.t_themeName);
            bStart = v.findViewById(R.id.b_start);
        }
    }

    public ThemeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.theme_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        /*final Theme t = themeList.get(pos);
        holder.themeName.setText(t.getTheme());

        if(t.getQuestionList().isEmpty()) {
            holder.bStart.setVisibility(View.GONE);
            //holder.themeName.setVisibility(View.INVISIBLE);
        }

        holder.bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, QuestionActivity.class);
                intent.putExtra("QUESTION", (Serializable) t.getQuestionList());
                ctx.startActivity(intent);
            }
        });*/
        if(!cursor.moveToPosition(pos)) return;

        //final Theme t = themeList.get(pos);
        String name = cursor.getString(cursor.getColumnIndex(QuizSAMPDBDescription.COLUMN_THEME_NAME));
        int id = cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
        holder.themeName.setText(name);
        /* Need to be change later */
        holder.bStart.setVisibility(View.GONE);

        holder.themeName.setOnClickListener(v -> { Toast.makeText(ctx,name,Toast.LENGTH_LONG).show(); } );

        holder.bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, QuestionActivity.class);
                //intent.putExtra("QUESTION", (Serializable) t.getQuestionList());
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
        int profilesCount = QuizSAMPDBHelper.getProfilesCount();
        return profilesCount;
    }

    @Override
    public long getItemId(int position)
    {
        return cursor.getInt(cursor.getColumnIndex(QuizSAMPDBDescription._ID));
    }

    /*
    private List<Theme> themeList;

    public ThemeAdapter(List<Theme> themeList) {
        this.themeList = themeList;
    }

    @Override
    public int getCount()
    {
        return themeList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, parent, false);
        } else {
            view = convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), QuestionActivity.class);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.themeName)).setText(themeList.get(position).getTheme());

        return view;
    }
    */
}
