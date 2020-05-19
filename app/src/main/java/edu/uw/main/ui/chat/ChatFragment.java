package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatBinding;


/**
 * The main chat fragment page.
 * @author Group 3
 * @version 5/5
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;

    /**
     * Default constructor
     */
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.buttonGroupA.setOnClickListener(button ->
                processFriend());
        binding.buttonGroupB.setOnClickListener(button ->
                processFriend2());
        binding.buttonGroupC.setOnClickListener(button ->
                processFriend3());


    }
    public void processFriend(){
        Navigation.findNavController(getView()).navigate(
                ChatFragmentDirections.actionNavigationChatToGroupFragment()
        );
    }

    public void processFriend2(){
        Navigation.findNavController(getView()).navigate(
                ChatFragmentDirections.actionNavigationChatToGroupFragment2()
        );
    }

    public void processFriend3(){
        Navigation.findNavController(getView()).navigate(
                ChatFragmentDirections.actionNavigationChatToGroupFragment3()
        );
    }
    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ChatFragmentDirections.actionNavigationChatToChange2());
        }
        super.onResume();
    }
}
