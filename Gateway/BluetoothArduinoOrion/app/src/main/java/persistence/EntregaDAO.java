package persistence;

import android.content.Context;


import model.Entrega;

/**
 * Created by wasuf on 28/03/2017.
 */

public class EntregaDAO extends GenericDao<Entrega> {
    public EntregaDAO(Context context) {
        super(context, Entrega.class);
    }
}
