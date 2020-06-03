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
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentConnectionAddBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionAdd extends Fragment {

    private FragmentConnectionAddBinding binding;

    private ConnectionAddViewModel mAddModel;

    public ConnectionAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Connection Add");

      //  binding.textSearch.setFocusableInTouchMode(true);

        return inflater.inflate(R.layout.fragment_connection_add, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAddModel = new ViewModelProvider(getActivity())
                .get(ConnectionAddViewModel.class);
      //  binding.buttonSearch.setEnabled(true);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentConnectionAddBinding.bind(getView());
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mAddModel.addConnectionAddObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ConnectionAddRecyclerViewAdapter(connectionList, mAddModel, getActivity())
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });

        binding.buttonSearch.setOnClickListener(button -> processSearch(model.getmJwt()));
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionAddDirections.actionConnectionAddToChange());
        }
        super.onResume();
    }

    private void processSearch(final String jwt) {
        Log.e("Check Step: ", "3.3");
        Log.e("Check Addition", binding.textSearch.toString());

        mAddModel.connectSearch(binding.textSearch.getText().toString(), jwt);
    }
}
