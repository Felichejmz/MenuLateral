package fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.poter.menulateral.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import org.jivesoftware.smack.ConnectionListener;


import xmpp.XmppService;

public class MainMap extends Fragment{

    Button btnConnect;
    TextView tvStatus;

    MapView mapView;
    GoogleMap map;
    LatLng latLng = new LatLng(19.7035261,-101.1943984);

    ProgressDialog myDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_maps,container,false);

        btnConnect = (Button)v.findViewById(R.id.btnMapConnect);
        tvStatus = (TextView)v.findViewById(R.id.tvMapStatus);
        mapView = (MapView)v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        MapsInitializer.initialize(this.getActivity());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        map.animateCamera(cameraUpdate);

        tvStatus.setText("Desconetado");
        tvStatus.setTextColor(Color.RED);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(), XmppService.class));
                myDialog = new ProgressDialog(v.getContext());
                myDialog.setMessage("Conectando al servidor .....");
                myDialog.setCancelable(false);
                myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                myDialog.show();
            }
        });
        return  v;
    }

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
        map.setMyLocationEnabled(false);
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
