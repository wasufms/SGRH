package model;

import java.io.Serializable;

public class Manancial implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String position;
	private String nome;
	private String outro;
	
	public Manancial(){
		
	}
	
	public Manancial(String id, String position, String nome, String outro) {
		super();
		this.id = id;
		this.position = position;
		this.nome = nome;
		this.outro = outro;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getOutro() {
		return outro;
	}
	public void setOutro(String outro) {
		this.outro = outro;
	}

	@Override
	public String toString() {
		return "Manancial [id=" + id + ", position=" + position + ", nome=" + nome + ", outro=" + outro + "]";
	}
	
}
