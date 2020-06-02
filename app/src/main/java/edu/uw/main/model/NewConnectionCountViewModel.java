package edu.uw.main.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 *  This class will monitor the Connections added.
 *  @author Group 3
 *  @version 6/1
 */
public class NewConnectionCountViewModel extends ViewModel {

    private MutableLiveData<Integer> mNewConnectionCount;

    /**
     * Default constructor method to generate the new mutable live data.
     */
    public NewConnectionCountViewModel() {
        mNewConnectionCount = new MutableLiveData<>();
        mNewConnectionCount.setValue(0);
    }

    /**
     * This method will observe the connection count
     * @param owner of the overall lifecycle of the app.
     * @param observer monitor the change to the apps messages
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewConnectionCount.observe(owner, observer);
    }

    /**
     * Increment will increment the count of connection notifications by one.
     */
    public void increment() {
        mNewConnectionCount.setValue(mNewConnectionCount.getValue() + 1);
    }

    /**
     * Reset will set the connection count to zero
     */
    public void reset() {
        mNewConnectionCount.setValue(0);
    }
}
