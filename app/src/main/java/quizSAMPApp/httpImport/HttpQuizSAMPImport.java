package quizSAMPApp.httpImport;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import quizSAMPApp.database.QuizSAMPDBHelper;
import quizSAMPApp.modele.Question;
import quizSAMPApp.modele.Theme;
import quizSAMPApp.vue.EditThemeActivity;

public class HttpQuizSAMPImport {
    HttpTitle httpTitle ;
    EditThemeActivity ta;
    QuizSAMPDBHelper db;

    public HttpQuizSAMPImport(EditThemeActivity ta) {
        this.ta = ta ;
        this.httpTitle = new HttpTitle() ;
        this.db = new QuizSAMPDBHelper(ta);
    }

    public void execute() {
        httpTitle.execute();
    }

    class HttpTitle  extends AsyncTask<Void, Void, List<Theme>> {
        private final List<Theme> themes = new ArrayList<>();

        private void getPage(String adresseURL) {
            BufferedReader bufferedReader = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(adresseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse (inputStream);
                    doc.getDocumentElement().normalize ();

                    getThemesFromHTTP(doc);
                }
            } catch (Exception ignored) { }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ignored) {}
                }
                if (urlConnection != null) urlConnection.disconnect();
            }
        }

        private void getThemesFromHTTP(Document doc) {
            NodeList quizzList = doc.getElementsByTagName("Quizz");

            for(int i = 0; i < quizzList.getLength(); i++)
            {
                Element quizzNode = (Element) quizzList.item(i) ;
                final String themeName = quizzNode.getAttributeNode("type").getValue();

                NodeList questionList = quizzNode.getElementsByTagName("Question");
                List<Question> q = new ArrayList<>();

                for(int j = 0; j < questionList.getLength(); j++) {
                    Element questionNode = (Element) questionList.item(j);
                    String question = questionNode.getFirstChild().getTextContent().trim();

                    int nbrOfProp = Integer.parseInt(((Element) questionNode.getElementsByTagName("Reponse").item(0)).getAttribute("valeur"));
                    if(nbrOfProp < 5){
                        NodeList propositionList = questionNode.getElementsByTagName("Proposition");
                        String answer[] = {null, null, null, null};
                        for(int k = 0; k < propositionList.getLength(); k++){
                            answer[k] = ((Element) propositionList.item(k)).getFirstChild().getTextContent().trim();
                        }

                        int correctProp = Integer.parseInt(((Element) questionNode.getElementsByTagName("Reponse").item(0)).getAttribute("valeur"));
                        q.add(new Question(question, answer[0], answer[1], answer[2], answer[3], correctProp));
                    }
                }

                themes.add(new Theme(themeName, q));
            }
        }

        public void addToDatabase() {
            for (Theme t: themes) {
                String themeName = t.getTheme();
                try {
                    db.insertTheme(themeName);
                } catch (Exception ignored) {}

                for (Question q: t.getQuestionList()) {
                    String question = q.getQuestion();
                    String ans1 = q.getAnswer1();
                    String ans2 = q.getAnswer2();
                    String ans3 = q.getAnswer3();
                    String ans4 = q.getAnswer4();
                    int cor_ans = q.getCorrect_answer();

                    db.insertQuestion(themeName, question, ans1, ans2, ans3, ans4, cor_ans);
                }
            }
        }

        @Override
        protected List<Theme> doInBackground(Void... params) {
            getPage("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml");
            addToDatabase();
            return themes;
        }

        @Override
        protected void onPostExecute(List<Theme> titles) {
            /*for( String key : titles )
            {
                System.out.println("Element : " + key);
            }*/
        }
    }
}
