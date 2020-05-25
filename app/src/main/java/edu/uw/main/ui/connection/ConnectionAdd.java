package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentConnectionAddBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionAdd extends Fragment {

    private FragmentConnectionAddBinding binding;

    public ConnectionAdd() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Connection List");
        binding = FragmentConnectionAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionAddDirections.actionConnectionAddFragmentToChange());
        }
        super.onResume();
    }
}
