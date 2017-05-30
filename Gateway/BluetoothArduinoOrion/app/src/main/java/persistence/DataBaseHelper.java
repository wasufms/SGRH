package persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import model.Cisterna;
import model.Entrega;
import model.Manancial;

/**
 * Created by wasuf on 28/03/2017.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, "CInsternas1.db", null, 1);//alteracao tabela entrega.entregaLocal
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource src) {
        try{
            TableUtils.createTable(src, Manancial.class);
            TableUtils.createTable(src, Cisterna.class);
            TableUtils.createTable(src, Entrega.class);
            TableUtils.createTable(src, CurrentEntrega.class);
        }catch(Exception e){
            e.printStackTrace();
            Log.d("CRIAR_BD","Erro ao criar o banco");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource src, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(src, Manancial.class, true);
            TableUtils.dropTable(src, Cisterna.class, true);
            TableUtils.dropTable(src, Entrega.class, true);
            TableUtils.createTable(src, CurrentEntrega.class);
            onCreate(db, src);
        }catch(Exception e){
            e.printStackTrace();
            Log.d("CRIAR_BD","Erro ao no upgrade");
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
