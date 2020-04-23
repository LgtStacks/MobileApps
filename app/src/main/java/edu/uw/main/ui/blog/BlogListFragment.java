package edu.uw.main.ui.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogListFragment extends Fragment {

    public BlogListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        if (view instanceof RecyclerView) {
// //Try out a grid layout to achieve rows AND columns. Adjust the widths of the
// //cards on display
// ((RecyclerView) view).setLayoutManager(new GridLayoutManager(getContext(), 2));
// //Try out horizontal scrolling. Adjust the widths of the card so that it is
// //obvious that there are more cards in either direction. i.e. don't have the cards
// //span the entire witch of the screen. Also, when considering horizontal scroll
// //on recycler view, ensure that thre is other content to fill the screen.
// ((LinearLayoutManager)((RecyclerView) view).getLayoutManager())
// .setOrientation(LinearLayoutManager.HORIZONTAL);
            ((RecyclerView) view).setAdapter(
                    new BlogRecyclerViewAdapter(BlogGenerator.getBlogList()));
        }
        return view;

    }
}
