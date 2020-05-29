package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        binding = FragmentConnectionAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAddModel = new ViewModelProvider(getActivity())
                .get(ConnectionAddViewModel.class);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mAddModel.addConnectionAddObserver(getViewLifecycleOwner(), connectionList -> {
            if (!connectionList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ConnectionAddRecyclerViewAdapter(connectionList)
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
        binding.buttonSearch.setOnClickListener(button -> processQuery(model.getmJwt()));
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(ConnectionAddDirections.actionConnectionAddToChange());
        }
        super.onResume();
    }

    private void processQuery(final String jwt) {
        mAddModel.connect(binding.textSearch.getText().toString(), jwt);
    }

}
