package edu.miami.cs.giuseppe.whereamiagain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    //-----------------------------------------------------------------------------
    private static final int MY_PERMISSIONS_REQUEST = 1;

    private LocationManager locationManager;
    private Location currentLocation;
    TextView theText;
    //-----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getPermission(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION})) {
            goOnCreating(true);
        }
    }
    //-----------------------------------------------------------------------------
    private void goOnCreating(boolean havePermission) {

        if (havePermission) {
            setContentView(R.layout.activity_main);
            theText = findViewById(R.id.theText);
            locationManager = (LocationManager)(getSystemService(LOCATION_SERVICE));
            detectLocators();

            String currentText;
            Time timeOfChange;
            currentText = theText.getText().toString();






            currentText += String.format("%.2f %s",currentLocation.getLatitude(),
                    currentLocation.getLatitude() >= 0.0?"N":"S") + "   ";
            currentText += String.format("%.2f %s",currentLocation.getLongitude(),
                    currentLocation.getLongitude() >= 0.0?"E":"W")  + "   ";
            if (currentLocation.hasAccuracy()) {
                currentText += String.format("%.2fm",currentLocation.getAccuracy());
            }
            currentText += "\n\n";
            theText.setText(currentText);








        } else {
            Toast.makeText(this,"Need permission",Toast.LENGTH_LONG).show();
            finish();
        }

    }
    //-----------------------------------------------------------------------------
    @Override
    public void onDestroy() {

        super.onDestroy();
        locationManager.removeUpdates(this);
    }
    //--------------------------------------------------------
    //-----------------------------------------------------------------------------
    private void detectLocators() {

        List<String> locators;

        locators = locationManager.getProviders(true);
        for (String aProvider : locators) {
            if (aProvider.equals(LocationManager.GPS_PROVIDER)) {
                //findViewById(R.id.select_gps).setEnabled(true);
                Toast.makeText(this,"GPS available",Toast.LENGTH_SHORT).show();
            }
            if (aProvider.equals(LocationManager.NETWORK_PROVIDER)) {
                //findViewById(R.id.select_network).setEnabled(true);
                Toast.makeText(this,"Network available",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    //-----------------------------------------------------------------------------
    public void myClickHandler(View view) {

        ToggleButton gPSButton;
        ToggleButton networkButton;

        gPSButton = findViewById(R.id.select_gps);
        networkButton = findViewById(R.id.select_network);

        try {
            switch (view.getId()) {
                case R.id.select_gps:
//----Stop current requests
                    locationManager.removeUpdates(this);
                    if (gPSButton.isChecked()) {
                        networkButton.setClickable(false);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,getResources().getInteger(
                                        R.integer.time_between_location_updates_ms),0,this);
                    } else {
                        networkButton.setClickable(true);
                    }
                    break;
                case R.id.select_network:
                    locationManager.removeUpdates(this);
                    if (networkButton.isChecked()) {
                        gPSButton.setClickable(false);
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, getResources().getInteger(
                                        R.integer.time_between_location_updates_ms),0,this);
                    } else {
                        gPSButton.setClickable(true);
                    }
                    break;
                case R.id.geolocate:
                    new SensorLocatorDecode(getApplicationContext(),this).
                            execute(currentLocation);
                    break;
                default:
                    break;
            }
        } catch (SecurityException e) {
            Toast.makeText(this,"Sorry, something went wrong",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }
    //-----------------------------------------------------------------------------
    public void onLocationChanged(Location newLocation) {

        TextView locationText;
        String currentText;
        Time timeOfChange;

        locationText = findViewById(R.id.current_location);
        currentText = locationText.getText().toString();

        if (newLocation == null) {
            Toast.makeText(this,"No location",Toast.LENGTH_SHORT).show();
            return;
        }
        currentLocation = newLocation;
        timeOfChange = new Time();
        timeOfChange.set(currentLocation.getTime());
        currentText += timeOfChange.format("%A %D %T") + "   ";
        currentText += "\nProvider " + currentLocation.getProvider() +
                " found location\n";

        currentText += String.format("%.2f %s",currentLocation.getLatitude(),
                currentLocation.getLatitude() >= 0.0?"N":"S") + "   ";
        currentText += String.format("%.2f %s",currentLocation.getLongitude(),
                currentLocation.getLongitude() >= 0.0?"E":"W")  + "   ";
        if (currentLocation.hasAccuracy()) {
            currentText += String.format("%.2fm",currentLocation.getAccuracy());
        }
        currentText += "\n\n";
        locationText.setText(currentText);

        findViewById(R.id.geolocate).setEnabled(true);
    }

     */
    //-----------------------------------------------------------------------------
    public void onProviderDisabled(String provider) {
    }
    //-----------------------------------------------------------------------------
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        String currentText;
        Time timeOfChange;
        currentText = theText.getText().toString();

        if (newLocation == null) {
            Toast.makeText(this,"No location",Toast.LENGTH_SHORT).show();
            return;
        }
        currentLocation = newLocation;
        timeOfChange = new Time();
        timeOfChange.set(currentLocation.getTime());
        currentText += timeOfChange.format("%A %D %T") + "   ";
        currentText += "\nProvider " + currentLocation.getProvider() +
                " found location\n";

        currentText += String.format("%.2f %s",currentLocation.getLatitude(),
                currentLocation.getLatitude() >= 0.0?"N":"S") + "   ";
        currentText += String.format("%.2f %s",currentLocation.getLongitude(),
                currentLocation.getLongitude() >= 0.0?"E":"W")  + "   ";
        if (currentLocation.hasAccuracy()) {
            currentText += String.format("%.2fm",currentLocation.getAccuracy());
        }
        currentText += "\n\n";
        theText.setText(currentText);
    }

    //-----------------------------------------------------------------------------
    public void onStatusChanged(String provider,int status,Bundle extras) {
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
}
//=============================================================================

