package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.MainActivity;
import edu.uw.main.databinding.FragmentConnectionPendingBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionPending extends Fragment {

    private FragmentConnectionPendingBinding binding;

    private ConnectionPendingViewModel mPendingModel;


    public ConnectionPending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Connection Pending");
        binding = FragmentConnectionPendingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mPendingModel = new ViewModelProvider(getActivity())
                .get(ConnectionPendingViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mPendingModel.addPendingObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {

                binding.listRoot.setAdapter(
                        new ConnectionPendingRecyclerViewAdapter(connectionList, mPendingModel, getActivity())
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }

        });

        getPendingRequests(model.getmJwt());
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionPendingDirections.actionConnectionPendingToChange());
        }
        super.onResume();
    }
    public void updatePendingRequest(){
        String size = binding.listRoot.getAdapter().toString();
        Log.e("PENDING REQUEST LIST COUNT: ", size);
      //  binding.listRoot.getAdapter().notifyItemRemoved();
    }
    private void getPendingRequests(final String jwt) {
        mPendingModel.connectGetPendingRequests(jwt);
    }
}
