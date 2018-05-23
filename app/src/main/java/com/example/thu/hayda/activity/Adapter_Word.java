package com.example.thu.hayda.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thu.hayda.DictionaryModel;
import com.example.thu.hayda.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class AdapterNhanVien extends BaseAdapter {
    Activity context;
    ArrayList<DictionaryModel> list;
    TextView txt_result;
    TextToSpeech textToSpeech;

    public AdapterNhanVien(Activity context, ArrayList<DictionaryModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.wordlist, null);

        final TextView id_word = (TextView) row.findViewById(R.id.id_word);
        TextView txt_ipa = (TextView) row.findViewById(R.id.txt_ipa);
        TextView txt_define = (TextView) row.findViewById(R.id.txt_define);
        txt_result = (TextView) row.findViewById(R.id.txt_result);
        ImageView img_micro = (ImageView) row.findViewById(R.id.img_micro);
        ImageView img_giongmy = (ImageView) row.findViewById(R.id.img_giongmy);


        final DictionaryModel dictionaryModel = list.get(position);
        id_word.setText(dictionaryModel.getWord() + "");
        txt_ipa.setText(dictionaryModel.getIpa());
        txt_define.setText(dictionaryModel.getMean());
        //micro.
        img_micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });

//GIONG MY

        img_giongmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //2
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    textToSpeech.speak(id_word.getText().toString(), textToSpeech.QUEUE_FLUSH, null, null);
//
//                } else
//                    textToSpeech.speak(id_word.getText().toString(), textToSpeech.QUEUE_FLUSH, null);
                textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
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
                            //2
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                textToSpeech.speak(id_word.getText().toString(), textToSpeech.QUEUE_FLUSH, null, null);

                            } else
                                textToSpeech.speak(id_word.getText().toString(), textToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
            }
        });
        return row;
    }

    public void getSpeechInput() {

//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        if (intent.resolveActivity(context.getPackageManager()) != null) {
//            context.startActivityForResult(intent, 10);
//        } else {
//            Toast.makeText(context, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
//        }


//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say something");
//
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
//
//        context.startActivityForResult(intent, 10);

        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            context.startActivityForResult(intent, 10);
        } catch(ActivityNotFoundException e) {
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       super.onActivityResult(requestCode, resultCode, data);

//        switch (requestCode) {
//            case 10:
//                if (resultCode == context.RESULT_OK && data != null) {
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txt_result.setText(result.get(0));
//                }
//                break;
//
//        }
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            txt_result.setText(result.get(0));
        }
//        super.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data);

    }

//    private void Speak() {
//        textToSpeech=new TextToSpeech(Context, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status==TextToSpeech.SUCCESS)
//                {
//                    int result=textToSpeech.setLanguage(Locale.ENGLISH);
////                    if(result==TextToSpeech.LANG_MISSING_DATA|| result==TextToSpeech.LANG_NOT_SUPPORTED)
////                    {
////                        Toast.makeText(MainActivity_LearnSpeak.this, "Language not support", Toast.LENGTH_SHORT).show();
////                    }
//                    textToSpeech.setPitch(0.6f);
//                    textToSpeech.setSpeechRate(1.0f);
//                    speack2();
//                }
//            }
//        });
//        img_giongmy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
//                {
//                    textToSpeech.speak("/I/",textToSpeech.QUEUE_FLUSH,null,null);
//
//                }
//                else
//                    textToSpeech.speak("/I/",textToSpeech.QUEUE_FLUSH,null);
//            }
//        });
//    }

}