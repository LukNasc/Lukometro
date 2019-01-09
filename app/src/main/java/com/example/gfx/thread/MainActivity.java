package com.example.gfx.thread;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvSegundos,tvMinutos,tvHoras, tvIndicador;
    private Button  btnIniciar, btnResetar;
    private MyThread iniciar;
    private int i = 0;
    private int cont = 00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Jiselle Linda", LENGTH_SHORT).show();

        tvSegundos = findViewById(R.id.tvSegundos);
        tvMinutos = findViewById(R.id.tvMinutos);
        tvHoras = findViewById(R.id.tvHoras);

        btnResetar = findViewById(R.id.btnReset);
        btnIniciar = findViewById(R.id.btnIniciar);
        tvIndicador = findViewById(R.id.tvInidcador);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnIniciar.getText().toString().equals("INICIAR")){
                    iniciar = new MyThread(false, false);
                    if(!iniciar.isInterrupted()){
                       iniciar.interrupt();
                    }
                    iniciar.start();

                    btnIniciar.setText("PARAR");
                }else{
                    iniciar = new MyThread(true, false);
                    iniciar.start();
                    btnIniciar.setText("INICIAR");

                    if(!iniciar.isInterrupted()){
                        iniciar.interrupt();
                    }
                }
                Log.i("onClick","O BOTÃO INICIAR/PARAR FOI PRESSIONADO");
            }
        });

        btnResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar = new MyThread(false, true);
                if(!iniciar.isInterrupted()){
                    iniciar.interrupt();
                }
                iniciar.start();
                btnIniciar.setText("INICIAR");
            }
        });

        try {
            TextView txtViewVersao = findViewById(R.id.tvVersao);
            PackageInfo pInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0);
            String version = pInfo.versionName;
            txtViewVersao.setText("v" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    class MyThread extends  Thread{
        private boolean stop;
        private boolean reset;
        private final int TEMPO = 1000;
        private int minutos = 0, horas = 0;


        MyThread(boolean stop, boolean reset){
            this.stop = stop;
            this.reset = reset;
        }

        @Override
        public void run() {
            super.run();
            if(i != 0) i=0;
            if(reset){
                cont = 0;
                minutos = 0;
                horas = 0;
                i = 10;
                interrupt();
                return;
            }
            while (i == 0){
                if(!this.stop){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(cont == 60){
                                cont = 0;
                                minutos++;
                                tvMinutos.setText(""+(minutos));
                                if(minutos == 60){
                                    minutos = 0;
                                    tvMinutos.setText(""+00);
                                    horas++;
                                    tvHoras.setText(""+(horas));
                                }
                            }
                            tvSegundos.setText(""+cont);
                        }
                    });
                    try {
                        Thread.sleep(TEMPO);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cont++ ;
                }else{
                    i = 10;
                    interrupt();
                }
            }
            interrupt();
        }
    }
}
