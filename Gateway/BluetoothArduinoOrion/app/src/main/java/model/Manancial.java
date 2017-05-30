package model;

/**
 * Created by wasuf on 25/03/2017.
 */

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "manancial")
public class Manancial implements Serializable{
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    private String id;
    @Column(name = "position")
    private String position;
    @Column(name = "nome")
    private String nome;
    @Column(name = "outro")
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
