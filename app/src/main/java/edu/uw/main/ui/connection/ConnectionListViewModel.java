package edu.uw.main.ui.connection;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import edu.uw.main.R;

public class ConnectionListViewModel extends AndroidViewModel {
    private MutableLiveData<List<ConnectionPost>> mConnectionList;

    public ConnectionListViewModel(@NonNull Application application) {
        super(application);
        mConnectionList = new MutableLiveData<>();
        mConnectionList.setValue(new ArrayList<>());
    }
    public void addBlogListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ConnectionPost>> observer) {
        mConnectionList.observe(owner, observer);
    }



}
