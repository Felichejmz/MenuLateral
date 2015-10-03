package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.poter.menulateral.R;

/**
 * Created by poter on 22/08/15.
 */
public class Autenticacion extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autenticar, container,false);

        return rootView;
    }
}
