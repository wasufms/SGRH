package persistence;

import android.content.Context;

import model.Manancial;

/**
 * Created by wasuf on 28/03/2017.
 */

public class ManancialDAO extends GenericDao<Manancial> {
    public ManancialDAO(Context context) {
        super(context, Manancial.class);
    }
}
