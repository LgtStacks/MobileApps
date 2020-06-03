package edu.uw.main.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 *  This class will monitor the messages from chat.
 *  @author Group 3
 * @version 6/2
 */
public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewMessageCount;

    /**
     * Default constructor method to generate the new mutable live data.
     */
    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
    }

    /**
     * This method will oberve the message count
     * @param owner of the overall lifecycle of the app.
     * @param observer monitor the change to the apps messages
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     * Increment will increment the count of messages by one.
     */
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    /**
     * Reset will set the message count to zero
     */
    public void reset() {
        mNewMessageCount.setValue(0);
    }
}
