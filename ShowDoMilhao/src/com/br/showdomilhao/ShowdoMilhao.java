package com.br.showdomilhao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.showdomilhao.R;

public class ShowdoMilhao extends Activity {

	public final Context context = this;
	public static MediaPlayer media;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		media = MediaPlayer.create(this, R.raw.intro); 
	}
	
	@Override
	protected void onResume() {
	    super.onResume(); 
		media = MediaPlayer.create(this, R.raw.intro); 
	    media.start();    
	}
	
	@Override
	protected void onPause() {
	    super.onPause(); 
	    media.stop();       
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}
	
	public void novoJogo(View view){
		Intent intent = new Intent(ShowdoMilhao.this, ControlGaming.class );
		try{
			ContextoDados bd = new ContextoDados(this);
			SQLiteDatabase db = new ContextoDados(this).getReadableDatabase();
			startActivity(intent);
		}catch(Exception e){
			Log.e("Erro no método novoJogo :", e.toString());
			startActivity(intent);
		}			
	}

	public void leaderBoard(View view){
		Intent intent = new Intent(ShowdoMilhao.this, LeaderBoardAction.class );
		startActivity(intent);

		//MELHORAR
		//		Swarm.showLeaderboards();
	}
	
	public void sair(View view){
		onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
