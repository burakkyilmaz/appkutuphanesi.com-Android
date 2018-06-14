package com.example.user.sesiyazyacevirme;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    private final int REQUEST_CODE_SPEECH_INPUT = 100;

    Button konusButon;
    TextView yaziyiGoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        konusButon = (Button) findViewById(R.id.konusButon);
        yaziyiGoster = (TextView) findViewById(R.id.yaziyiGoster);

        konusButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konusDialog();
            }
        });

    }

    private void konusDialog(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        // getDefault metodu telefonun seçili olan dilini döndürür.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Konuş bakalım.");

        try {
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }catch(ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),"Desteklenmiyor",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && data!=null){
                    ArrayList<String> donus = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // intentden dönen veriyi arrayliste atıyoruz

                    yaziyiGoster.setText(donus.get(0));
                }

                break;
            }
        }

    }
}
