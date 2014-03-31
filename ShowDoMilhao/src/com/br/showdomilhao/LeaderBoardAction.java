package com.br.showdomilhao;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.showdomilhao.R;

public class LeaderBoardAction extends Activity {

	 private SQLiteDatabase database;
	 private CursorAdapter dataSource;
	 
	 private static final String campos[] = {"nome", "score", "_id"};
	 
	 ListView listView;
	 DatabaseHandler helper;
	    
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
	
		listView = (ListView) findViewById(R.id.listView1);
		
		helper = new DatabaseHandler(this);
		database = helper.getWritableDatabase();
		
		Cursor nomes = database.query("leaderBoard", campos, null, null, null, null, "CAST(score AS integer)" + " DESC LIMIT 5");
		
		if (nomes.getCount() > 0){
            //cria cursor que será exibido na tela, nele serão exibidos
            //todos os contatos cadastrados
            dataSource = new SimpleCursorAdapter(this, R.layout.row, nomes,
                    campos, new int[] { R.id.score_name , R.id.score_points});
            //relaciona o dataSource ao próprio listview
            listView.setAdapter(dataSource);
        }else{
            Toast.makeText(this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
        }
		
	}
	
	
	
}
