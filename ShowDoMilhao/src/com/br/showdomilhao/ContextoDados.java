package com.br.showdomilhao;

import java.io.*;

import com.example.showdomilhao.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class ContextoDados extends SQLiteOpenHelper {
	
	private static final String NOME_BD = "DadosShowMilhao";
	private static final int VERSAO_BD = 17;
	private static final String LOG_TAG = "DadosShowMilhao";
	private final Context contexto;

	public ContextoDados(Context context) {
		super(context, NOME_BD, null, VERSAO_BD);
		this.contexto = context;
		}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String[] sql = contexto.getString(R.string.ContextoDados_onCreate).split("\n");
		String[] sql2 = contexto.getString(R.string.ContextoDados_data).split("\n");
		db.beginTransaction();
		try
		{
			ExecutarComandosSQLVetor(db, sql);
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		catch (SQLException e)
		{
			Log.e("Erro ao criar as tabelas.", e.toString());
		}
		
		
		try
		{
			ExecutarComandosSQLVetor(db, sql2);
			db.setTransactionSuccessful();
		}
		catch (SQLException e)
		{
			Log.e("Erro ao popular as tabelas", e.toString());
		}
		finally
		{
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(LOG_TAG, "Atualizando a base de dados da versão " + oldVersion + " para " + newVersion + ", que destruirão todos os dados antigos");
		String[] sql = contexto.getString(R.string.ContextoDados_onUpgrade).split("\n");
		db.beginTransaction();

		try
		{
			ExecutarComandosSQLVetor(db, sql);
			db.setTransactionSuccessful();
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally
		{
			db.endTransaction();
		}
		onCreate(db);
	}
	
	public void ExecutarComandosSQLVetor(SQLiteDatabase db, String[] sql)
	{
		for( String s : sql )
			if (s.trim().length()>0)
				db.execSQL(s);
	}
	
	public Cursor RetornarDadosComandosSQL(String sql){
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[]{});
		return cursor;
	}
	
	public void DroparBase()
	{
		SQLiteDatabase db = getWritableDatabase();
		String[] sql = contexto.getString(R.string.ContextoDados_onUpgrade).split("\n");
		db.beginTransaction();
		try
		{
			ExecutarComandosSQLVetor(db, sql);
			db.setTransactionSuccessful();
		}
		catch (SQLException e)
		{
			Log.e("Erro ao atualizar as tabelas e testar os dados", e.toString());
			throw e;
		}
		finally
		{
			db.endTransaction();
		}
	}
	
	
	public Cursor RetornarDados(SQLiteDatabase db, String Tabelas, String[] Colunas, String Argumentos, String[] Clausulas, int OrdemPosition){
		Cursor cursor = db.query(Tabelas, Colunas, Argumentos, Clausulas, null, null, Integer.toString(OrdemPosition) );
		return cursor;
	}
	
	public Cursor RetornarDados2(String Tabelas, String[] Colunas, String Argumentos, String[] Clausulas, int OrdemPosition){
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Tabelas, Colunas, Argumentos, Clausulas, null, null, Integer.toString(OrdemPosition) );
		
		return cursor;

	}
	
	public void Insert(SQLiteDatabase db, String Table, ContentValues Values){
		
		try
		{
			db.insert(Table, null, Values);
		}
		finally
		{
			db.close();
		}

	}
	
	public void Delete(SQLiteDatabase db, String Tabelas, String Argumentos, String[] Clausulas){
		db.delete(Tabelas, Argumentos, Clausulas);
	}
	public void Update(SQLiteDatabase db, String Tabelas, ContentValues Values, String Argumentos, String[] Clausulas ){
		db.update(Tabelas, Values, Argumentos, Clausulas);
	}
	
    public void exportDataBase() throws IOException{
    	String inFileName = this.getReadableDatabase().getPath();
    	InputStream myInput = new BufferedInputStream(new FileInputStream(inFileName));
    	String outFileName = "/storage/extSdCard/Download/" + NOME_BD;
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
}
