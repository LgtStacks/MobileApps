package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentConnectionPostBinding;
import edu.uw.main.model.PushyTokenViewModel;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.auth.Login.LoginViewModel;

/**
 * A The post fragment of each connection.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionPostFragment extends Fragment {
    /** Our login view model. */
    private ConnectionListViewModel mConnectionModel;
    private UserInfoViewModel mUserModel;
    /**
     * Default constructor.
     */
    public ConnectionPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mConnectionModel = new ViewModelProvider(getActivity())
                .get(ConnectionListViewModel.class);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
    }
/**
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
 */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConnectionPostFragmentArgs args = ConnectionPostFragmentArgs.fromBundle(getArguments());
        FragmentConnectionPostBinding binding = FragmentConnectionPostBinding.bind(getView());

        binding.textName.setText(args.getFriend().getConnection());
        binding.buttonRemove.setOnClickListener(button ->
                removeClicked(args.getFriend().getConnection()));
        ((MainActivity) getActivity())
                .setActionBarTitle(args.getFriend().getConnection() + " Profile");

    }

    private void removeClicked(String toDel) {
        mConnectionModel.connectDelete(mUserModel.getmJwt(), toDel);
        Navigation.findNavController(getView()).navigate(
                ConnectionPostFragmentDirections.actionConnectionPostFragmentToNavigationConnection()
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_post, container, false);
        if (view instanceof RecyclerView) {
// //Try out a grid layout to achieve rows AND columns. Adjust the widths of the
// //cards on display
// ((RecyclerView) view).setLayoutManager(new GridLayoutManager(getContext(), 2));
// //Try out horizontal scrolling. Adjust the widths of the card so that it is
// //obvious that there are more cards in either direction. i.e. don't have the cards
// //span the entire witch of the screen. Also, when considering horizontal scroll
// //on recycler view, ensure that there is other content to fill the screen.
// ((LinearLayoutManager)((RecyclerView) view).getLayoutManager())
// .setOrientation(LinearLayoutManager.HORIZONTAL);
            ((RecyclerView) view).setAdapter(
                    new ConnectionRecyclerViewAdapter(ConnectionGenerator.getConnectionsList()));
        }
        return view;
    }
    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionPostFragmentDirections.actionConnectionPostFragmentToChange2());
        }
        super.onResume();
    }
}
