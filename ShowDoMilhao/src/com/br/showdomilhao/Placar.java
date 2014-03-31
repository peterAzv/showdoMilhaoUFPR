package com.br.showdomilhao;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.showdomilhao.R;


public class Placar extends Activity {
	
	private Timer timerAtual = new Timer();
	private TimerTask task;
	private final Handler handler = new Handler();
	    
	public String txtDescPlacar;
	public String txtExibe;
	public int sTempo;
	public int tPlacar;
	public int rodada;
	MediaPlayer somRodada;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placar);
		Intent intent = getIntent();
		txtExibe = intent.getStringExtra("txtExibe");
		sTempo = intent.getIntExtra("sTempo", 3); //Exibe o placar por tanto tempo
		tPlacar = intent.getIntExtra("tPlacar", 1); //Tipo de Placar exibido
		rodada = intent.getIntExtra("rodada", 0); 
		switch (rodada) {
		case 1:
			somRodada = MediaPlayer.create(this, R.raw.rodada1); 
			sTempo = 8;
			break;
		case 6:
			somRodada = MediaPlayer.create(this, R.raw.rodada2);
			sTempo = 9;
			break;
		case 11:
			somRodada = MediaPlayer.create(this, R.raw.rodada3);
			sTempo = 8;
			break;
		case 16:	
			somRodada = MediaPlayer.create(this, R.raw.rodada4);
			sTempo = 8;
			break;
		case 0:	 
			somRodada = MediaPlayer.create(this, R.raw.perg); 
			break;
		case 50:	 
			somRodada = MediaPlayer.create(this, R.raw.perdeu); 
			sTempo = 4;
			break;
		default:
			somRodada = MediaPlayer.create(this, R.raw.perg); 
			break;
		}
		TextView txtTextPlacar = (TextView)findViewById(R.id.txtDescPlacar);
		txtTextPlacar.setText(txtExibe);
		ImageView imgPlacar = (ImageView)findViewById(R.id.imageView1);
		
		somRodada.start();

		if (tPlacar==1){ //Se for um placar de continuacao
			imgPlacar.setImageResource(R.drawable.imgbarrasgod);
		} else if (tPlacar==2){ //Se for um placar de Erro
			imgPlacar.setImageResource(R.drawable.imgbarrasgod);
		} else if (tPlacar==3){ //Se for um placar de Vencedor
			MediaPlayer media = MediaPlayer.create(this, R.raw.ummilhao_); //Musica de introdução
			imgPlacar.setImageResource(R.drawable.winner);
			media.start();
		}
		ativaTimer(sTempo);
	}
	

	@Override
	protected void onPause() {
	    super.onPause(); 
	    somRodada.release();       
	}
	
	 private void ativaTimer(int segundos){
		 	int seg = segundos * 1000;
	        task = new TimerTask() {
	            public void run() {
	                    handler.post(new Runnable() {
	                            public void run() {
	                                finish();
	                                timerAtual.cancel();
	                            }
	                   });
	            }};
	           
	            timerAtual.schedule(task, seg, seg); 
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
