package app.porter.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import app.porter.com.datastore.parcel;

public class Details extends AppCompatActivity{

    private GoogleMap googleMap;
    private parcel parcels;
    private String result;
    private ImageView imageView;
    private TextView txt1,txt2,txt3,txt4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parcels =  (parcel)getIntent().getSerializableExtra(MainActivity.SER_KEY);
        initUI(parcels);
        ImageButton phone = (ImageButton)findViewById(R.id.imageButton);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+parcels.getPhone()));
                startActivity(callIntent);
            }
        });

    }
    public void initUI(parcel parcels)
    {

        imageView = (ImageView)findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).
                load(parcels.getImage())
                .error(R.mipmap.ic_launcher)
                .noFade()
                .resize(150,150)
                .into(imageView);
        txt1 = (TextView)findViewById(R.id.textView);
        txt2 =(TextView)findViewById(R.id.textView2);
        txt3 = (TextView)findViewById(R.id.textView4);
        txt4 = (TextView)findViewById(R.id.textView5);
        txt1.setText(parcels.getName());
        txt2.setText(parcels.getType());
        txt3.setText("Weight : " + parcels.getWeight());
        txt4.setText("Price : RS. " + parcels.getPrice());
        showMap(parcels);
        geocodeService();
    }
    public void showMap(parcel parcels)
    {
        try {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            final LatLng parcelMap = new LatLng(parcels.getLatitude(), parcels.getLongitude());
            Marker TP = googleMap.addMarker(new MarkerOptions().position(parcelMap).title(parcels.getName()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parcels.getLatitude(), parcels.getLongitude()), 12.0f));
        }catch (Exception e){
            Log.d("google err",e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            shareparcel();
            return true;
        }
        else if(id == R.id.sendsms){
            getNumber();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shareparcel()
    {
        String ShareMessage= "Hey look at this parcel Named "+parcels.getName()+" which is around RS."+parcels.getPrice()
                +" and can be used as utility "+parcels.getType()+" and it weighs around "+parcels.getWeight()
                +".if you're interested just call "+parcels.getPhone()+" There are only "+parcels.getQuantity()+" parcels left out"
                +"and the link is here "+parcels.getLink() + " And Location : "+result;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,ShareMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    public void getNumber()
    {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Details.this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                sendSMS(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
    public void sendSMS(String phone)
    {
       String ShareMessage = "link: "+parcels.getLink();
        Uri uri = Uri.parse("smsto:" + phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", ShareMessage);
        startActivity(it);
    }
    public void geocodeService() {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        result = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(parcels.getLatitude(), parcels.getLongitude(), 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                result = strReturnedAddress.toString();
            }


        } catch (Exception e) {

        }
    }
}