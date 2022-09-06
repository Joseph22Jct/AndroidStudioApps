package edu.miami.cs.giuseppe.datacontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_SELECT_CONTACT = 2;
    private static final int SEND_EMAIL= 3;
    public static final int PICTURE_SELECTED = 1;
    private static final int MY_PERMISSIONS_REQUEST = 4;
    Intent gallery;
    Bitmap selectedPicture;
    Uri selectedURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getPermission(new String[]{
                Manifest.permission.READ_CONTACTS})) {
            goOnCreating(true);
        }
    }
    //-----------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {

        if (havePermission) {
            setContentView(R.layout.activity_main);
            choosePicture();
        } else {
            Toast.makeText(this,"Need permission",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void choosePicture(){
        gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICTURE_SELECTED);
    }

    //---------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ImageView pictureView = findViewById(R.id.chosenimage);


        Uri contactData;
        Cursor contactsCursor;
        String contactName;
        int contactId;

        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {
            case PICTURE_SELECTED:
                if (resultCode == Activity.RESULT_OK) {
                    selectedURI = data.getData();
                    try {
                        selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedURI);
                        //pictureView.setImageBitmap(selectedPicture);
                    } catch (Exception e) {
                        Log.i("ERROR", "could not get the bitmap from tbe URI");
                    }

                    Intent nextIntent;

                    nextIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(nextIntent,ACTIVITY_SELECT_CONTACT);
                    break;

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    finish();
                }

                break;

            case ACTIVITY_SELECT_CONTACT:
                if (resultCode == Activity.RESULT_OK){
                    contactData = data.getData();
                    contactsCursor = getContentResolver().query(contactData,
                            null,null,null,null);
                    if (contactsCursor.moveToFirst()){
                        contactName = contactsCursor.getString(
                                contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        contactId = contactsCursor.getInt(
                                contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));

                        String[] email = new String[1];

                        email[0] = searchForEmailAddressById(contactId);

                        Intent nextIntent = new Intent(Intent.ACTION_SEND);
                        nextIntent.setType("Image/*");
                        if (email!= null && email.length > 0 && email[0].length() !=0) {
                            Log.d("Debug", email[0]);
                            nextIntent.putExtra(Intent.EXTRA_EMAIL,email);
                            nextIntent.putExtra(Intent.EXTRA_SUBJECT, "This email is nice.");
                            nextIntent.putExtra(Intent.EXTRA_TEXT, "Look I wrote for you so you don't have to.");
                            nextIntent.putExtra(Intent.EXTRA_STREAM, selectedURI);

                        }
                        startActivity(Intent.createChooser(nextIntent,"Choose ..."));
                        break;

                    }
                    contactsCursor.close();
                }
                break;
            default:
                break;
        }


    }
//-----------------------------

    private String searchForEmailAddressById(int contactId) {

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                ContactsContract.CommonDataKinds.Email.DATA
        };
        Cursor emailCursor;
        String emailAddress;

        emailCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,projection,"CONTACT_ID = ?",
                new String[]{Integer.toString(contactId)},null);
        if (emailCursor.moveToFirst()) {
            emailAddress = emailCursor.getString(emailCursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DATA));
        } else {
            emailAddress = null;
        }
        emailCursor.close();
        return(emailAddress);
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

}
