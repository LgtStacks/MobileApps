package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.MainActivity;
import edu.uw.main.databinding.FragmentConnectionSentRequestBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionSentRequest extends Fragment {

    private FragmentConnectionSentRequestBinding binding;

    private ConnectionSentViewModel mSentModel;

    public ConnectionSentRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Connection Pending");
        binding = FragmentConnectionSentRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mSentModel = new ViewModelProvider(getActivity())
                .get(ConnectionSentViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mSentModel.addSentObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ConnectionSentRecyclerViewAdapter(connectionList, mSentModel, getActivity())
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
         getSentRequests(model.getmJwt());
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionSentRequestDirections.actionConnectionSentRequestToChange());
        }
        super.onResume();
    }

    private void getSentRequests(final String jwt) {
        mSentModel.connectGetSentRequests(jwt);
    }
}
