package edu.uw.main.ui.connection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentPendingCardBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * The class to handle the recycler view adapter.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionPendingRecyclerViewAdapter extends
        RecyclerView.Adapter<ConnectionPendingRecyclerViewAdapter.ConnectionViewHolder> {

    private final List<Pending> mConnection;

    private ConnectionPendingViewModel mPendingModel;

    private UserInfoViewModel mUserModel;

    private Activity act;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ConnectionPendingRecyclerViewAdapter(List<Pending> items, ConnectionPendingViewModel model, Activity theActivity) {
        this.mConnection = items;
        mPendingModel = model;
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
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConnectionViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_pending_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
        holder.setConnection(mConnection.get(position), mUserModel.getmJwt());
    }

    /**
     * Inner class for holding each connection.
     */
    public class ConnectionViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private FragmentPendingCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public ConnectionViewHolder(View view) {
            super(view);

            mView = view;
            //How I add listeners to the card layout view stuff
            binding = FragmentPendingCardBinding.bind(view);

        }

        /**
         * Updates each connection post.
         * @param user each individual connection post.
         */
        void setConnection(final Pending user, final String jwt) {
//            binding.textName.setText(user.getUsername());
//
//            binding.buttonAdd.setOnClickListener(button -> getUsername(user, jwt));
            //Use methods in the HTML class to format the HTML found in the text

        }
    }

    private void getUsername(final Pending user, final String jwt) {
//        mPendingModel.connectAdd(user.getUsername(), jwt);
    }
}
