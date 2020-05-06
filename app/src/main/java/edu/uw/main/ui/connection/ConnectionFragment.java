package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentConnectionBinding;

/**
 * The fragment showing the list of connections/friends.
 * @author Group 3
 *  @version 5/5
 */
public class ConnectionFragment extends Fragment {
    private FragmentConnectionBinding binding;
    /**
     * Default constructor
     */
    public ConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConnectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.friendA.setOnClickListener(button ->
                processFriend());
    }

    /**
     * Transition to a friend fragment.
     */
    public void processFriend(){
        Navigation.findNavController(getView()).navigate(
                ConnectionFragmentDirections.actionConnectionFragmentToFriendFragment()
        );
    }
}
