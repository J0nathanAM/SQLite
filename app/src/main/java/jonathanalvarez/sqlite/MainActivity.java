package jonathanalvarez.sqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    SQLiteDatabase db;
    Cursor cursor;
    ListAdapter adaptador;
    EditText productoabuscar;
    Button buscar;

    private static final int INSERTAR_ID= Menu.FIRST;
    public static final int BORRAR_ID=Menu.FIRST+1;
    public static final int ACTUALIZAR_ID=Menu.FIRST+3;
    public static final int SALIR=Menu.FIRST+4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this).getWritableDatabase();

        consulta("SELECT _id, nombre, precio FROM productos ORDER BY _id");

        registerForContextMenu(lista);
        productoabuscar=(EditText)findViewById(R.id.productobuscar);
        buscar=(Button)findViewById(R.id.btnbuscar);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consulta("SELECT _id, nombre, precio FROM productos WHERE nombre LIKE '"
                        +productoabuscar.getText().toString().trim()
                        +"%' ORDER BY _id");
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cactualizar=db.rawQuery("SELECT nombre, precio FROM productos WHERE _id=?",new String[]{String.valueOf(i)});
                cactualizar.moveToFirst();
                final String id=cursor.getString(cursor.getColumnIndex("_id"));

                Intent intent = new Intent(MainActivity.this,InfoProducto.class);
                intent.putExtra("idproducto",id);
                startActivity(intent);
            }
        });
    }

    public void consulta(String sql){
        cursor=db.rawQuery(sql,null);

        adaptador=new SimpleCursorAdapter(this,R.layout.row,cursor,
                new String[]{"_id","nombre","precio"},new int[]{R.id.idproducto,R.id.nombre,R.id.precio},0);

        lista =(ListView)findViewById(R.id.lista);
        lista.setAdapter(adaptador);
    }

    @Override
    protected void onResume() {
        consulta("SELECT _id, nombre, precio FROM productos ORDER BY _id");
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,ACTUALIZAR_ID,Menu.NONE,"Actualizar Registro");
        menu.add(Menu.NONE,BORRAR_ID,Menu.NONE,"Borrar Registro");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case INSERTAR_ID:
                Intent intent=new Intent(MainActivity.this,AgregarNuevo.class);
                startActivity(intent);
                break;
            case  SALIR:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case ACTUALIZAR_ID:
                AdapterView.AdapterContextMenuInfo registroActualizar=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                actualizar(registroActualizar.id);
                break;
            case BORRAR_ID:
                AdapterView.AdapterContextMenuInfo registroBorrar=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                borrar(registroBorrar.id);
                break;
        }
        return true;
    }

    private void actualizar(final long rowid){
        Cursor cactualizar=db.rawQuery("SELECT nombre, precio FROM productos WHERE _id=?",new String[]{String.valueOf(rowid)});
        cactualizar.moveToFirst();
        final String nombrep=cursor.getString(cursor.getColumnIndex("nombre"));
        String preciop=cursor.getString(cursor.getColumnIndex("precio"));

        LayoutInflater inflater =LayoutInflater.from(this);
        View row = inflater.inflate(R.layout.editar,null);
        final EditText prod=(EditText)row.findViewById(R.id.editnombre);
        final EditText prec=(EditText)row.findViewById(R.id.editprecio);

        prod.setText(nombrep);
        prec.setText(preciop);

        if(rowid>0){
            new AlertDialog.Builder(this)
                    .setTitle("Actualizar Registro")
                    .setView(row)
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String[] args={String.valueOf(rowid)};
                            ContentValues cv=new ContentValues();
                            cv.put("nombre",prod.getText().toString());
                            cv.put("precio",prec.getText().toString());

                            db.update("productos",cv,"_id=?",args);

                            cursor=db.rawQuery("SELECT * FROM productos ORDER BY _id",null);
                            ((SimpleCursorAdapter)adaptador).changeCursor(cursor);
                        }
                    })
                    .show();
        }
    }

    private void borrar (final long rowid){
        if(rowid>0){
            new AlertDialog.Builder(this)
                    .setTitle("¿Estas seguro de borrar?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String[] args={String.valueOf(rowid)};
                            db.delete("productos","_id=?",args);

                            cursor=db.rawQuery("SELECT * FROM productos ORDER BY _id",null);
                            ((SimpleCursorAdapter)adaptador).changeCursor(cursor);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,INSERTAR_ID,Menu.NONE,"Nuevo Registro");
        menu.add(Menu.NONE,SALIR,Menu.NONE,"Salir");
        return super.onCreateOptionsMenu(menu);
    }
}
