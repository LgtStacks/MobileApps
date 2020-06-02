package edu.uw.main.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatBinding;
import edu.uw.main.databinding.FragmentChatroomCardBinding;
import edu.uw.main.model.UserInfoViewModel;

public class ChatroomRecylcerViewAdapter extends
        RecyclerView.Adapter<ChatroomRecylcerViewAdapter.ChatroomViewHolder> {

    private final List<GroupPost> mChatRoom;

    private UserInfoViewModel mUserModel;

    private ChatroomListViewModel mModel;

    private String jwt;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ChatroomRecylcerViewAdapter(List<GroupPost> items, ChatroomListViewModel mModel, String jwt) {
        this.mChatRoom = items;
        this.mModel = mModel;
        this.jwt = jwt;
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
        holder.setConnection(mChatRoom.get(position), mModel, jwt, position, this);
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
        void setConnection(final GroupPost chat, final ChatroomListViewModel mModel, final String jwt,
                           final int position, ChatroomRecylcerViewAdapter adapter) {
            binding.buttonChatroom.setOnClickListener(view -> Navigation.findNavController(mView).navigate(
                    ChatFragmentDirections
                            .actionNavigationChatToGroupFragment(chat))
            );
            binding.buttonChatroom.setOnLongClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Delete ChatRoom");
                alert.setMessage("Are you sure you want to delete this ChatRoom?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChatRoom.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, mChatRoom.size());
                        adapter.notifyDataSetChanged();
                        mModel.chatDelete(chat.getId(), jwt);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;
            });
            binding.buttonChatroom.setText(chat.getChat());
            //Use methods in the HTML class to format the HTML found in the text

        }
    }
}
