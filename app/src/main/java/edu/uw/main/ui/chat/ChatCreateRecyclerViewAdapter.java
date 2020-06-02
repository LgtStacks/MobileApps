package edu.uw.main.ui.chat;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChatCreateCardBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * The class to handle the recycler view adapter.
 * @author Group 3
 * @version 5/19
 */
public class ChatCreateRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatCreateRecyclerViewAdapter.ConnectionViewHolder> {

    private final List<Contacts> mConnection;

    private ChatCreateViewModel mCreateChatModel;

    private UserInfoViewModel mUserModel;

    private Activity act;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ChatCreateRecyclerViewAdapter(List<Contacts> items, ChatCreateViewModel model, Activity theActivity) {
        this.mConnection = items;
        mCreateChatModel = model;
        act = theActivity;
        ViewModelProvider provider = new ViewModelProvider((ViewModelStoreOwner) theActivity);
        mUserModel = provider.get(UserInfoViewModel.class);
    }

    @Override
    public int getItemCount() {
        return mConnection.size();
    }
    @NonNull
    @Override
    public ChatCreateRecyclerViewAdapter.ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatCreateRecyclerViewAdapter.ConnectionViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_create_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ChatCreateRecyclerViewAdapter.ConnectionViewHolder holder, int position) {
        holder.setConnection(mConnection.get(position), mUserModel.getmJwt());
    }

    /**
     * Inner class for holding each connection.
     */
    public class ConnectionViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private FragmentChatCreateCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public ConnectionViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentChatCreateCardBinding.bind(view);

        }

        /**
         * Updates each connection post.
         * @param user each individual connection post.
         */
        void setConnection(final Contacts user, final String jwt) {
            binding.textContact.setText(user.getUsername());

            binding.layoutInner.setOnClickListener(view -> checkTheBox(user, binding));
            //Use methods in the HTML class to format the HTML found in the text

        }
    }

    private void checkTheBox(final Contacts user, final FragmentChatCreateCardBinding binding) {
        user.setChecked(!binding.checkboxAdd.isChecked());
        binding.checkboxAdd.setChecked(!binding.checkboxAdd.isChecked());
        if (!binding.checkboxAdd.isChecked()) {
            for (int i = 0; i < MainActivity.myContacts.size(); i++) {
                if (MainActivity.myContacts.get(i).getUsername().equals(user.getUsername())) {
                    MainActivity.myContacts.remove(i);
                    break;
                }
            }
        } else {
            MainActivity.myContacts.add(user);
        }
    }
}
