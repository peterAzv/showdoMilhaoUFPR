package com.br.showdomilhao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.showdomilhao.R;

public class Splash extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timer = new Thread(){
        
        	public void run(){
	            try{
	                sleep(3000);
	            }catch (InterruptedException e){
	                e.printStackTrace();
	            }finally{
	                Intent janelaInicial = new Intent("com.example.showdomilhao.SHOWDOMILHAO");
	                startActivity(janelaInicial);
	            }
	        }
	    };
	    timer.start();
}
    
    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.finish();
                Intent mainIntent = new Intent(Splash.this, ShowdoMilhao.class);
                Splash.this.startActivity(mainIntent);
            }
        }, 3000); 
    }
}
