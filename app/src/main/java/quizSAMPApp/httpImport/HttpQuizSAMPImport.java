package quizSAMPApp.httpImport;

import android.os.AsyncTask;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class HttpQuizSAMPImport {
    HttpTitle httpTitle ;
    MainActivity ma;

    public HttpGrandSite(MainActivity ma) {
        this.ma = ma ;
        httpTitle = new HttpTitle() ;
    }

    public void execute() {
        httpTitle.execute() ;
    }

    class HttpTitle  extends AsyncTask<Void, Void, List<String>> {
        private List<String> titles = new ArrayList<String>();

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


                    NodeList nodeList ;
                    NodeList paysList = doc.getElementsByTagName("France");
                    Element franceNode = (Element) paysList.item(0) ;
                    NodeList sitesFranceList = franceNode.getElementsByTagName("Nom") ;

                    for(int i = 0; i < sitesFranceList.getLength(); i += 1)
                    {
                        final String siteName = sitesFranceList.item(i).getFirstChild().getNodeValue();
                        titles.add(siteName);
                    }

                }
                else { }

            } catch (Exception e) { }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) { }
                }
                if (urlConnection != null) urlConnection.disconnect();
            }
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            getPage("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/GrandsSites.xml");
            return titles;
        }

        @Override
        protected void onPostExecute(List<String> titles) {
            ma.adapter = new MyAdapter(ma, titles);
            ma.vueGrandsSitesDeFrance.setAdapter(ma.adapter);
        }
    }

}
