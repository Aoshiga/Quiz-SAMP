package quizSAMPApp.modele;

import java.util.List;

public class Theme {
    private String theme;
    private List<Question> questionList;

    public Theme(String theme, List<Question> questionList) {
        this.theme = theme;
        this.questionList = questionList;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
