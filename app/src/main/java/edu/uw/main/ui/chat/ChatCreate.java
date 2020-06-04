package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatCreateBinding;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.connection.ConnectionAddDirections;
import edu.uw.main.ui.connection.ConnectionAddRecyclerViewAdapter;
import edu.uw.main.ui.connection.ConnectionAddViewModel;

/**
 * The Fragment to create a new chat.
 * @author Group 3
 * @version 5/19
 */
public class ChatCreate extends Fragment {

    private FragmentChatCreateBinding binding;

    private ChatCreateViewModel mCreateModel;

    private UserInfoViewModel mModel;

    /**
     * Default Constructor
     */
    public ChatCreate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Create Chatroom");
        binding = FragmentChatCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mCreateModel = new ViewModelProvider(getActivity())
                .get(ChatCreateViewModel.class);

        mModel= new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mCreateModel.connectGet(mModel.getmJwt());

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCreateModel.addChatCreateObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {
                binding.listRoot.setAdapter(new ChatCreateRecyclerViewAdapter(connectionList, mCreateModel, getActivity()));
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
        binding.buttonCreate.setOnClickListener(button -> {
            try {
                createChatRoom(mModel.getmJwt());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        binding.textGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.textGroupName.getText().length() > 0) {
                    binding.buttonCreate.setEnabled(true);
                } else {
                    binding.buttonCreate.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.buttonCreate.setEnabled(false);
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ChatCreateDirections.actionChatCreateToChange());
        }
        super.onResume();
    }

    /**
     * Exit strategy from the create chatroom fragment.
     * @param jwt Java Web Token
     * @throws JSONException
     */
    private void createChatRoom(final String jwt) throws JSONException {
        mCreateModel.connectCreateChatroom(binding.textGroupName.getText().toString(), jwt, mModel.getEmail());
        getActivity().finish();
        getActivity().startActivity(getActivity().getIntent());
        Navigation.findNavController(getView()).navigate(ChatCreateDirections.actionChatCreateToNavigationChat());

    }

}
