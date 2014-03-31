package com.br.showdomilhao;

public class LeaderBoard {
	int id;
	String nome;
	int score;
	
	public LeaderBoard(){
	}
	
	public LeaderBoard(int Id, String nome, int score){
		this.id = id;
		this.nome = nome;
		this.score = score;
	}
	
	public LeaderBoard(String nome, int score){
		this.nome = nome;
		this.score = score;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
