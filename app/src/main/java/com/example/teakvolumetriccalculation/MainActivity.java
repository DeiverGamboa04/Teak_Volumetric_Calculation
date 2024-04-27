package com.example.teakvolumetriccalculation;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.teakvolumetriccalculation.ml.ModelAlturc;
import com.example.teakvolumetriccalculation.ml.ModelDiamet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity /*implements OnSuccessListener<Text>, OnFailureListener*/ {

    public static int REQUEST_GALLERY = 222;
    public static int REQUEST_CAMERA = 111;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;


    StorageReference storageReference;
    /*FirebaseFirestore firebaseFirestore;*/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Uri imagenUri;

    ImageView mImageView;
    TextView txtResults, textvolumenapro, valordediam, valordealtur, valordefor, valordecons;
    Button btGuar;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, repositorio, tema, calculadora, repositoriomanual;

    int imageSize = 224;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageReference = FirebaseStorage.getInstance().getReference();

        mImageView = findViewById(R.id.imageviewteca);
        txtResults = findViewById(R.id.txtresults);
        textvolumenapro = findViewById(R.id.textvolumenapro);
        btGuar = findViewById(R.id.btGuar);
        valordediam = findViewById(R.id.valordediam);
        valordealtur = findViewById(R.id.valordealtur);
        valordefor = findViewById(R.id.valordefor);
        valordecons = findViewById(R.id.valordecons);


        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.home);
        configuracion = findViewById(R.id.settings2);
        tema = findViewById(R.id.theme);
        calculadora = findViewById(R.id.calculate);

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
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirecActivity(Activity activity, Class secondActivity) {
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
                    startActivityForResult(intentCamara, REQUEST_CAMERA);
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                timeStamp,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
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

    private Uri bitmapToUri(Bitmap bitmap) {
        // Asegúrate de tener permiso para escribir en el almacenamiento externo
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Teak Volumetric Calculation");
        if (!storageDir.exists() && !storageDir.mkdirs()) {
                Log.e("TAG", "Error al crear el directorio");
                return null;

        }

        // Crea un archivo de imagen
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File file = new File(storageDir, "JPEG_" + timeStamp + ".jpg");

        try (FileOutputStream out = new FileOutputStream(file)) {
            // Comprime y escribe el bitmap en el archivo
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            Log.e("TAG", "Error al escribir el bitmap", e);
            return null;
        }

        // Retorna el Uri del archivo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(this, "tu.autoridad.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }

        /*String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.e("TAG", "Failed to create directory");
                return null;
            }
        }

        // Crea el archivo
        File imageFile = new File(storageDir, "JPEG_" + timeStamp + ".jpg");
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            out.flush();
        } catch (IOException e) {
            Log.e("TAG", "Error saving image", e);
            return null;
        }

        // Retorna el Uri utilizando un FileProvider
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap mSelectedImage = (Bitmap) data.getExtras().get("data");
                // Otras operaciones con el bitmap...
                int dimension = Math.min(mSelectedImage.getWidth(), mSelectedImage.getHeight());
                mSelectedImage = ThumbnailUtils.extractThumbnail(mSelectedImage, dimension, dimension);
                /*imagenUri = bitmapToUri(mSelectedImage);*/
                mImageView.setImageBitmap(mSelectedImage);
                /*imagenUri = bitmapToUri(mSelectedImage);*/
                /*calculo(imagenUri);*/


                // Otras operaciones con el bitmap...
                /*imagenUri = bitmapToUri(mSelectedImage); // Convierte el bitmap a Uri*/
                /*mImageView.setImageBitmap(mSelectedImage);
                calculo(imagenUri);*/
            } else if (data != null && data.getData() != null) {
                Uri dat = data.getData();
                imagenUri = dat; // Aquí ya tienes un Uri, no necesitas convertirlo
                mImageView.setImageURI(dat);
                calculo(imagenUri);
            }
        }
    }

    public void calculo(Uri imagenUri){
        //Calculo de la formula
       try {
           Bitmap mSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenUri);

            ModelAlturc modelA = ModelAlturc.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

           Bitmap resizedBitmap = Bitmap.createScaledBitmap(mSelectedImage, imageSize, imageSize, true);

            int[] intValues = new int[imageSize * imageSize];
            /*mSelectedImage.getPixels(intValues, 0, mSelectedImage.getWidth(), 0, 0, mSelectedImage.getWidth(), mSelectedImage.getHeight());*/

           resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

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


           Bitmap mSelectedImage10 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenUri);

           ModelDiamet modelD = ModelDiamet.newInstance(getApplicationContext());

           TensorBuffer inputFeature0Diam = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
           ByteBuffer byteBuffer1D = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
           byteBuffer1D.order(ByteOrder.nativeOrder());

           Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(mSelectedImage10, imageSize, imageSize, true);

           int[] intValues1 = new int[imageSize * imageSize];
           /*mSelectedImage.getPixels(intValues, 0, mSelectedImage.getWidth(), 0, 0, mSelectedImage.getWidth(), mSelectedImage.getHeight());*/

           resizedBitmap1.getPixels(intValues1, 0, resizedBitmap1.getWidth(), 0, 0, resizedBitmap1.getWidth(), resizedBitmap1.getHeight());

           int pixel1 = 0;
           for(int i = 0; i < imageSize; i++){
               for(int j = 0; j < imageSize; j++){
                   int val1 = intValues1[pixel1++]; //RGB
                   byteBuffer1D.putFloat(((val1 >> 16) & 0xFF) * (1.f / 255));
                   byteBuffer1D.putFloat(((val1 >> 8) & 0xFF) * (1.f / 255));
                   byteBuffer1D.putFloat((val1 & 0xFF) * (1.f / 255));
               }
           }

           inputFeature0Diam.loadBuffer(byteBuffer1D);

           // Runs model inference and gets result.
           ModelDiamet.Outputs outputs1 = modelD.process(inputFeature0Diam);
           TensorBuffer outputFeature0Diam = outputs1.getOutputFeature0AsTensorBuffer();

           float[] diametro  = outputFeature0Diam.getFloatArray();

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


           String[] classesAlturaC = {"5", "6", "7", "8", "9", "10", "11"};
           String[] classesDiametro = {"0.3565", "0.1655", "0.1719", "0.2165", "0.2228", "0.2324", "0.2387", "0.2547", "0.2578", "0.2674", "0.2706", "0.2960", "0.3056", "0.3119", "0.3151", "0.3183", "0.3215", "0.3310", "0.3374", "0.1337", "0.3692"};

           float alturaComercialValue = Float.parseFloat(classesAlturaC[maxPosAltur]);
           float diametroValue = Float.parseFloat(classesDiametro[maxPosDiam]);

           // Aplicar la formula
           float factorDeForma = 0.7f;
           float constante = 0.7854f;
           float volumen = (float) Math.pow(diametroValue, 2) * alturaComercialValue * factorDeForma * constante;

           /*float volumenaltur =  diametroValue ;*/

           //codigo para obtener dos numeros decimales
           String resultadoVoluemen = String.format("%.2f", volumen); // Para cuatros decimales

           //Muestra del resultado de la fórmula em txtResults
           txtResults.setText("Volumen = " + resultadoVoluemen);

           float diametroaano = 0.03f;
           float alturaaano = 1.6f;
           float volumaano = (float) Math.pow(diametroValue + diametroaano, 2) * alturaComercialValue + alturaaano * factorDeForma * constante;

           String resuvolumano = String.format("%.2f", volumaano);

           textvolumenapro.setText("Volumen de aproximación de 1 año = " + resuvolumano);




          String valordiam = String.format(String.valueOf(diametroValue));
          String valoraltu = String.format(String.valueOf(alturaComercialValue));
          String valorfor = String.format(String.valueOf(factorDeForma));
          String valorcons = String.format(String.valueOf(constante));

          valordediam.setText("D=" + valordiam);
          valordealtur.setText("hc=" + valoraltu);
          valordefor.setText("FF=" + valorfor);
          valordecons.setText("Cc=" + valorcons);

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public void Guardarfx(View v) {
        /*Guardar en el repositorio*/
        if (imagenUri != null) {
            // Mostrar el indicador de progreso
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo...");
            progressDialog.show();

            // Crear una referencia a 'images/miImagen.jpg'
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String fileName = "parcela/" + timeStamp + ".jpg";
            StorageReference fileRef = storageReference.child(fileName);

            fileRef.putFile(imagenUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Imagen subida exitosamente", Toast.LENGTH_LONG).show();
                            // Si quieres obtener la URL de la imagen subida puedes hacerlo aquí
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Uri de la imagen subida
                                    String downloadUrl = uri.toString();
                                    // Aquí puedes guardar la URL en tu base de datos si es necesario

                                    Map<String, Object> dataToSave = new HashMap<>();
                                    dataToSave.put("imageUrl", downloadUrl);
                                    dataToSave.put("diametro", valordediam.getText().toString());
                                    dataToSave.put("alturacomercial", valordealtur.getText().toString());
                                    dataToSave.put("factorforma", valordefor.getText().toString());
                                    dataToSave.put("constante", valordecons.getText().toString());
                                    dataToSave.put("volumen", txtResults.getText().toString());
                                    dataToSave.put("volumenAprox", textvolumenapro.getText().toString());

                                    db.collection("datosParcela").add(dataToSave)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Datos guardados exitosamente", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Error al guardar datos", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Subido " + (int) progress + "%...");
                        }
                    });
        } else {
            Toast.makeText(this, "No hay imagen para subir", Toast.LENGTH_SHORT).show();
        }

    }

    public void Limpiarfx(View v) {
        /*Limpiar*/
        txtResults.setText("");
        textvolumenapro.setText("");
        valordediam.setText("");
        valordealtur.setText("");
        valordefor.setText("");
        valordecons.setText("");
        mImageView.setImageDrawable(null);
        mImageView.setImageBitmap(null);
    }

}