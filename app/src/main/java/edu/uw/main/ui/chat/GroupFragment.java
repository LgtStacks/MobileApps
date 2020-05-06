package edu.uw.main.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.R;

/**
 * A mock fragment showing how to interact in a group chat.
 * @author Group 3
 * @version 5/5
 */
public class GroupFragment extends Fragment {
    /**
     * Default constructor
     */
    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }
}
