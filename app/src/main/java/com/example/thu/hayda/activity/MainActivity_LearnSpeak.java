package com.example.thu.hayda.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.thu.hayda.AdapterNhanVien;
import com.example.thu.hayda.Database;
import com.example.thu.hayda.DictionaryModel;
import com.example.thu.hayda.R;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity_LearnSpeak extends AppCompatActivity {
    final String DATABASE_NAME = "thth.sqlite";
    SQLiteDatabase database;
    String id_image = "";
//    TextView txt_redirct;
    ImageView img_speak, img_image,btnvideo;
    ListView recyclerView;
    ArrayList<DictionaryModel> list;
    AdapterNhanVien adapter;
    TextToSpeech textToSpeech;
    String _value="";
    MediaPlayer mediaPlayer;
//    String stream = "http://k003.kiwi6.com/hotlink/15cdm6tuo5/m_I.mp3";
//    String stream = "http://k003.kiwi6.com/hotlink/hthwmtbvea/2.mp3";
    String stream = "http://mic.duytan.edu.vn:86/ncs.mp3";
    String Link_video = "http://mic.duytan.edu.vn:86/ncs.mp3";
    Boolean prepare = false;
    Boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__learn_speak);
        AddCOntrols();
//        img_speak.setEnabled(false);


        Intent t = getIntent();
         _value = getIntent().getStringExtra("id_Ipa");
        int _value2 = Integer.valueOf(_value);

//        readData();
//        readData1();
//        Speak();




        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT image,link_Audio,link_Video FROM Ipa where id_Ipa=? ", new String[]{_value});
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            byte[] anh = cursor.getBlob(0);
            stream = (cursor.getString(1));
            Link_video = (cursor.getString(2));
            Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(anh, 0, anh.length);
            img_image.setImageBitmap(bmHinhDaiDien);
//            txt_redirct.setText(ha);

        }

        //speak
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(stream);
        mediaPlayer.start();
        img_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                speack2();
//                Toast.makeText(MainActivity_LearnSpeak.this, "haha", Toast.LENGTH_SHORT).show();
//                if (started) {
//                    started = false;
//                    mediaPlayer.pause();
//                } else {
                    started = true;
                    mediaPlayer.start();
//                }
            }
        });
        readData1();
        ClickVideo();
    }

    private void ClickVideo() {
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity_LearnSpeak.this, "ho ho", Toast.LENGTH_SHORT).show();
                Intent t=new Intent(MainActivity_LearnSpeak.this,Main_YoutubeAPI.class);
                t.putExtra("Link_video", Link_video);
                startActivity(t);
            }
        });
    }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepare = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return prepare;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mediaPlayer.start();
//            img_speak.setEnabled(true);
            img_speak.setEnabled(true);
        }
    }

    private void Speak() {
        textToSpeech = new TextToSpeech(MainActivity_LearnSpeak.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
//                    if(result==TextToSpeech.LANG_MISSING_DATA|| result==TextToSpeech.LANG_NOT_SUPPORTED)
//                    {
//                        Toast.makeText(MainActivity_LearnSpeak.this, "Language not support", Toast.LENGTH_SHORT).show();
//                    }
                    textToSpeech.setPitch(0.6f);
                    textToSpeech.setSpeechRate(1.0f);
                    speack2();
                }
            }
        });

    }

//    @Override
//    protected void onDestroy() {
////        if (textToSpeech != null) {
////            textToSpeech.stop();
////            textToSpeech.shutdown();
////        }
//        super.onDestroy();
//        if (prepare) {
//            mediaPlayer.release();
//        }
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(started)
//        {
//            mediaPlayer.pause();
//        }
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(started)
//        {
//            mediaPlayer.start();
//        }
//    }
    private void speack2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak("/I/", textToSpeech.QUEUE_FLUSH, null, null);

        } else
            textToSpeech.speak("/I/", textToSpeech.QUEUE_FLUSH, null);
    }

    private void AddCOntrols() {
        img_speak = (ImageView) findViewById(R.id.img_speak);
        img_image = (ImageView) findViewById(R.id.img_image);
        btnvideo = (ImageView) findViewById(R.id.btnvideo);
//        txt_redirct = (TextView) findViewById(R.id.txt_redirct);
        recyclerView = (ListView) findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new AdapterNhanVien(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void readData1() {
       try {
           database = Database.initDatabase(this, DATABASE_NAME);
//           Cursor cursor = database.rawQuery("select * from word ", null);
           Cursor cursor = database.rawQuery("SELECT * FROM word  where id_ipa=? ", new String[]{_value});
           list.clear();
           for (int i = 0; i < cursor.getCount(); i++) {
               cursor.moveToPosition(i);
               String word = cursor.getString(2);
               String ipa = cursor.getString(3);
               String mean = cursor.getString(4);
               list.add(new DictionaryModel(ipa, word, mean));


           }
           adapter.notifyDataSetChanged();
       }
       catch (Exception e)
       {e.printStackTrace();}
//        Cursor cursor = database.rawQuery("SELECT * FROM word  where id_ipa=? ", new String[]{_value});
//        Cursor cursor = database.rawQuery("SELECT image,link_Audio,link_Video FROM Ipa where id_Ipa=? ", new String[]{_value});

    }

//    private void readData() {
//        String ha = "a";
//        database = Database.initDatabase(this, DATABASE_NAME);
//        Cursor cursor = database.rawQuery("SELECT redirection FROM image where id_image=? ", new String[]{});
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            ha = (cursor.getString(0));
//
//
//        }
//        txt_redirct.setText(ha);
//    }



}

//BAO CAO 15-18.
