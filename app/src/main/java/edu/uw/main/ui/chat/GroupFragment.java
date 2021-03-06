package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentGroupBinding;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.chat.chatMain.ChatroomListViewModel;
import edu.uw.main.ui.chat.chatMessage.ChatRecyclerViewAdapter;
import edu.uw.main.ui.chat.chatMessage.ChatSendViewModel;
import edu.uw.main.ui.chat.chatMessage.ChatViewModel;

/**
 * A First group chat room.
 * @author Group 3
 * @version 6/2
 */
public class GroupFragment extends Fragment{
    //The chat ID for "global" chat

    private ChatroomListViewModel mModel;

    private ChatSendViewModel mSendModel;
    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;
    private int chatID = -6;
    private String chatName = "NAME";
    public GroupFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mModel = provider.get(ChatroomListViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mSendModel = provider.get(ChatSendViewModel.class);
        chatID = GroupFragmentArgs.fromBundle(getArguments()).getChatRoom().getId();
        chatName = GroupFragmentArgs.fromBundle(getArguments()).getChatRoom().getChat();
        mChatModel.getFirstMessages(chatID, mUserModel.getmJwt());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle(chatName);

        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentGroupBinding binding = FragmentGroupBinding.bind(getView());
        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(
                mChatModel.getMessageListByChatId(chatID),
                mUserModel.getEmail()));


        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(chatID, mUserModel.getmJwt());
        });
        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(chatID,
                    mUserModel.getmJwt(),
                    binding.editMessage.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));

        mChatModel.addMessageObserver(chatID, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

   }
    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(GroupFragmentDirections.actionGroupFragmentToChange2());
        }
        if (MainActivity.changeFragment) {
            MainActivity.changeFragment = false;
        }
        super.onResume();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_sign_out).setVisible(false);
        menu.findItem(R.id.action_add_friend).setVisible(true);
        menu.findItem(R.id.action_remove_friend).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_friend) {
            Navigation.findNavController(getView()).navigate(GroupFragmentDirections
                    .actionGroupFragmentToChatAddRemove(true, chatID, new GroupPost.Builder(chatName, chatID).build()));
        } else if (id == R.id.action_remove_friend) {
            Navigation.findNavController(getView()).navigate(GroupFragmentDirections
                    .actionGroupFragmentToChatAddRemove(false, chatID, new GroupPost.Builder(chatName, chatID).build()));
        }
        return super.onOptionsItemSelected(item);
    }
}
