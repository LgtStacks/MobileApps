package edu.uw.main.ui.connection;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentConnectionCardBinding;
import edu.uw.main.model.UserInfoViewModel;


public class ConnectionRecyclerViewAdapter extends
        RecyclerView.Adapter<ConnectionRecyclerViewAdapter.ConnectionViewHolder> {

    private final List<ConnectionPost> mConnection;
    private UserInfoViewModel mUserModel;

    public ConnectionRecyclerViewAdapter(List<ConnectionPost> items) {
        this.mConnection = items;
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
                .inflate(R.layout.fragment_connection_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
        holder.setConnection(mConnection.get(position));
    }

    public class ConnectionViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private FragmentConnectionCardBinding binding;

        public ConnectionViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentConnectionCardBinding.bind(view);

        }


        void setConnection(final ConnectionPost connection) {
            binding.buttonName.setOnClickListener(view ->Navigation.findNavController(mView).navigate(
                    ConnectionListFragmentDirections
                            .actionNavigationConnectionToConnectionPostFragment(connection))
            );

            binding.buttonName.setText(connection.getConnection());
            //Use methods in the HTML class to format the HTML found in the text

        }
    }
}
