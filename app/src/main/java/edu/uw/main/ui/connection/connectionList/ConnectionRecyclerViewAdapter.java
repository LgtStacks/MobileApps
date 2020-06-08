package edu.uw.main.ui.connection.connectionList;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentConnectionCardBinding;

/**
 * The class to handle the recycler view adapter.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionRecyclerViewAdapter extends
        RecyclerView.Adapter<ConnectionRecyclerViewAdapter.ConnectionViewHolder> {

    private final List<ConnectionPost> mConnection;

    private ConnectionListViewModel mListModel;
    
    private String jwt;



    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public ConnectionRecyclerViewAdapter(List<ConnectionPost> items, ConnectionListViewModel model, String jwt) {

        this.mConnection = items;
        this.mListModel = model;
        this.jwt = jwt;
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
        holder.setConnection(mConnection.get(position),this.jwt, mListModel, position, this);
    }

    /**
     * Inner class for holding each connection.
     */
    public class ConnectionViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private FragmentConnectionCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public ConnectionViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentConnectionCardBinding.bind(view);

        }

        /**
         * Updates each connection post.
         * @param connection each individual connection post.
         */
        void setConnection(final ConnectionPost connection, final String jwt, final ConnectionListViewModel mModel,
                           final int position, ConnectionRecyclerViewAdapter adapter) {

            binding.textName.setText(connection.getConnection());

            binding.buttonRemove.setOnClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Remove Contact");
                alert.setMessage("Are you sure you want to remove this contact?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConnection.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, mConnection.size());
                        adapter.notifyDataSetChanged();
                        mModel.connectDelete(jwt, connection.getConnection());
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

            });
            //Use methods in the HTML class to format the HTML found in the text

        }
    }


}
