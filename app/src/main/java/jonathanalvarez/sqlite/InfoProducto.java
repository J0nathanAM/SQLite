package jonathanalvarez.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by IDS Comercial on 28/09/2017.
 */

public class InfoProducto extends AppCompatActivity {

    ImageView imagenp;
    TextView name, price;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.infoproducto);
        super.onCreate(savedInstanceState);

        imagenp=(ImageView)findViewById(R.id.imagen);
        name=(TextView)findViewById(R.id.nombreprod);
        price=(TextView)findViewById(R.id.prec);

        String idprod=getIntent().getStringExtra("idproducto");
        db = new DBHelper(this).getWritableDatabase();
        //Toast.makeText(this, idprod, Toast.LENGTH_SHORT).show();

        Cursor consulta=db.rawQuery("SELECT nombre,precio,imagen FROM productos WHERE _id=?",new String[]{idprod});
        consulta.moveToFirst();

        final String nombrep=consulta.getString(consulta.getColumnIndex("nombre"));
        final String precio=consulta.getString(consulta.getColumnIndex("precio"));
        final int imag=consulta.getInt(consulta.getColumnIndex("imagen"));

        name.setText(nombrep);
        price.setText("$ "+precio);
        imagenp.setImageResource(imag);

    }
}
