package persistence;

import android.content.Context;

import java.util.List;

import model.Cisterna;


/**
 * Created by wasuf on 28/03/2017.
 */

public class CisternaDAO extends GenericDao<Cisterna> {
    public CisternaDAO(Context context) {
        super(context, Cisterna.class);
    }


}
