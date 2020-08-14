package com.example.jhirsch.bullshitgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Locale;

public class Main extends AppCompatActivity {

    TextToSpeech textToSpeech;

    String text = null;
    String text2 = null;
    TextView textView;
    TextView textView2;
    Button neuladen;
    Button kopieren;

    private boolean isreading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.GERMANY);
                }
            }
        });

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.Bullshit);


        loadWebsite(this.getWindow().getDecorView());




        neuladen = findViewById(R.id.neuladen);
        kopieren = findViewById(R.id.kopieren);
    }


    public void read(View view){
        Button read = findViewById(R.id.read);
        if(isreading){
            read.setText("Lesen");
            textToSpeech.stop();
        }else{
            read.setText("Stop");
            textToSpeech.speak(textView2.getText().toString(), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, null);

        }
        isreading = !isreading;
    }


    public void loadWebsite(View view){
        new Connection().execute();
        while (this.text == null || this.text2 == null);
//        String text2 = "";

        textView.setText(text);
//        int i;
//        for(i = 0;  i < this.text2.length(); i++){
//            System.out.println("For");
//            text2 += this.text2.charAt(i);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

            textView2.setText(text2);
    }

    public void copy(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Bullshit",text2));
    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            connect();
            return null;
        }

    }

    private void connect() {
        try {
            Document doc = Jsoup.connect("http://homepageberatung.at/cont/junk/bullshit_generator/index.php").get();
            if (doc == null) {
                System.out.println("dasdadadasdasadasdas");
            }

            Elements texte = doc.select("p");
            if(texte.size() < 1) {
                this.text = "Text konnte nicht gefunden werden.";
            }
            this.text = texte.get(0).text();
            this.text2 = texte.get(1).text();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
