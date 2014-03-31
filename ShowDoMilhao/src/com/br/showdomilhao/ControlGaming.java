package com.br.showdomilhao;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.showdomilhao.R;

public class ControlGaming extends Activity { 
	
	private final Handler handler = new Handler();
	public static MediaPlayer media;

	public String nomeJogador;
	public String pontos;
	public int nPergunta = 1; 
	public int nDificult = 1; 
	public int nResposta;    
	final Random numRandomico = new Random();
	int rodada = 1;

	//Variaveis de ajuda

	public String extras;
	int ajudaPulo = 3;
	int ajudaCartas = 1;
	
	public int[] PArray = new int[100];	
	public Alternativa[] AArray = new Alternativa[3];	
	
	PerguntaAtual PgAtual = new PerguntaAtual();	
	Condicao CAtual = new Condicao();

	TextView txtPergunta;
	Button btnAlt1, btnAlt2, btnAlt3, btnAlt4, btnAcertar, btnErrar, btnParar, btnPulo, btnCartas;
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            break;
	        case DialogInterface.BUTTON_NEGATIVE:
	            break;
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gaming);
		media = MediaPlayer.create(this, R.raw.fundosus); 

		Intent intent = getIntent();
		
		txtPergunta = (TextView) findViewById(R.id.pnlpergunta);
		btnAlt1 = (Button) findViewById(R.id.btnresp1);
		btnAlt2 = (Button) findViewById(R.id.btnresp2);
		btnAlt3 = (Button) findViewById(R.id.btnresp3);
		btnAlt4 = (Button) findViewById(R.id.btnresp4);
		btnAcertar = (Button) findViewById(R.id.btnAcertar);
		btnErrar = (Button) findViewById(R.id.btnErrar);
		btnParar = (Button) findViewById(R.id.btnParar);
		btnPulo = (Button) findViewById(R.id.btnPulo);
		btnCartas = (Button) findViewById(R.id.btnCartas);
		Rodada();
	}
	
	@Override
	protected void onResume() {
	    super.onResume(); 
		media = MediaPlayer.create(this, R.raw.fundosus); 
	    media.start();    
	}
	
	@Override
	protected void onPause() {
	    super.onPause(); 
	    media.stop();       
	}

	//Ao apertar Parar
	public void onParar() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Parar")
	        .setMessage("Você tem certeza que deseja parar?")
	        .setPositiveButton("Sim", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	Timer(1);
	        }
	    })
	    .setNegativeButton("Não", null)
	    .show();
	}

	//Ao apertar botão Pular
	public void onPular() {
		if(ajudaPulo!=0){
		    new AlertDialog.Builder(this)
		        .setIcon(R.drawable.puloselected)
		        .setTitle("Pular")
		        .setMessage("Deseja pular pergunta? Você possui ainda "+ ajudaPulo +" Pulo(s)")
		        .setPositiveButton("Pular", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	Timer(1);
		        }
		    })
		    .setNegativeButton("Voltar", null)
		    .show();
		}else{
			Toast.makeText(this, "Você não possui mais pulos!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onCartas() {
		if(ajudaCartas!=0){
			ajudaCartas--;
			CharSequence cartas[] = new CharSequence[] {"Carta 1", "Carta 2", "Carta 3", "Carta 4"};
			final int cartasSg[] = new int[] {0, 1, 2, 3};
			embaralhar(cartasSg);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Cartas");
		        builder.setItems(cartas, new DialogInterface.OnClickListener() {		        
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	int selecionada = cartasSg[which];
					Toast.makeText(ControlGaming.this, "Você selecionou a carta: " + selecionada, Toast.LENGTH_SHORT).show();
		        	if(selecionada != 0){
		        		retiraIncorreta(selecionada);
		        	}
		        }
		    })
		    .setNegativeButton("Voltar", null)
		    .show();
		}else{
			Toast.makeText(this, "Você não possui mais Cartas!", Toast.LENGTH_SHORT).show();
		}
	}

	//Embaralha cartas
	public static void embaralhar(int[] ar){
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	}
	
	public void onSalvar(String pontos) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Você conseguiu " + pontos + " Pontos!");
		alert.setMessage("Digite seu nome para guardar sua pontuação.");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String nome = input.getText().toString();
      		SalvaScore(nome);
		  }
		});
		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}
	
	public void Rodada(){
			rodada = nPergunta;
		
			if(nPergunta % 5 == 0) {
				nDificult++; // Dificuldade
			}
			AtualizarCondicao(nPergunta);
			ExibePlacar("Valendo " + CAtual.Acertar + " Reais", 4,1, rodada); 
			
			do {
				PgAtual = TrazPergunta(nDificult);
			} while (PgAtual.ID==0);
			recolorirbotoes();  //Redefine a aparencia dos botoes
			
			AArray = TrazAlternativas(PgAtual); //Trouxe Alternativas populando o Array
			txtPergunta.setText(PgAtual.textoPergunta);
			btnAlt1.setText(AArray[0].textoAlternativa);
			btnAlt2.setText(AArray[1].textoAlternativa);
			btnAlt3.setText(AArray[2].textoAlternativa);
			btnAlt4.setText(AArray[3].textoAlternativa);
	}
	
	//Atualiza valores dos prêmios
	public void AtualizarCondicao(int nPergunta){
		ContextoDados assBD = new ContextoDados(this);
		SQLiteDatabase db = new ContextoDados(this).getReadableDatabase();
		Cursor cursor = assBD.RetornarDados(db, "Premios", new String[]{"ID, Acertar, Parar, Errar"}, "NPergunta=?", new String[]{Integer.toString(nPergunta)}, 1);
	    try {
	    	if (cursor.moveToFirst()) {
	    			CAtual.Acertar  = cursor.getString(1);
	    			CAtual.Parar 	= cursor.getString(2);
	    			CAtual.Errar 	= cursor.getString(3);
	    	}
	    } finally {
	    	cursor.close();
	    	db.close();
	    }
		btnAcertar.setText(CAtual.Acertar);
		btnParar.setText(CAtual.Parar);
		btnErrar.setText(CAtual.Errar);
	}
	
	public void ExibePlacar(String txtExibe, int sTempo, int tPlacar, int rodada){
		Intent intent = new Intent(this, Placar.class );
		intent.putExtra("txtExibe", txtExibe);
		intent.putExtra("sTempo", sTempo);
		intent.putExtra("tPlacar", tPlacar);
		intent.putExtra("rodada", this.rodada);
		startActivity(intent);
	}
	
	public PerguntaAtual TrazPergunta(int nDificult){
		PerguntaAtual pg = new PerguntaAtual();
		pg.ID = 0;
		ContextoDados assBD = new ContextoDados(this);
		SQLiteDatabase db = new ContextoDados(this).getReadableDatabase();
		Cursor cursor = assBD.RetornarDados(db, "Perguntas", new String[]{"ID, TextoPergunta"}, "Dificult=?", new String[]{Integer.toString(nDificult)}, 1);
	    try {
	    	if (cursor.moveToFirst()) {
	    			cursor.moveToPosition(numRandomico.nextInt(cursor.getCount())); //Cursor se move para uma posiÃ§Ã£o randÃ´mica entre 1 e o maximo
	    			pg.ID = cursor.getInt(0);
	    			pg.textoPergunta = cursor.getString(1);
	    			if (!verificaRepeticao(pg)) pg.ID = 0;
	    	}
	    } finally {
	    	cursor.close();
	    	db.close();
	    }
	    
	    return pg;
	}
	
	public Alternativa[] TrazAlternativas(PerguntaAtual pg){
		Alternativa[] arratv = new Alternativa[4];
		ContextoDados assBD = new ContextoDados(this);
		SQLiteDatabase db = new ContextoDados(this).getReadableDatabase();
		Cursor cursor = assBD.RetornarDados(db, "Alternativas", new String[]{"ID, TextoAlternativa, Correta"}, "IdPerg=?", new String[]{Integer.toString(pg.ID)}, 1);
		
		int i=0;
	    try {
	    	if (cursor.moveToFirst()) {
	    		do {
	    			Alternativa atv = new Alternativa();
	    			atv.ID = cursor.getInt(0);
	    			atv.textoAlternativa = cursor.getString(1);
	    			atv.correta = cursor.getInt(2);
	    			do { 
	    				i = Integer.valueOf(numRandomico.nextInt(4));
					} while (arratv[i] != null);
	    			arratv[i] = atv;
	    		} while (cursor.moveToNext());
	    	}
	    } finally {
	    	db.close();
	    }
		return arratv;
	}
	@Override //Esta função bloqueia o botão voltar
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
       return true;
    }
    if (keyCode == KeyEvent.KEYCODE_MENU) {
        return true;
     }
    return super.onKeyDown(keyCode, event);
    }
	
	public boolean verificaRepeticao(PerguntaAtual pg){
		int i=0;
		do {
			if (PArray[i]==pg.ID) return false;
			i++;
		} while (PArray[i]!=0);
		
		PArray[i] = pg.ID; 
		return true;
	}
	
	public class PerguntaAtual{
		int ID;
		String textoPergunta;
	}
	
	public class Alternativa{
		int ID;
		String textoAlternativa;
		int correta;
	}

	public class Condicao{
		String Acertar;
		String Parar;
		String Errar;
	}
	
	public void btnCartas(View view){
		btnCartas.setBackgroundResource(R.drawable.cartas);
		extras = "CARTAS";
		onCartas();
	}
	
	public void btnPular(View view){
		btnPulo.setBackgroundResource(R.drawable.pulo);
		extras = "PULAR";
		onPular();
	}
	
	public void btnParar(View view){
		btnParar.setBackgroundResource(R.drawable.barrgold);
		extras = "PARAR";
		onParar();
	}
	
	public void btnResposta1(View view){
		btnAlt1.setBackgroundResource(R.drawable.btnselected1);
		btnAlt1.setPadding(60, 0, 0, 0);
		
		nResposta = 0;
		DestacaAltCorreta();

	}
	
	public void btnResposta2(View view){
		btnAlt2.setBackgroundResource(R.drawable.btnselected2);
		btnAlt2.setPadding(60, 0, 0, 0);
		nResposta = 1;
		DestacaAltCorreta();

	}
	public void btnResposta3(View view){
		btnAlt3.setBackgroundResource(R.drawable.btnselected3);
		btnAlt3.setPadding(60, 0, 0, 0);
		nResposta = 2;
		DestacaAltCorreta();

	}
	public void btnResposta4(View view){
		btnAlt4.setBackgroundResource(R.drawable.btnselected4);
		btnAlt4.setPadding(60, 0, 0, 0);
		nResposta = 3;
		DestacaAltCorreta();

	}
	
	public void DestacaAltCorreta(){
		
		if (AArray[0].correta==1){
			btnAlt1.setBackgroundResource(R.layout.btnanimation1);
			AnimationDrawable frameAnimation = (AnimationDrawable) btnAlt1.getBackground();
			btnAlt1.setPadding(60, 0, 0, 0);
			frameAnimation.start();
		}else if (AArray[1].correta==1){
			btnAlt2.setBackgroundResource(R.layout.btnanimation2);
			AnimationDrawable frameAnimation = (AnimationDrawable) btnAlt2.getBackground();
			btnAlt2.setPadding(60, 0, 0, 0);
			frameAnimation.start();
		}else if (AArray[2].correta==1){
			btnAlt3.setBackgroundResource(R.layout.btnanimation3);
			AnimationDrawable frameAnimation = (AnimationDrawable) btnAlt3.getBackground();
			btnAlt3.setPadding(60, 0, 0, 0);
			frameAnimation.start();
		}else if (AArray[3].correta==1){
			btnAlt4.setBackgroundResource(R.layout.btnanimation4);
			AnimationDrawable frameAnimation = (AnimationDrawable) btnAlt4.getBackground();
			btnAlt4.setPadding(60, 0, 0, 0);
			frameAnimation.start();
		}
	
		Timer(2);

	}
	
	public void retiraIncorreta(int cartaEscolhida){
		int tirouQuantas = 0;
			for(int i = 0; i-1 < cartaEscolhida; i++){
				if (AArray[i].correta!=1){
					if(i==0){
						btnAlt1.setBackgroundResource(R.drawable.remover);
				//		btnAlt1.setOnClickListener(null);
						tirouQuantas++;
					}
					if(i==1){
						btnAlt2.setBackgroundResource(R.drawable.remover);
				//		btnAlt2.setOnClickListener(null);
						tirouQuantas++;
					}
					if(i==2){
						btnAlt3.setBackgroundResource(R.drawable.remover);
				//		btnAlt3.setOnClickListener(null);
						tirouQuantas++;
					}
					if(i==3){
						btnAlt4.setBackgroundResource(R.drawable.remover);
				//		btnAlt4.setOnClickListener(null);
						tirouQuantas++;
					}
				}
			}
			extras = "";
	}
	
		public void Timer(int segundos){
		 	int seg = segundos * 1000;
		 	handler.postDelayed(mRun, seg);
	    }
	 
		private Runnable mRun = new Runnable () {
	        @Override
	        public void run() {
	        	VerificaResposta();
	        }
		};
		

		
	public void VerificaResposta(){
		
		if("PARAR".equals(extras)){
			Toast.makeText(this, "Você parou!", Toast.LENGTH_SHORT).show();
			pontos = CAtual.Parar;
			ExibePlacar("Você ganhou " + CAtual.Parar + " Reais", 6,2, 6);
			onSalvar(pontos);
		}else {
			if ("PULAR".equals(extras)) {
				if(ajudaPulo != 0){
					ajudaPulo--;
					extras = "";
					Rodada();
				}
			} else {
			++nPergunta;
				if(AArray[nResposta].correta==1){	
					if (nPergunta==17) {  
						ExibePlacar("", 6,3, 4); 
						pontos = CAtual.Acertar;
						onSalvar(pontos);
					}
					Rodada();
				}else{
					Toast.makeText(this, "Resposta errada!", Toast.LENGTH_SHORT).show();
					recolorirbotoes();
					pontos = CAtual.Errar;
					onSalvar(pontos);
					rodada = 50;
					ExibePlacar("Você ganhou " + CAtual.Errar + " Reais", 6,2, 50); 
				}
			}
		}
	}
	
		public void recolorirbotoes(){
			btnAlt1.setBackgroundResource(R.layout.btnrespost1);
			btnAlt2.setBackgroundResource(R.layout.btnrespost2);
			btnAlt3.setBackgroundResource(R.layout.btnrespost3);
			btnAlt4.setBackgroundResource(R.layout.btnrespost4);
			btnAlt1.setPadding(60, 0, 0, 0);
			btnAlt2.setPadding(60, 0, 0, 0);
			btnAlt3.setPadding(60, 0, 0, 0);
			btnAlt4.setPadding(60, 0, 0, 0);	
		}
		
	public void SalvaScore(String nome){
		//SwarmLeaderboard.submitScore(15266, pontos);
		DatabaseHandler db = new DatabaseHandler(this);
        db.addNome(new LeaderBoard(nome, Integer.parseInt(pontos)));
        
        Log.d("Reading: ", "Lendo nomes.."); 
        List<LeaderBoard> nomes = db.getAllNomes();       
        
        for (LeaderBoard lb : nomes) {
            String log = "Id: "+lb.getId()+" ,Nome: " + lb.getNome() + " ,Score: " + lb.getScore();
                // Writing Contacts to log
        Log.d("Nome: ", log);
        }
        finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
