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
import edu.uw.main.databinding.FragmentConnectionListBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A Handles the front page of each connection.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionListFragment extends Fragment {
    private FragmentConnectionListBinding binding;

    private ConnectionListViewModel mModel;

    private UserInfoViewModel mUserModel;

    /**
     * Default constructor.
     */
    public ConnectionListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity())
                .setActionBarTitle("Connection List");
        binding = FragmentConnectionListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentConnectionListBinding binding = FragmentConnectionListBinding.bind(getView());


        mModel.addConnectionListObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ConnectionRecyclerViewAdapter(connectionList)
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
        binding.buttonAddContacts.setOnClickListener(button -> navigateToConnectionAdd());
        binding.buttonPending.setOnClickListener(button -> navigateToConnectionPending());
        binding.buttonSentRequests.setOnClickListener(button -> navigateToSentRequest());
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getActivity().findViewById(R.id.tab_layout);
        view.setVisibility(View.GONE);
        View view2 = getActivity().findViewById(R.id.frameLayout5);
        view2.setVisibility(View.GONE);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionListFragmentDirections.actionNavigationConnectionToChange2());
        }
        super.onResume();
    }
    private void navigateToConnectionAdd() {
        Navigation.findNavController(getView()).navigate(ConnectionListFragmentDirections.actionNavigationConnectionToConnectionAdd());
    }

    private void navigateToConnectionPending() {
        Navigation.findNavController(getView()).navigate(ConnectionListFragmentDirections.actionNavigationConnectionToConnectionPending());
    }

    private void navigateToSentRequest() {
        Navigation.findNavController(getView()).navigate(ConnectionListFragmentDirections.actionNavigationConnectionToConnectionSentRequest());
    }
}
