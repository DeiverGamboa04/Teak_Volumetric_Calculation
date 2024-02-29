package com.example.teakvolumetriccalculation;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.tensorflow.lite.Interpreter;

import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;

import com.example.teakvolumetriccalculation.ml.ModelAlturc;
import com.example.teakvolumetriccalculation.ml.ModelDiamet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.text.Text;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity /*implements OnSuccessListener<Text>, OnFailureListener*/{

    public static int REQUEST_GALLERY = 222;
    public static int REQUEST_CAMERA = 111;

    Bitmap mSelectedImage;
    ImageView mImageView;
    TextView txtResults;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, compartir, repositorio, tema, calculadora, repositoriomanual;

    int imageSize = 224;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.image_view);
        txtResults = findViewById(R.id.txtresults);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.home);
        configuracion = findViewById(R.id.settings2);
        compartir = findViewById(R.id.share);
        repositorio = findViewById(R.id.report2);
        tema = findViewById(R.id.theme);
        calculadora = findViewById(R.id.calculate);
        repositoriomanual = findViewById(R.id.repositorio);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(MainActivity.this, ConfiguracionActivity.class);
            }
        });
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(MainActivity.this, CompartirActivity.class);
            }
        });
        repositorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(MainActivity.this, RepositorioActivity3.class);
            }
        });
        tema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(MainActivity.this, TemaActivity.class);
            }
        });
        calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(MainActivity.this, CalculoManualActivity.class);
            }
        });
        repositoriomanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(MainActivity.this, RepositorioManualActivity.class);
            }
        });

        }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirecActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public void abrirGaleria(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    public void abrirCamera(View view) {
        showSuccessDialogThenOpenCamera();
    }
    public void showSuccessDialogThenOpenCamera() {
        showSuccessDialog(new Runnable() {
            @Override
            public void run() {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamara, 3);
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }
    public void showSuccessDialog(final Runnable postDialogAction) {
        ConstraintLayout successConstrainLayout = findViewById(R.id.successConstrainLayout);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.success_dialog, successConstrainLayout);
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                if (postDialogAction != null) {
                    postDialogAction.run();
                }
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap mSelectedImage = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(mSelectedImage.getWidth(), mSelectedImage.getHeight());
                mSelectedImage = ThumbnailUtils.extractThumbnail(mSelectedImage, dimension, dimension);
                mImageView.setImageBitmap(mSelectedImage);

                mSelectedImage = Bitmap.createScaledBitmap(mSelectedImage, imageSize, imageSize, false);
                calculo(mSelectedImage);

            }else{
                Uri dat = data.getData();
                Bitmap  mSelectedImage = null;
                try {
                    mSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    /*throw new RuntimeException(e);*/
                    e.printStackTrace();
                }
                mImageView.setImageBitmap(mSelectedImage);

                mSelectedImage = Bitmap.createScaledBitmap(mSelectedImage, imageSize, imageSize, false);
                calculo(mSelectedImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

   public void calculo(Bitmap mSelectedImage){

       try {
            ModelAlturc modelA = ModelAlturc.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            mSelectedImage.getPixels(intValues, 0, mSelectedImage.getWidth(), 0, 0, mSelectedImage.getWidth(), mSelectedImage.getHeight());

            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelAlturc.Outputs outputs = modelA.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] alturaComercial  = outputFeature0.getFloatArray();

            int maxPosAltur = 0;
            float maxAltura = 0;
            for(int i = 0; i < alturaComercial.length; i++){
                if(alturaComercial[i] > maxAltura){
                    maxAltura = alturaComercial[i];
                    maxPosAltur = i;
                }
            }

            // Releases model resources if no longer used.
            modelA.close();

           ModelDiamet modelD = ModelDiamet.newInstance(getApplicationContext());

           TensorBuffer inputFeature01 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
           ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
           byteBuffer1.order(ByteOrder.nativeOrder());

           int[] intValues1 = new int[imageSize * imageSize];
           mSelectedImage.getPixels(intValues, 0, mSelectedImage.getWidth(), 0, 0, mSelectedImage.getWidth(), mSelectedImage.getHeight());

           int pixel1 = 0;
           for(int i = 0; i < imageSize; i++){
               for(int j = 0; j < imageSize; j++){
                   int val = intValues1[pixel1++]; //RGB
                   byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                   byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                   byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
               }
           }

           inputFeature01.loadBuffer(byteBuffer1);

           // Runs model inference and gets result.
           ModelDiamet.Outputs outputs1 = modelD.process(inputFeature01);
           TensorBuffer outputFeature01 = outputs1.getOutputFeature0AsTensorBuffer();

           float[] diametro  = outputFeature01.getFloatArray();

           int maxPosDiam = 0;
           float maxDia = 0;
           for(int i = 0; i < diametro.length; i++){
               if(diametro[i] > maxDia){
                   maxDia = diametro[i];
                   maxPosDiam = i;
               }
           }

           // Releases model resources if no longer used.
           modelD.close();


           String[] classesAlturaC = {"6", "8", "10"};
           String[] classesDiametro = {"0.3565", "0.3183", "0.2324", "0.2419", "0.1337", "0.3311", "0.2578", "0.3056", "0.1655", "0.3151", "0.2165"};

           float alturaComercialValue = Float.parseFloat(classesAlturaC[maxPosAltur]);
           float diametroValue = Float.parseFloat(classesDiametro[maxPosDiam]);

           // Aplicar la formula
           float factorDeForma = 0.7f;
           float constante = 0.7854f;
           float volumen = (float) Math.pow(diametroValue, 2) * alturaComercialValue * factorDeForma * constante;

           String resultadoVoluemen = String.format("%.4f", volumen); // Para dos decimales

           //Muestra del resultado de la fórmula em txtResults
           txtResults.setText("Volumen = " + resultadoVoluemen);


        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public void Guardarfx(View v) {
        /*Guardar en el repositorio*/
    }

    public void Limpiarfx(View v) {
        /*Limpiar*/
        txtResults.setText("");
        mImageView.setImageDrawable(null);
        mImageView.setImageBitmap(null);
    }

}