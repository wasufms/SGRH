package persistence;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wasuf on 28/03/2017.
 */

public abstract class GenericDao<E> extends DataBaseHelper{
protected Dao<E, Integer> dao;
private Class<E> type;

public GenericDao(Context context, Class<E> type) {
        super(context);
        this.type = type;
        setDao();
        }

protected void setDao() {
        try{
        dao = DaoManager.createDao(getConnectionSource(), type);
        }catch(Exception e){
        e.printStackTrace();
        }
        }

public List<E> getAll() {
        try{
        List<E> result = dao.queryForAll();
        return result;
        }catch(Exception e){
        e.printStackTrace();
        return null;
        }
        }

public E getById(int id) {

        try{
        E obj = dao.queryForId(id);
        return obj;
        }catch(Exception e){
        e.printStackTrace();
        return null;
        }
        }

        public E getById(String id) {
                Integer inda=Integer.getInteger(id);
                E obj=null;


                try{
                        List<E>list;
                        list=dao.queryBuilder()
                                .where()
                                .eq("id", id)
                                .query();
                        if(list.size()>0){
                                obj=list.get(0);
                        }
                        return obj;
                }catch(Exception e){
                        e.printStackTrace();
                        return null;
                }
        }


public void insert(E obj) {
        try{
        dao.create(obj);
        }catch(Exception e){
        e.printStackTrace();
        }
        }

public void delete(E obj) {
        try{
        dao.delete(obj);
        }catch(Exception e){
        e.printStackTrace();
        }
        }

public void update(E obj) {
        try{
        dao.update(obj);
        }catch(Exception e){
        e.printStackTrace();
        }
        }
}
