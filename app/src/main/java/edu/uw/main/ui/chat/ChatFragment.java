package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
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
        binding.buttonGroup.setOnClickListener(button ->
                processFriend());
    }
    public void processFriend(){
        Navigation.findNavController(getView()).navigate(
                ChatFragmentDirections.actionNavigationChatToGroupFragment()
        );
    }
}
