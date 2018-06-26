package jonathanalvarez.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

/**
 * Created by IDS Comercial on 28/09/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="productosBD";
    private static final int DATABASE_VERSION=2;
    private static final String NOMBRE="nombre";
    private static final String PRECIO="precio";
    private static final String IMAGEN="imagen";

    public DBHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE productos(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre VARCHAR(20) NOT NULL," +
                "precio VARCHAR(5) NOT NULL," +
                "imagen INTEGER" +
                ");";
        db.execSQL(sql);

        ContentValues cv=new ContentValues();

        cv.put(NOMBRE,"Leche");
        cv.put(PRECIO,"15");
        cv.put(IMAGEN,R.drawable.leche);
        db.insert("productos",NOMBRE,cv);

        cv.put(NOMBRE,"Pan");
        cv.put(PRECIO,"3");
        cv.put(IMAGEN,R.drawable.pan);
        db.insert("productos",NOMBRE,cv);

        cv.put(NOMBRE,"Refresco");
        cv.put(PRECIO,"16");
        cv.put(IMAGEN,R.drawable.refresco);
        db.insert("productos",NOMBRE,cv);

        cv.put(NOMBRE,"Galletas");
        cv.put(PRECIO,"8");
        cv.put(IMAGEN,R.drawable.galleta);
        db.insert("productos",NOMBRE,cv);

        cv.put(NOMBRE,"Sopa");
        cv.put(PRECIO,"5");
        cv.put(IMAGEN,R.drawable.sopa);
        db.insert("productos",NOMBRE,cv);

        cv.put(NOMBRE,"Café");
        cv.put(PRECIO,"7");
        cv.put(IMAGEN,R.drawable.cafe);
        db.insert("productos",NOMBRE,cv);

        cv.put(NOMBRE,"Atún");
        cv.put(PRECIO,"18");
        cv.put(IMAGEN,R.drawable.atun);
        db.insert("productos",NOMBRE,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }
}
