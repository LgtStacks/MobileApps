package edu.uw.main.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentSuccessBinding;
import edu.uw.main.model.UserInfoViewModel;


/**
 * A framgent to handle the home page.
 * The user has successfully logged into the app.
 * @author Group 3
 * @version 5/5
 */
public class SuccessFragment extends Fragment {

    private FragmentSuccessBinding binding;

    /**
     * Default constructor.
     * @author Group 3
     * @version 5/5
     */
    public SuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getActivity().findViewById(R.id.tab_layout);
        view.setVisibility(View.GONE);
        View view2 = getActivity().findViewById(R.id.frameLayout5);
        View view3 = getActivity().findViewById(R.id.zipCodeLayout);
        View view4 = getActivity().findViewById(R.id.layout_root);
        if (view2 != null) {
            view2.setVisibility(View.GONE);
        }
        if (view3 != null) {
            view3.setVisibility(View.GONE);
        }
        if (view4 != null) {
            view4.setVisibility(View.GONE);
        }
        binding = FragmentSuccessBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity())

                .get(UserInfoViewModel.class);

        //  SuccessFragmentArgs args = SuccessFragmentArgs.fromBundle(getArguments());
        binding.textMessage.setText("Hello " + model.getEmail());

    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(SuccessFragmentDirections.actionNavigationHomeToChange2());
        }
        super.onResume();
    }
}
