package persistence;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by wasuf on 28/03/2017.
 */
//MANTEM A ENTRADA DA ENTREGA CORRENTE
@Entity(name = "CurrentEntrega")
public class CurrentEntrega implements Serializable{
    @DatabaseField(id = true)
    int id;
    @Column(name = "entregaId")
    String entregaId;

    public CurrentEntrega(){

    }

    public CurrentEntrega(int id, String entregaId) {
        this.id = id;
        this.entregaId = entregaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntregaId() {
        return entregaId;
    }

    public void setEntregaId(String entregaId) {
        this.entregaId = entregaId;
    }

    @Override
    public String toString() {
        return "CurrentEntrega{" +
                "id=" + id +
                ", entregaId='" + entregaId + '\'' +
                '}';
    }
}
