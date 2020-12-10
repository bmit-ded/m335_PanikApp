package ch.zli.panikapp.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.zli.panikapp.MainActivity;
import ch.zli.panikapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button button;
    TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;
    String phoneNo;
    String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        button = (Button) view.findViewById(R.id.button);

        //initialisiere fusedlocationproviderclient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        while (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //falls nicht erteilt
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 44);
        }
        while (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //falls nicht erteilt
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //erlaubnis überprüfen
                        //wenn Erlaubnis erteilt
                        getLocation();




            }
        });


        return view;
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                sendSMSMessage(addresses);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void sendSMSMessage(List<Address> addresses) {
        phoneNo = "11";
        message = "I pressed the panic button on my phone. Please send help to this location: ";
        message += "Latitude" + addresses.get(0).getLatitude() + " Longitude :" + addresses.get(0).getLongitude() + " Country :" + addresses.get(0).getCountryName() + " Locality :" + addresses.get(0).getLocality() + " Address :"+ addresses.get(0).getAddressLine(0) ;
        sendSMS(phoneNo, message);
    }


    public void sendSMS(String phoneNo, String message) {


                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getActivity().getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();

                    return;
                }

}