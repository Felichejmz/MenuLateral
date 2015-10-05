package fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poter.menulateral.R;

import xmpp.XmppConnection;
import xmpp.XmppService;

/**
 * Created by poter on 22/08/15.
 */
public class Acceso extends Fragment implements View.OnClickListener{
    Button btnConectarXMPP;
    EditText etConectarNombre;
    EditText etConectarUser;
    EditText etConectarPasswd;

    String Password;
    String Service;

    ProgressDialog myDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.acceso, container,false);

        btnConectarXMPP = (Button)rootView.findViewById(R.id.btnAccesoConectar);
        etConectarNombre = (EditText)rootView.findViewById(R.id.etAccesoName);
        etConectarUser = (EditText)rootView.findViewById(R.id.etAccesoUser);
        etConectarPasswd = (EditText)rootView.findViewById(R.id.etAccesoPasswd);

        btnConectarXMPP.setOnClickListener(this);

        // Obtiene el usuario y password almacenado
        // solo en caso que se encuentre uno almacenado
        getUserPassword();

        if(!XmppService.getState().equals(XmppConnection.ConnectionState.CONNECTED)){
            btnConectarXMPP.setText("Desconectado");
        }

        if(Service != null){
            etConectarUser.setText(Service);
        }

        if(Password != null){
            etConectarPasswd.setText(Password);
        }

        return rootView;
    }

    public void conectar(){
        etConectarNombre.setText("Se presiono el boton");
    }

    private void getUserPassword(){
        Password = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("xmpp_password",null);
        Service = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("xmpp_user", null);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAccesoConectar){
            save();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        BroadcastReceiver myReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case XmppService.BUNDLE_TO:
                        break;
                    case XmppService.BUNDLE_FROM_XMPP:
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter(XmppService.BUNDLE_FROM_XMPP);
        filter.addAction(XmppService.BUNDLE_TO);
        getActivity().registerReceiver(myReceiver,filter);
    }

    // Almacena el usuario y password ademas se conecta al servidor
    public void save(){
        if(verifyXmppID(etConectarUser.getText().toString()) == false){
            Toast.makeText(getActivity(),"Formato invalido", Toast.LENGTH_LONG).show();
            return;
        }

        if(!XmppService.getState().equals(XmppConnection.ConnectionState.CONNECTED)){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            prefs.edit()
                    .putString("xmpp_user",etConectarUser.getText().toString())
                    .putString("xmpp_password", etConectarPasswd.getText().toString())
                    .commit();

            connectXMPPServer();
            btnConectarXMPP.setText("Conectado");
            Intent intent = new Intent(getActivity(),XmppService.class);
            getActivity().startService(intent);
        }
        else
        {
            btnConectarXMPP.setText("Desconectado");
            Intent intent = new Intent(getActivity(),XmppService.class);
            getActivity().stopService(intent);
        }
    }
    // Ventana de conexión al servidor XMPP
    private void connectXMPPServer(){
        myDialog = new ProgressDialog(getActivity());
        myDialog.setMessage("Conectando al servidor");
        myDialog.setCancelable(false);
        myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whitch) {
                        dialog.dismiss();
                    }
                });
        myDialog.show();
    }

    // Obtiene el usuario y el dominio
    private static boolean verifyXmppID(String userId){
        try {
            String parts[] = userId.split("@");
            if (parts.length != 2)
                return false;
            if (parts[0].length() == 0) {
                return false;
            }
            if (parts[1].length() == 0) {
                return false;
            }
        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }
}
