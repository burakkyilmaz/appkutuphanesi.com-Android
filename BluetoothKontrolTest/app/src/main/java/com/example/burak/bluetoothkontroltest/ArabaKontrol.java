package com.example.burak.bluetoothkontroltest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

import android.app.AlertDialog;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.view.MotionEvent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class ArabaKontrol extends AppCompatActivity {
    String gonder="";
    Button btnIleri,btnSol,btnSag,btnGeri,btnBasla,btnRandom1;
TextView randtext;
TextView textSonuc;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public ImageButton voice_button;
    public EditText multiline_txt;
    public Button send_bttn;
    public Intent intent;
    public static final int request_code_voice = 1;
    public SpeechRecognizer recognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bluetooth Cihazının adresini alır
        Intent newint = getIntent();
        address = newint.getStringExtra(BluetoothListe.EXTRA_ADDRESS);

        setContentView(R.layout.activity_araba_kontrol);

        btnIleri = (Button)findViewById(R.id.btnIleri);
        btnBasla = (Button)findViewById(R.id.btnBasla);
        btnSol = (Button)findViewById(R.id.btnSol);
        btnSag = (Button)findViewById(R.id.btnSag);
        btnGeri = (Button)findViewById(R.id.btnGeri);
        textSonuc=(TextView) findViewById(R.id.textt);
        btnRandom1=(Button) findViewById(R.id.btnRandom);

        new BTbaglan().execute();

        /* btnIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ileri();
            }
        });

        btnSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sol();
            }
        });

        btnSag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sag();
            }
        });

        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geri();
            }
        }); */
final String[] random={"Kedi","Köpek","İnek","Baykuş","Maymun","At","Aslan","Yılan","Balık"};
        btnRandom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int idx=new Random().nextInt(random.length);
               String rndDeger=(random[idx]);
               Toast.makeText(getApplicationContext(),rndDeger+" Hayvanına git!",Toast.LENGTH_SHORT).show();
               randtext= (TextView)findViewById(R.id.randtext);
               randtext.setText(rndDeger+" Hayvanına Git!");
                randtext.setTextColor(Color.GREEN);
randtext.setTextSize(35);

            }
        });



        btnIleri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ileri();
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                }
                return false;
            }
        });

        btnSol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        sol();
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                }
                return false;
            }
        });


        btnSag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        sag();
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                }
                return false;
            }
        });


        btnGeri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        geri();
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                }
                return false;
            }
        });

        btnBasla.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                      basla();
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                }
                return false;
            }
        });


        voice_button = (ImageButton)findViewById(R.id.voice_button);
        multiline_txt = (EditText)findViewById(R.id.voice_txt);


        multiline_txt.setEnabled(false);

        voice_button.setOnClickListener(new OnClickListener() { // image button a t�klama olay�

            @Override
            public void onClick(View v) {

                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // intent i olu�turduk sesi tan�yabilmesi i�in
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                try{
                    startActivityForResult(intent, request_code_voice);  // activityi ba�latt�k belirledi�imiz sabit de�er ile birlikte
                }catch(ActivityNotFoundException e)
                {
                    // activity bulunamad��� zaman hatay� g�stermek i�in alert dialog kulland�m
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ArabaKontrol.this);
                    builder.setMessage("Uzgunuz Telefonunuz bu sistemi desteklemiyor!!!")
                            .setTitle("HATA!!")
                            .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case request_code_voice: {

                if (resultCode == RESULT_OK && data != null)
                {multiline_txt.setText("");
                    textSonuc.setText("");
                    // intent bo� olmad���nda ve sonu� tamam oldu�u anda konu�may� al�p listenin i�ine att�k
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    multiline_txt.setText(speech.get(0));
                    parcala();
                }
                break;
            }

        }


    }


    public void parcala()
    {
        String ayir = multiline_txt.getText().toString();
        String[] bosluk;
        bosluk = ayir.split("\\s+");

        for(int i=0;i<bosluk.length;i++)
        {

            if(bosluk[i].equals("sağ")  || bosluk[i].equals("Sağ") )
            {
                sag();
            }

            if(bosluk[i].equals("sol")  || bosluk[i].equals("Sol") )
            {
                sol();
            }

            if(bosluk[i].equals("ileri") || bosluk[i].equals("İleri"))
            {
               ileri();
            }

            if(bosluk[i].equals("geri") || bosluk[i].equals("Geri") )
            {
                geri();
            }

            if(bosluk[i].equals("başla") || bosluk[i].equals("başlat"))
            {
               basla();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Disconnect();
    }

    //Baglantı kurma ve Socket vasıtasyıla veriyi gonderme
    private class BTbaglan extends AsyncTask<Void,Void,Void>{
        private boolean ConnectSuccess = true;
        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(ArabaKontrol.this,"Baglanıyor...","Lütfen Bekleyin");
        }

        // https://gelecegiyazanlar.turkcell.com.tr/konu/android/egitim/android-301/asynctask
        @Override
        protected Void doInBackground(Void...devices){
            try {
                if(btSocket == null || !isBtConnected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(!ConnectSuccess){
                msg("Baglantı Hatası, Lütfen Tekrar Deneyin");
                finish();
            } else {
                msg("Baglantı Basarılı");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    //Baglantıyı Sonlandırma
    private void Disconnect(){
        if(btSocket!=null){
            try {
                btSocket.close();
            } catch (IOException e){
                msg("Error");
            }
        }
        finish();
    }

    //Hata mesajı
    private void msg(String s){
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }


    //Butona basılınca yapılacak hareket kodları
    private void ileri(){
        if(textSonuc.getText().toString().equals("Komutları söyledikten sonra(sağ,sol,ileri,geri) BAŞLA komutunu söyleyin!"))
        {
            textSonuc.setText("");
        }
        if(btSocket!=null){

            try {
                String gecici=textSonuc.getText().toString();
                textSonuc.setText(gecici+"->İleri");
                btSocket.getOutputStream().write("A".toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }

        }



    }

    private void sol(){
        if(textSonuc.getText().toString().equals("Komutları söyledikten sonra(sağ,sol,ileri,geri) BAŞLA komutunu söyleyin!"))
        {
            textSonuc.setText("");
        }
        if(btSocket!=null){

            try {
                String gecici=textSonuc.getText().toString();
                textSonuc.setText(gecici+"->Sol");
                btSocket.getOutputStream().write("C".toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }

        }
    }

    private void sag(){
        if(textSonuc.getText().toString().equals("Komutları söyledikten sonra(sağ,sol,ileri,geri) BAŞLA komutunu söyleyin!"))
        {
            textSonuc.setText("");
        }
        if(btSocket!=null){

            try {
                String gecici=textSonuc.getText().toString();
                textSonuc.setText(gecici+"->Sağ");
                btSocket.getOutputStream().write("D".toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }

        }
    }

    private void geri(){
        if(textSonuc.getText().toString().equals("Komutları söyledikten sonra(sağ,sol,ileri,geri) BAŞLA komutunu söyleyin!"))
        {
            textSonuc.setText("");
        }
        if(btSocket!=null){

            try {
                String gecici=textSonuc.getText().toString();
                textSonuc.setText(gecici+"->Geri");
                btSocket.getOutputStream().write("B".toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }

        }

    }

    private void basla(){
        if(btSocket!=null){

            try {
                String gecici=textSonuc.getText().toString();
                textSonuc.setText(gecici+"->Basla");
                btSocket.getOutputStream().write("S".toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }
            MyThread thread=new MyThread();
            thread.start();
            textSonuc.setText("Komutları söyledikten sonra(sağ,sol,ileri,geri) BAŞLA komutunu söyleyin!");

        }
    }





}
