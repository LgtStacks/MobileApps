package edu.uw.main.ui.chat.chatAddRemove;

import android.app.Activity;
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
import edu.uw.main.databinding.FragmentAddRemoveCardBinding;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.chat.Contacts;

public class ChatAddRemoveRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatAddRemoveRecyclerViewAdapter.ConnectionViewHolder> {

    private final List<Contacts> mConnection;

    private ChatAddRemoveLiveViewModel mAddRemoveModel;

    private UserInfoViewModel mUserModel;

    private Activity act;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ChatAddRemoveRecyclerViewAdapter(List<Contacts> items, ChatAddRemoveLiveViewModel model, Activity theActivity) {
        this.mConnection = items;
        mAddRemoveModel = model;
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
    public ChatAddRemoveRecyclerViewAdapter.ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatAddRemoveRecyclerViewAdapter.ConnectionViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_add_remove_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAddRemoveRecyclerViewAdapter.ConnectionViewHolder holder, int position) {
        holder.setConnection(mConnection.get(position), mUserModel.getmJwt());
    }

    /**
     * Inner class for holding each connection.
     */
    public class ConnectionViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private FragmentAddRemoveCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public ConnectionViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentAddRemoveCardBinding.bind(view);

        }

        /**
         * Updates each connection post.
         * @param user - each individual connection post.
         * @param jwt - the JSON web token.
         */
        void setConnection(final Contacts user, final String jwt) {
            binding.textContact.setText(user.getUsername());
//
            binding.checkboxAdd.setOnClickListener(view -> checkTheBox(user, binding));
            //Use methods in the HTML class to format the HTML found in the text

        }
    }

    /**
     * Adds all the users that have checkboxes to a list.
     * @param user - User to add.
     * @param binding - Binding for our card.
     */
    private void checkTheBox(final Contacts user, final FragmentAddRemoveCardBinding binding) {
        if (!binding.checkboxAdd.isChecked()) {
            for (int i = 0; i < MainActivity.myAddRemoveSelection.size(); i++) {
                if (MainActivity.myAddRemoveSelection.get(i).getUsername().equals(user.getUsername())) {
                    MainActivity.myAddRemoveSelection.remove(i);
                    break;
                }
            }
        } else {
            MainActivity.myAddRemoveSelection.add(user);
        }
    }
}