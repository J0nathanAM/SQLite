package jonathanalvarez.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by IDS Comercial on 28/09/2017.
 */

public class AgregarNuevo extends AppCompatActivity {

    EditText nombre,precio;
    Button agregar;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.agregar_activity);
        super.onCreate(savedInstanceState);

        nombre=(EditText)findViewById(R.id.nombreproducto);
        precio=(EditText)findViewById(R.id.precioproducto);
        agregar=(Button)findViewById(R.id.btnagregar);

        db = new DBHelper(this).getWritableDatabase();

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv=new ContentValues();

                cv.put("nombre",nombre.getText().toString());
                cv.put("precio",precio.getText().toString());
                db.insert("productos","nombre",cv);

                finish();
            }
        });
    }
}
