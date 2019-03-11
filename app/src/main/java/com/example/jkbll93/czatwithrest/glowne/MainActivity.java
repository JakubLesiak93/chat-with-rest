package com.example.jkbll93.czatwithrest.glowne;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jkbll93.czatwithrest.R;
import com.example.jkbll93.czatwithrest.wiadomosci.Wiadomosci;
import com.example.jkbll93.czatwithrest.wiadomosciJSONParser.WiadomosciParserJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText eT;
    EditText eNick;
    TextView output;
    List<HttpRequestTask> tasks;
    List<Wiadomosci> listaWiadomosci;
    Timer repeatTask = new Timer();
    TimerTask timerTask;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //skrolowanie w gore i w dol
        output = findViewById(R.id.receive);
        output.setMovementMethod(new ScrollingMovementMethod());
    
        eT = findViewById(R.id.mess);
        eNick = findViewById(R.id.nick);
        output = findViewById(R.id.receive);
    
        tasks = new ArrayList<>();
        timerTask = new TimerTask() {  // funkcja zapewniajaca ciagle odswiezanie
            @Override
            public void run() {
                if (isOnline()) {
                    if(i>1000000)
                    {
                        repeatTask.cancel();
                    }
                    //requestData("http://192.168.0.18:8080/chat/"); // zbieranie obecnego stanu serwera // testowałem pod moim telefonem i IP i śmiga jak złoto. - Piotr
                   requestData("http://10.10.201.21:8080/chat/"); // test Jakuba
                }
            }
        };
        repeatTask.schedule(timerTask,0,1000);
    }

// komentar dotyczacy poprawek - musimy poprawic pierwsze odbieranie z serwera oraz obsluge spacji i znakow specjalnych - fixed chyba
    
	//autor: Jakub Lesiak
    //metoda sprawdza czy jesteśmy online
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }

	//autor: Jakub Lesiak
    // pobiera i wysyla dane do uri
    public void refresh (View v){
        if (isOnline()){
            String nick = eNick.getText().toString();
            
            if (nick.length() != 0 ) {
                String what = eT.getText().toString();
                if(what.length() != 0) {
                    what = what.replaceAll("%", "%25");
                    what = what.replaceAll(" ", "%20");
                    what = what.replaceAll("/", "slash");
                    
                    String nameMess = nick + "/" + what;
                    
                    //requestData("http://192.168.0.18:8080/chat/" + nameMess); // testowałem pod moim telefonem i IP i śmiga jak złoto. 10.0.2.2:8080 - Piotr
                    requestData("http://10.10.201.21:8080/chat/" + nameMess); // test Jakuba
                }else {
                    Toast.makeText(this, "Wpisz wiadomosc", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Wpisz nik", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText( this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }
    }
    
	//autor: Jakub Lesiak
    public void fresh (View v)
    {
        if(isOnline()){
            //requestData("http://192.168.0.18:8080/chat/"); // testowałem pod moim telefonem i IP i śmiga jak złoto. - Piotr
           requestData("http://10.10.201.21:8080/chat/"); // test Jakuba
        }
        else{
            Toast.makeText( this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }
    }
    
	//autor: Jakub Lesiak
    //wykonuje HttpRequesTask
    private void requestData(String uri) {
        HttpRequestTask task = new HttpRequestTask();
        task.execute(uri);
    }

	//autor: Jakub Lesiak
    // wrzuca na ekran wiadomosc
    protected void updateDisplay(){
        if (listaWiadomosci != null) {
            for (Wiadomosci wiadomosci : listaWiadomosci) {
                output.append(wiadomosci.getNazwa() + ": " + wiadomosci.getTresc() + "\n");
            }
        }
    }
    
	//autor: Jakub Lesiak
    // klasa do tworzenia wątku który pobiera ostatnio wrzucone dane do serwera
    private class HttpRequestTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            listaWiadomosci = WiadomosciParserJSON.parserJSON(s);
            
            tasks.remove(this);
            output.setText(null); // czyszczenie starego ekranu
            updateDisplay();
        }
    }
}
