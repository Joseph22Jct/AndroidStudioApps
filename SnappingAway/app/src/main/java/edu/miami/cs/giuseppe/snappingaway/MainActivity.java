package edu.miami.cs.giuseppe.snappingaway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback{

    private static final int MY_PERMISSIONS_REQUEST = 1;

    private static final boolean SAVE_TO_FILE = true;
    private String cameraFileName;

    private SurfaceView cameraPreview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean cameraInUse;
    private ImageView currentPhoto;
    Bitmap photoBitmap;
    FileOutputStream photoStream;

    //-----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            goOnCreating(true);
        }
    }
    //-----------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {

        if (havePermission) {
            setContentView(R.layout.activity_main);

//----Prepare the surface view
            cameraPreview = findViewById(R.id.surface_camera);
            surfaceHolder = cameraPreview.getHolder();
            surfaceHolder.addCallback(this);
            currentPhoto = findViewById(R.id.takenPicture);

            cameraFileName =Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString() + "/" +
                    getString(R.string.camera_file_name);

            openCamera();

        } else {
            Toast.makeText(this,"Need permission",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //------------------------------------------
    @Override
    protected void onDestroy(){
        super.onDestroy();
        closeCamera();
    }

    //-------------------------------------------

    public void myCameraClickListener(View view) {



        switch (view.getId()) {
            case R.id.PhotoButton:
                camera.takePicture(null,null,null,this);
                break;
            case R.id.ExitButton:
                camera.stopPreview();
                try {

                    photoStream = new FileOutputStream(cameraFileName);
                    photoBitmap.compress(CompressFormat.JPEG,100,photoStream);
                    photoStream.close();
                    Toast.makeText(this,"File created in SD card: TakenPic.jpg",
                            Toast.LENGTH_LONG).show();
                    finish();

                } catch (IOException e) {
                    Toast.makeText(this,"ERROR: Cannot save photo to file",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                break;

                default:
                    break;

        }

    }


    //-----------------------------------------------------------------------------
    private void openCamera() {

        if ((camera = Camera.open(0)) == null) {
            Toast.makeText(getApplicationContext(),"Camera not available!",
                    Toast.LENGTH_LONG).show();
        } else {

            cameraInUse = true;
        }
    }
    //-----------------------------------------------------------------------------
    private void closeCamera() {


        camera.stopPreview();
        camera.release();
        cameraInUse = false;
    }
    //-----------------------------------------------------------------------------
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            camera.setPreviewDisplay(holder);

//----This will make the surface be changed
            camera.startPreview();

        } catch (Exception e) {
            //----Do something
        }
    }
    //-----------------------------------------------------------------------------
    public void surfaceChanged(SurfaceHolder holder,int format,int width,
                               int height) {

        Camera.Parameters cameraParameters;
        boolean sizeFound;


        sizeFound = false;
        cameraParameters = camera.getParameters();
        for (Size size : cameraParameters.getSupportedPreviewSizes()) {
            if (size.width == width || size.height == height) {
                width = size.width;
                height = size.height;
                sizeFound = true;
                break;
            }
        }
        /*if (sizeFound) {
            cameraParameters.setPreviewSize(width,height);
            camera.setParameters(cameraParameters);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Camera cannot do "+width+"x"+height,Toast.LENGTH_LONG).show();
            finish();
        }

         */

    }
    //-----------------------------------------------------------------------------
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    //-----------------------------------------------------------------------------
    private boolean getPermission(String[] whatPermissions) {

        int index;
        boolean haveAllPermissions;

        haveAllPermissions = true;
        for (index =0; index < whatPermissions.length;index++) {
            if (ContextCompat.checkSelfPermission(this,whatPermissions[index]) !=
                    PackageManager.PERMISSION_GRANTED) {
                haveAllPermissions = false;
            }
        }
        if (haveAllPermissions) {
            return(true);
        } else {
            ActivityCompat.requestPermissions(this,whatPermissions,
                    MY_PERMISSIONS_REQUEST);
            return(false);
        }
    }
    //-----------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,int[] grantResults) {

        int index;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0) {
                    for (index = 0;index < grantResults.length;index++) {
                        if (grantResults[index] !=
                                PackageManager.PERMISSION_GRANTED) {
                            goOnCreating(false);
                            return;
                        }
                    }
                    goOnCreating(true);
                } else {
                    goOnCreating(false);
                }
                return;
            default:
                return;
        }
    }
//-----------------------------------------------------------------------------
public void onPictureTaken(byte[] data,Camera whichCamera) {





    currentPhoto.setVisibility(View.VISIBLE);
    photoBitmap = BitmapFactory.decodeByteArray(data,0,data.length);
    currentPhoto.setImageBitmap(photoBitmap);
    camera.startPreview();



}
//-----------------------------------------------------------------------------



}
