package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatAddRemoveBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatAddRemove extends Fragment {

    private FragmentChatAddRemoveBinding binding;

    private boolean isAdd;

    private ChatAddRemoveLiveViewModel mAddRemoveModel;

    private int chatID;

    private UserInfoViewModel mModel;

    private GroupPost previousChatRoom;

    public ChatAddRemove() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isAdd = ChatAddRemoveArgs.fromBundle(getArguments()).getIsAdd();
        chatID = ChatAddRemoveArgs.fromBundle(getArguments()).getChatID();
        previousChatRoom = ChatAddRemoveArgs.fromBundle(getArguments()).getGrouppost();

        mAddRemoveModel = new ViewModelProvider(getActivity())
                .get(ChatAddRemoveLiveViewModel.class);

        mModel= new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        if (!isAdd) mAddRemoveModel.connectGetRemove(mModel.getmJwt(), chatID);
        else mAddRemoveModel.connectGetAdd(mModel.getmJwt(), chatID);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isAdd) ((MainActivity) getActivity()).setActionBarTitle("Add Friends");
        else ((MainActivity) getActivity()).setActionBarTitle("Remove Friends");
        binding = FragmentChatAddRemoveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddRemoveModel.addAddRemoveObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {
                binding.listRoot.setAdapter(new ChatAddRemoveRecyclerViewAdapter(connectionList, mAddRemoveModel, getActivity()));
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
        if (isAdd) {
            binding.buttonAddRemove.setText("Add Users");
            binding.buttonAddRemove.setOnClickListener(button -> addFriend());
        } else {
            binding.buttonAddRemove.setOnClickListener(button -> removeFriend());
            binding.buttonAddRemove.setText("Remove Users");
        }
    }

    /**
     * This method adds a friend to the chat. Calls the backend endpoint.
     */
    private void addFriend() {
        try {
            for (int i = 0; i < MainActivity.myAddRemoveSelection.size(); i++) {
                mAddRemoveModel.connectAddToRoom(mModel.getmJwt(), chatID, MainActivity.myAddRemoveSelection.get(i).getUsername());
            }
            MainActivity.myAddRemoveSelection.clear();
            Navigation.findNavController(getView()).navigate(ChatAddRemoveDirections.actionChatAddRemoveToGroupFragment(previousChatRoom));
            getActivity().onBackPressed();
            getActivity().onBackPressed();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method removes a friend to the chat. Calls the backend endpoint.
     */
    private void removeFriend() {
        try {
            for (int i = 0; i < MainActivity.myAddRemoveSelection.size(); i++) {
                mAddRemoveModel.connectRemoveFromRoom(mModel.getmJwt(), chatID, MainActivity.myAddRemoveSelection.get(i).getUsername());
            }
            Navigation.findNavController(getView()).navigate(ChatAddRemoveDirections.actionChatAddRemoveToGroupFragment(previousChatRoom));
            getActivity().onBackPressed();
            getActivity().onBackPressed();
            MainActivity.myAddRemoveSelection.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
