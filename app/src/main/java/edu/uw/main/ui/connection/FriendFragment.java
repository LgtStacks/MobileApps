package edu.uw.main.ui.connection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.R;

/**
 * A fragment showing a mock friends profile view.
 * @author Group 3
 * @version 5/5
 */
public class FriendFragment extends Fragment {
    /**
     * Default constructor
     */
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }
}
