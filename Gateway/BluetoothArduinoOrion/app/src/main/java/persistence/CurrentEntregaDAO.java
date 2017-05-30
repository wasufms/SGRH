package persistence;

import android.content.Context;

import model.Cisterna;
import model.Entrega;
import model.Manancial;

/**
 * Created by wasuf on 28/03/2017.
 */

public class CurrentEntregaDAO extends GenericDao<CurrentEntrega> {
    public CurrentEntregaDAO(Context context) {
        super(context, CurrentEntrega.class);
    }
    public Entrega getCurrentEntrega(Context context){
        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(context);
        CurrentEntrega currentEntrega=currentEntregaDAO.getById(1);

        EntregaDAO entregaDAO=new EntregaDAO(context);
        Entrega entrega=entregaDAO.getById(currentEntrega.getEntregaId());

        return entrega;
    }
    public Cisterna getCurrentCisterna(Context context){
        Entrega entrega=getCurrentEntrega(context);
        CisternaDAO cisternaDAO=new CisternaDAO(context);
        Cisterna cisterna=cisternaDAO.getById(entrega.getCisterna());
        return cisterna;
    }
    public Manancial getCurrentManancial(Context context){
        Entrega entrega=getCurrentEntrega(context);
        ManancialDAO manancialDAO=new ManancialDAO(context);
        Manancial manancial=manancialDAO.getById(entrega.getManancial());
        return manancial;
    }
}
