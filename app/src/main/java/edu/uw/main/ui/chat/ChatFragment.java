package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatBinding;
import edu.uw.main.model.UserInfoViewModel;


/**
 * The main chat fragment page.
 * @author Group 3
 * @version 5/19
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;

    private ChatroomListViewModel mModel;
    private UserInfoViewModel mUserModel;

    /**
     * Default constructor.
     */
    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().findViewById(R.id.tab_layout);
        view.setVisibility(View.GONE);
        View view2 = getActivity().findViewById(R.id.frameLayout5);
        View view3 = getActivity().findViewById(R.id.zipCodeLayout);
        View view4 = getActivity().findViewById(R.id.layout_root);
        if (view2 != null) {
            view2.setVisibility(View.GONE);
        }
        if (view3 != null) {
            view3.setVisibility(View.GONE);
        }
        if (view4 != null) {
            view4.setVisibility(View.GONE);
        }

        ((MainActivity) getActivity())
                .setActionBarTitle("Chat Rooms");
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatBinding binding = FragmentChatBinding.bind(getView());

        mModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            if (!chatList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ChatroomRecylcerViewAdapter(chatList)
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
        binding.buttonCreate.setOnClickListener(button -> navigateToCreateChat());
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(ChatroomListViewModel.class);
        mModel.chatGet(mUserModel.getmJwt());
    }


    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ChatFragmentDirections.actionNavigationChatToChange2());
        }
        super.onResume();
    }

    private void navigateToCreateChat() {
       Navigation.findNavController(getView()).navigate(ChatFragmentDirections.actionNavigationChatToChatCreate());
    }
}
