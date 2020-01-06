package com.example.dailynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<NewsItem> news;
    private RecyclerView recView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recView = findViewById(R.id.recView);
        adapter = new RecyclerViewAdapter(this);

        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(adapter);

        news = new ArrayList<>();

        GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask();
        getDataAsyncTask.execute();
    }

    private void initXMLPullParser(InputStream inputStream){
        Log.d(TAG, "initXMLPullParser: Parser");

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG,null,"rss");
            while (parser.next() != XmlPullParser.END_TAG){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }
                parser.require(XmlPullParser.START_TAG,null,"channel");
                while (parser.next() != XmlPullParser.END_TAG){
                    if(parser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }
                    if(parser.getName().equals("item")){
                        parser.require(XmlPullParser.START_TAG,null,"item");

                        String title = "";
                        String desc = "";
                        String link = "";
                        String date = "";
                        String imgUrl = "";

                        while (parser.next() != XmlPullParser.END_TAG){
                            if(parser.getEventType() != XmlPullParser.START_TAG){
                                continue;
                            }
                            String tagName = parser.getName();
                            if(tagName.equals("title")){
                                Log.d(TAG, "initXMLPullParser: Title");
                                title = getContent(parser,"title",0);
                            }else if(tagName.equals("link")){
                                Log.d(TAG, "initXMLPullParser: Link" );
                                link = getContent(parser,"link",0);
                            }else if(tagName.equals("pubDate")){
                                Log.d(TAG, "initXMLPullParser: Date");
                                date = getContent(parser,"pubDate",0);
                            }else if(tagName.equals("description")){
                                Log.d(TAG, "initXMLPullParser: Description");

                                String imgContent = "", descContent ="";
                                String content = "";
                                int start,end;
                                if((parser.next() == XmlPullParser.TEXT)) {
                                    Log.d(TAG, "initXMLPullParser: " + parser.getText());
                                    content = parser.getText();

                                    start = content.indexOf("https");
                                    end = content.lastIndexOf(".jpg");
                                    imgContent = content.substring(start);
                                    Log.d(TAG, "start: " + start + " end: " + end + "\nurl: " + imgContent);
                                    imgUrl = imgContent;

                                    start = content.indexOf("<div>", content.indexOf("<div>") + 1) + 5;
                                    end = content.indexOf("</div>");
                                    descContent = content.substring(start,end);
                                    Log.d(TAG, "start: " + start + "end " + end + "desc " + descContent );
                                    desc = descContent;

                                }
                                //imgUrl = getContent(parser,"description",1);
                                //desc = getContent(parser,"description",2);
                                parser.next();
                            }
                            else {
                                skipTag(parser);
                            }
                        }
                        NewsItem newsItem = new NewsItem(title,desc,link,date,imgUrl);
                        news.add(newsItem);
                    }else {
                        skipTag(parser);
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContent(XmlPullParser parser , String tagName,int count){
        try {

            String content = "";

            parser.require(XmlPullParser.START_TAG,null,tagName);

            if(count == 0){
                if(parser.next() == XmlPullParser.TEXT){
                    content = parser.getText();
                    parser.next();
                    Log.d(TAG, "getContent: " + parser.getName());
                }
            }
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {

        if(parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }

        int number = 1;
        while (number != 0){
            switch (parser.next()){
                case XmlPullParser.START_TAG:
                    number++;
                    break;
                case XmlPullParser.END_TAG:
                    number--;
                    break;
                default:
                    break;
            }
        }
    }

    private InputStream getInputStream(){
        try {
            URL url = new URL("https://rss.app/feeds/hqJl8C3KMzY9FYsU.xml");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetDataAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStream();
            initXMLPullParser(inputStream);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setNews(news);
            super.onPostExecute(aVoid);
        }
    }
}
