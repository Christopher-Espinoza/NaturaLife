package com.caea.naturalife;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caea.naturalife.database.MNaturalReference;
import com.caea.naturalife.entity.Mnaturales;
import com.caea.naturalife.repository.MNaturalRepository;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MNaturalActivity extends AppCompatActivity implements MNaturalRepository{

    private final String raiz = "NaturaLife/";
    private final String ruta = raiz + "medicamentos";
    private final int cod_selecciona = 10;
    private final int cod_foto = 20;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String path = "";

    TextInputEditText nombre, descripcion, formula, recomendacion, sabor, precio;
    Spinner grasa, azucar, sal;
    ImageView imagen, imedicamento;
    Mnaturales mnaturales;
    FloatingActionMenu fabmenu;
    com.github.clans.fab.FloatingActionButton fabsave, fabdelete, fabcamera;

    private FirebaseDatabase ChrisMNaturaBD;
    private DatabaseReference ChrisMNaturaBDReference;
    private StorageReference ChrisMNaturaStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnatural);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ChrisMNaturaStorage = FirebaseStorage.getInstance().getReference();

        imedicamento = findViewById(R.id.imedicamento);
        imagen = findViewById(R.id.imagen);
        nombre = findViewById(R.id.tietnombre);
        descripcion = findViewById(R.id.tietdescripcion);
        formula = findViewById(R.id.tietformula);
        recomendacion = findViewById(R.id.tietrecomendacion);
        sabor = findViewById(R.id.tietsabor);
        precio = findViewById(R.id.tietprecio);
        grasa = findViewById(R.id.spgrasa);
        azucar = findViewById(R.id.spazucar);
        sal = findViewById(R.id.spsal);

        fabmenu = findViewById(R.id.fabmenu);
        fabmenu.setClosedOnTouchOutside(true);
        fabsave = findViewById(R.id.fabsave);
        fabdelete = findViewById(R.id.fabdelete);
        fabcamera = findViewById(R.id.fabcamera);
        fabcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarimagen();
            }
        });

        if(validapermiso()){
            fabmenu.setEnabled(true);
        }else{
            fabmenu.setEnabled(false);
        }
    }

    private boolean validapermiso() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return true;
        }

        if((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }
        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MNaturalActivity.this);
        dialog.setTitle("Permisos Desactivados");
        dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                fabmenu.setEnabled(true);
            }else{
                solicitarpermisosmanual();
            }
        }
    }

    private void solicitarpermisosmanual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder aleropcionnes = new AlertDialog.Builder(MNaturalActivity.this);
        aleropcionnes.setTitle("¿Desea configurar lso permisos de forma manual?");
        aleropcionnes.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        aleropcionnes.show();
    }

    private void cargarimagen() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder aleropcionnes = new AlertDialog.Builder(MNaturalActivity.this);
        aleropcionnes.setTitle("Seleccionar una Opcion");
        aleropcionnes.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Tomar Foto")) {
                    tomarfoto();
                } else {
                    if (opciones[which].equals("Cargar Imagen")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Selecciona la aplicacion"), cod_selecciona);
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        aleropcionnes.show();
    }

    private void tomarfoto() {
        File file = new File(Environment.getExternalStorageDirectory(), ruta);
        boolean iscreada = file.exists();
        String nombre = "";
        if (iscreada == false) {
            iscreada = file.mkdirs();
        }

        if (iscreada == true) {
            nombre = (System.currentTimeMillis() / 1000) + ".jpg";
        }
        path = Environment.getExternalStorageDirectory() + File.separator + ruta + File.separator + nombre;

        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case cod_selecciona:
                    Uri mipath = data.getData();
                    imagen.setImageURI(mipath);
                    break;

                case cod_foto:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Toast.makeText(MNaturalActivity.this, "Ruta de almacenamiento " + path, Toast.LENGTH_SHORT).show();
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    String frag1, frag2, sinespa,indicador="";
                    if (nombre.getText().toString().indexOf(' ', 0) > 0){
                        frag1 = nombre.getText().toString().substring(0,1);
                        int espacio = nombre.getText().toString().indexOf(' ',0);
                        frag2 = nombre.getText().toString().substring(espacio+1, nombre.getText().toString().length());
                        sinespa = frag1.concat(frag2);
                        indicador = sinespa.replace(" ","");
                    }

                    Bundle extras = data.getExtras();
                    Bitmap imagebitmap = (Bitmap) extras.get("data");
                    StorageReference filepath = ChrisMNaturaStorage.child("medicamento").child(indicador);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagebitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] datas = baos.toByteArray();

                    UploadTask uploadTask = filepath.putBytes(datas);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri uri = taskSnapshot.getDownloadUrl();
                            Glide.with(MNaturalActivity.this).load(uri).into(imagen);
                            Toast.makeText(MNaturalActivity.this, "Subido con exito", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MNaturalActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    public void onclickcatalogo(View view){
        Intent intent = new Intent(this, MNaturalCatalogActivity.class);
        startActivity(intent);
    }

    @Override
    public void create(View view) {
        ChrisMNaturaBD = FirebaseDatabase.getInstance();
        ChrisMNaturaBDReference = ChrisMNaturaBD.getReference();
        String frag1, frag2, sinespa,indicador="";

        if (nombre.getText().toString().indexOf(' ', 0) > 0){
            frag1 = nombre.getText().toString().substring(0,1);
            int espacio = nombre.getText().toString().indexOf(' ',0);
            frag2 = nombre.getText().toString().substring(espacio+1, nombre.getText().toString().length());
            sinespa = frag1.concat(frag2);
            indicador = sinespa.replace(" ","");
        }

        final String finalIndicador = indicador;
        ChrisMNaturaStorage.child("medicamento/"+indicador).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageurl = uri.toString();
                mnaturales = new Mnaturales(imageurl, nombre.getText().toString(), descripcion.getText().toString(),
                        formula.getText().toString(), recomendacion.getText().toString(), sabor.getText().toString(),
                        Double.valueOf(precio.getText().toString()));

                ChrisMNaturaBDReference.child(MNaturalReference.MEDICAMENTO).child(finalIndicador).setValue(mnaturales);

                mnaturales = new Mnaturales(grasa.getSelectedItem().toString(), azucar.getSelectedItem().toString(),
                        sal.getSelectedItem().toString());

                ChrisMNaturaBDReference.child(MNaturalReference.MEDICAMENTO).child(finalIndicador)
                        .child(MNaturalReference.SEMAFORO_NUTRICIONAL).setValue(mnaturales);
                Toast.makeText(MNaturalActivity.this, "Nuevo Medicamento" + nombre.getText().toString() + " ingresado"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void read(View view) {

    }

    @Override
    public void update(View view) {

    }

    @Override
    public void delete(View view) {
        String frag1 = nombre.getText().toString().substring(0,1);
        int espacio = frag1.indexOf(' ',0);
        String frag2 = nombre.getText().toString().substring(espacio+1, nombre.getText().toString().length());
        String sinespa = frag1.concat(frag2);
        String indicador = sinespa.replace(" ","");
        ChrisMNaturaBD = FirebaseDatabase.getInstance();
        ChrisMNaturaBDReference = ChrisMNaturaBD.getReference();

        ChrisMNaturaBDReference.child(MNaturalReference.MEDICAMENTO).child(indicador).removeValue();
    }
}
