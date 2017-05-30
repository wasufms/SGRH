package model;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
/*import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import services.CisternaService;*/

//import com.google.android.gms.maps.model.LatLng;

@Entity(name = "cisterna")
public class Cisterna implements Serializable {
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    private String id;
    @Column(name = "altura")
    private double altura;//altura real da cisterna
    @Column(name = "raio")
    private double raio;//NÃO USADA
    @Column(name = "capacidade")
    private double capacidade;//calculado;
    @Column(name = "volume")
    private double volume;//volume do sensor areaDaBase*Altura
    @Column(name = "distancia")
    private double distancia;//distancia entre o Sensor e o espelho d�gua CHEIO
    @Column(name = "position")
    private String position;//latitude longitude da cisterna
    @Column(name = "areaBase")
    private double areaBase;
    @Column(name = "outro")
    private String outro; //proposito geral


    public Cisterna(){

    }
    public Cisterna(String id, double distancia, double altura,double areaBase){
        this.id=id;
        this.distancia=distancia;
        this.altura=altura;
        this.areaBase=areaBase;
        this.capacidade=areaBase*altura/1000;
    }

    public Cisterna(String id, double altura, double raio, double capacidade, double volume, double distancia,
                    String position, double areaBase, String outro) {
        super();
        this.id = id;
        this.altura = altura;
        this.raio = raio;
        this.capacidade = capacidade;
        this.volume = volume;
        this.distancia = distancia;
        this.position = position;
        this.areaBase = areaBase;
        this.outro = outro;
    }

    public double getAreaBase() {
        return areaBase;
    }

    public void setAreaBase(double areaBase) {
        this.areaBase = areaBase;
    }

    //private LatLng localizacao;
    //String localizacao;



    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getAltura() {
        return altura;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public double getCapacidade() {
        return capacidade;
    }
    public void setCapacidade(double capacidade) {
        this.capacidade = capacidade;
    }
    public double getVolume() {
        return volume;
    }
    public void setVolume(double volume) {
        this.volume = volume;
    }


    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double calculaVolume(double distanciaArduino){

        double alturaAgua;
        double volume=0;
        alturaAgua=(this.altura+this.distancia)-distanciaArduino;
        volume=Math.PI*raio*raio*alturaAgua;
        //this.volume=volume;
        return volume;
        //return distancia;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Cisterna [id=" + id + ", altura=" + altura + "cm, Área da Base=" + areaBase + "cm2, capacidade=" + capacidade+
                " litros, distancia=" + distancia + "cm, volume=" + volume + " litros, position=" + position + " outro=" + outro + "]";
    }
    public String getOutro() {
        return outro;
    }
    public void setOutro(String outro) {
        this.outro = outro;
    }

}
