package quizSAMPApp.modele;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import quizSAMPApp.database.QuizSAMPDBDescription;
import quizSAMPApp.database.QuizSAMPDBHelper;

public class Themes {
    private List<Theme> themes;

    public Themes() {
        themes = new LinkedList<Theme>();
        init();
    }

    private void init() {
        List<Question> questionsList = new ArrayList<>();

        questionsList.add(new Question("Le Master en Informatique peut-il se faire en alternance ?", "Oui", "Non", null, null, 1));
        questionsList.add(new Question("Qui est le responsable du CMI ? ", "B. Tatibouët", "A. Giorgetti", "F. Dadeau", null, 3));
        questionsList.add(new Question("Quelle est la durée maximale d'un stage ?", "12 semaines", "3 mois", "6 mois", "8 mois", 4));
        add(new Theme("Informatique", questionsList));

        add("Nourriture", new ArrayList<>());
        add(new Theme("Musique", new ArrayList<>()));
    }

    public List<Theme> getAll() {
        return themes;
    }

    public Theme get(int pos) {
        return themes.get(pos);
    }

    public void add(Theme t) {
        themes.add(t);
    }

    public void add(String s, List<Question> q) {
        themes.add(new Theme(s, q));
    }

    public void add(int pos, Theme t) {
        themes.add(pos, t);
    }

    public void remove(Theme t) {
        themes.remove(t);
    }

    public void remove(String s, List<Question> q) {
        themes.remove(new Theme(s, q));
    }

    public void remove(int pos) {
        themes.remove(pos);
    }
}
