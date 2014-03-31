package com.br.showdomilhao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
//Classe responsável pela criação do Banco de Dados e tabelas
public class DatabaseHandler extends SQLiteOpenHelper {
	//Nome da tabela
    private static final String TABLE_LEADERBOARD = "leaderBoard";
    //Campos da tabela
    private static final String KEY_ID = "_id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_SCORE = "score";
    //Nome e versão do banco de dados
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "leaderBoard";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    //Cria tabelas
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LEADERBOARD_TABLE = "CREATE TABLE " + TABLE_LEADERBOARD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOME + " TEXT,"
                + KEY_SCORE + " TEXT" + ")";
        db.execSQL(CREATE_LEADERBOARD_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEADERBOARD);
        onCreate(db);
    }
    
    public void addNome(LeaderBoard leaderBoard) {
    	SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOME, leaderBoard.getNome()); // Contact Name
        values.put(KEY_SCORE, leaderBoard.getScore()); // Contact Phone Number
        db.insert(TABLE_LEADERBOARD, null, values);
        db.close(); // Closing database connection
    }

    public List<LeaderBoard> getAllNomes() {
    	List<LeaderBoard> nomeList = new ArrayList<LeaderBoard>();

    	String selectQuery = "SELECT  * FROM " + TABLE_LEADERBOARD;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        if (cursor.moveToFirst()) {
            do {
            	LeaderBoard nome = new LeaderBoard();
                nome.setId(Integer.parseInt(cursor.getString(0)));
                nome.setNome(cursor.getString(1));
                nome.setScore(cursor.getInt(2));
                // Adding contact to list
                nomeList.add(nome);
            } while (cursor.moveToNext());
        }
     
        return nomeList;
    }

	public Cursor RetornarDados(SQLiteDatabase db, String Tabelas, String[] Colunas, int OrdemPosition){
		Cursor cursor = db.query(Tabelas, Colunas, null, null, null, null, Integer.toString(OrdemPosition) );

		return cursor;

	}
}
