package model;

//done 20/03/2017

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "entrega")
public class Entrega implements Serializable {
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    private String id;
    @Column(name = "status")
    private int status;//SERÁ UM ENUM {0=PREVISTO, 1=EXECUTANDO, 2=RECEBIDO}libera a notificacao para o Gateway
    @Column(name = "acaoProxy")
    private int acaoProxy;//ENUM {0=NONE,1=SIGN, 2=VERIFY}->libera uma notificacao para o proxy
    @Column(name = "regstrationId")
    private String regstrationId;

    //----DADOS DA ENTREGA-----
    @Column(name = "volumePrevisto")
    private double volumePrevisto;
    @Column(name = "pipeiro")
    private String pipeiro;//NO REAL SERÁ TIPO Pipeiro
    @Column(name = "manancial")
    private String manancial;//TAMBÉM SERÁ UM TIPO: Manancial
    @Column(name = "cisterna")
    private String cisterna;//Na real será Tipo Cisterna;
    @Column(name = "volumeEntregue")
    private double volumeEntregue;
    @Column(name = "pureza")
    private double pureza;
    @Column(name = "carradaValida")
    private int carradaValida;//0=NÃO, 1=SIM
    @Column(name = "pacoteEntregaValido")
    private int pacoteEntregaValido;//0=NÃO, 1=SIM
    @Column(name = "pacoteRecebimentoValido")
    private int pacoteRecebimentoValido;//0=NÃO, 1=SIM

    @Column(name = "dataPrevista")
    private String dataPrevista;
    @Column(name = "dataRealizada")
    private String dataRealizada;

    @Column(name = "localEntrega")
    private String localEntrega;


    @Column(name = "areaBase")
    private String assinaturaProxy;
    @Column(name = "assinaturaNo")
    private String assinaturaNo;


    public Entrega(String id, int status, int acaoProxy, double volumePrevisto, String pipeiro, String manancial,
                   String cisterna, String dataPrevista) {
        super();
        this.id = id;
        this.status = status;
        this.acaoProxy = acaoProxy;
        this.volumePrevisto = volumePrevisto;
        this.pipeiro = pipeiro;
        this.manancial = manancial;
        this.cisterna = cisterna;
        this.dataPrevista = dataPrevista;

    }



    public Entrega(String id, int status, int acaoProxy, double volumePrevisto, String pipeiro, String manancial,
                   String cisterna, double volumeEntregue, double pureza, int carradaValida, int pacoteEntregaValido,
                   int pacoteRecebimentoValido, String dataPrevista, String dataRealizada, String assinaturaProxy,
                   String assinaturaNo, String regstrationId, String localEntrega) {
        super();
        this.id = id;
        this.status = status;
        this.acaoProxy = acaoProxy;
        this.volumePrevisto = volumePrevisto;
        this.pipeiro = pipeiro;
        this.manancial = manancial;
        this.cisterna = cisterna;
        this.volumeEntregue = volumeEntregue;
        this.pureza = pureza;
        this.carradaValida = carradaValida;
        this.pacoteEntregaValido = pacoteEntregaValido;
        this.pacoteRecebimentoValido = pacoteRecebimentoValido;
        this.dataPrevista = dataPrevista;
        this.dataRealizada = dataRealizada;
        this.assinaturaProxy = assinaturaProxy;
        this.assinaturaNo = assinaturaNo;
        this.regstrationId=regstrationId;
        this.localEntrega=localEntrega;
    }



    public Entrega(String id) {
        super();
        this.id = id;
    }
    public Entrega() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAcaoProxy() {
        return acaoProxy;
    }

    public void setAcaoProxy(int acaoProxy) {
        this.acaoProxy = acaoProxy;
    }

    public double getVolumePrevisto() {
        return volumePrevisto;
    }

    public void setVolumePrevisto(double volumePrevisto) {
        this.volumePrevisto = volumePrevisto;
    }

    public String getPipeiro() {
        return pipeiro;
    }

    public void setPipeiro(String pipeiro) {
        this.pipeiro = pipeiro;
    }

    public String getManancial() {
        return manancial;
    }

    public void setManancial(String manancial) {
        this.manancial = manancial;
    }

    public String getCisterna() {
        return cisterna;
    }

    public void setCisterna(String cisterna) {
        this.cisterna = cisterna;
    }

    public double getVolumeEntregue() {
        return volumeEntregue;
    }

    public void setVolumeEntregue(double volumeEntregue) {
        this.volumeEntregue = volumeEntregue;
    }

    public double getPureza() {
        return pureza;
    }

    public void setPureza(double pureza) {
        this.pureza = pureza;
    }

    public int getCarradaValida() {
        return carradaValida;
    }

    public void setCarradaValida(int carradaValida) {
        this.carradaValida = carradaValida;
    }

    public int getPacoteEntregaValido() {
        return pacoteEntregaValido;
    }



    public int getPacoteRecebimentoValido() {
        return pacoteRecebimentoValido;
    }

   /* public void setPacoteRecebimento(int pacoteRecebimentoValido) {
        this.pacoteRecebimentoValido = pacoteRecebimentoValido;
    }*/

    public String getDataRealizada() {
        return dataRealizada;
    }

    public void setDataRealizada(String dataRealizada) {
        this.dataRealizada = dataRealizada;
    }

    public String getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(String dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public String getAssinaturaProxy() {
        return assinaturaProxy;
    }

    public void setAssinaturaProxy(String assinaturaProxy) {
        this.assinaturaProxy = assinaturaProxy;
    }

    public String getAssinaturaNo() {
        return assinaturaNo;
    }

    public void setAssinaturaNo(String assinaturaNo) {
        this.assinaturaNo = assinaturaNo;
    }
    public void setPacoteEntregaValido(int pacoteEntregaValido) {
        this.pacoteEntregaValido = pacoteEntregaValido;
    }

    public void setPacoteRecebimentoValido(int pacoteRecebimentoValido) {
        this.pacoteRecebimentoValido = pacoteRecebimentoValido;
    }

    public String getLocalEntrega() {
        return localEntrega;
    }

    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }


    /*@Override
    public String toString() {
        String entrega="Entrega:"+this.id+" [Cisterna: "+this.cisterna+"]";
        return entrega;

    }*/

    @Override
    public String toString() {
        return "Entrega{" +
                "id='" + id + '\'' +
                ", volumePrevisto=" + volumePrevisto +
                ", pipeiro='" + pipeiro + '\'' +
                ", manancial='" + manancial + '\'' +
                ", cisterna='" + cisterna + '\'' +
                ", dataPrevista='" + dataPrevista + '\'' +
                '}';
    }


    public String todoString() {
        return "Entrega{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", acaoProxy=" + acaoProxy +
                ", regstrationId='" + regstrationId + '\'' +
                ", volumePrevisto=" + volumePrevisto +
                ", pipeiro='" + pipeiro + '\'' +
                ", manancial='" + manancial + '\'' +
                ", cisterna='" + cisterna + '\'' +
                ", volumeEntregue=" + volumeEntregue +
                ", pureza=" + pureza +
                ", carradaValida=" + carradaValida +
                ", pacoteEntregaValido=" + pacoteEntregaValido +
                ", pacoteRecebimentoValido=" + pacoteRecebimentoValido +
                ", dataPrevista='" + dataPrevista + '\'' +
                ", dataRealizada='" + dataRealizada + '\'' +
                ", localEntrega='" + localEntrega + '\'' +
                ", assinaturaProxy='" + assinaturaProxy + '\'' +
                ", assinaturaNo='" + assinaturaNo + '\'' +
                '}';
    }

    public String getRegstrationId() {
        return regstrationId;
    }

    public void setRegstrationId(String regstrationId) {
        this.regstrationId = regstrationId;
    }

    /*public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
