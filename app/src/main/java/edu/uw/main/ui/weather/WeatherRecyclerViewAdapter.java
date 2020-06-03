package edu.uw.main.ui.weather;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentWeatherCardBinding;
import edu.uw.main.model.UserInfoViewModel;
/**
 * The class to handle the weather recycler view Adapter.
 * @author Group 3
 * @version 6/2
 */
public class WeatherRecyclerViewAdapter extends
        RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    private final List<WeatherPost> mWeather;
    private UserInfoViewModel mUserModel;

    /**
     * The connection recycler view constructor.
     * @param items list of items posts.
     */
    public WeatherRecyclerViewAdapter(List<WeatherPost> items) {
        this.mWeather = items;
    }

    @Override
    public int getItemCount() {
       // Log.e("SIZE", String.valueOf(mWeather.size()));
        return mWeather.size();
    }
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_card, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setConnection(mWeather.get(position));
    }

    /**
     * Inner class for holding each connection.
     */
    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private FragmentWeatherCardBinding binding;

        /**
         * Inner connect view holder constructor.
         * @param view the view.
         */
        public WeatherViewHolder(View view) {
            super(view);

            mView = view;
            binding = FragmentWeatherCardBinding.bind(view);

        }

        /**
         * Updates each weather post.
         * @param weather each individual weather post.
         */
        void setConnection(final WeatherPost weather) {
            Log.e("Check Data", weather.getWeather());
            binding.textCategory.setText(weather.getTitle());
            binding.textWeatherCurrent2.setText(weather.getWeather());

            //Use methods in the HTML class to format the HTML found in the text

        }
    }
}
