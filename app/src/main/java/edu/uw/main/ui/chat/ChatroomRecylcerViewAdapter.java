package edu.uw.main.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatroomCardBinding;
import edu.uw.main.model.UserInfoViewModel;

public class ChatroomRecylcerViewAdapter extends
        RecyclerView.Adapter<ChatroomRecylcerViewAdapter.ChatroomViewHolder> {

    private final List<GroupPost> mChatRoom;
    private UserInfoViewModel mUserModel;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ChatroomRecylcerViewAdapter(List<GroupPost> items) {
        this.mChatRoom = items;
    }

    @Override
    public int getItemCount() {
        return mChatRoom.size();
    }
    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatroomViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position) {
        holder.setConnection(mChatRoom.get(position));
    }

    /**
     * Inner class for holding each connection.
     */
    public class ChatroomViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private FragmentChatroomCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public ChatroomViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentChatroomCardBinding.bind(view);

        }

        /**
         * Updates each connection post.
         * @param chat each individual connection post.
         */
        void setConnection(final GroupPost chat) {
            binding.buttonChatroom.setOnClickListener(view -> Navigation.findNavController(mView).navigate(
                    ChatFragmentDirections
                            .actionNavigationChatToGroupFragment(chat))
            );

            binding.buttonChatroom.setText(chat.getChat());
            //Use methods in the HTML class to format the HTML found in the text

        }
    }
}
