package edu.uw.main.ui.connection;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentSearchCardBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * The class to handle the recycler view adapter.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionAddRecyclerViewAdapter extends
        RecyclerView.Adapter<ConnectionAddRecyclerViewAdapter.ConnectionViewHolder> {

    private final List<Add> mConnection;

    private ConnectionAddViewModel mAddModel;

    private UserInfoViewModel mUserModel;

    private Activity act;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ConnectionAddRecyclerViewAdapter(List<Add> items, ConnectionAddViewModel model, Activity theActivity) {
        this.mConnection = items;
        mAddModel = model;
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
                .inflate(R.layout.fragment_search_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
        holder.setConnection(mConnection.get(position), mUserModel.getmJwt(), mAddModel, position, this);
    }

    /**
     * Inner class for holding each connection.
     */
    public class ConnectionViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private FragmentSearchCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public ConnectionViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentSearchCardBinding.bind(view);

        }

        /**
         * Updates each connection post.
         * @param user each individual connection post.
         */
        void setConnection(final Add user, final String jwt, final ConnectionAddViewModel mModel,
                           final int position, ConnectionAddRecyclerViewAdapter adapter) {
            binding.textName.setText(user.getUsername());

            //binding.buttonAdd.setOnClickListener(button -> getUsername(user, jwt));

            binding.buttonAdd.setOnClickListener(view ->{
                String check = user.getUsername();
                Log.e("INPUT NAME: ", check);
                if(!user.equals(null)){
                    getUsername(user, jwt);
                    mConnection.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, mConnection.size());
                    adapter.notifyDataSetChanged();
                }


                // button -> accept(user.getUsername(), jwt);
            });

            //Use methods in the HTML class to format the HTML found in the text

        }
    }

    private void getUsername(final Add user, final String jwt) {

        mAddModel.connectAdd(user.getUsername(), jwt);
    }

}

