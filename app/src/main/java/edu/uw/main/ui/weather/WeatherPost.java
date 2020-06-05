package edu.uw.main.ui.weather;

import android.graphics.Color;
import android.graphics.fonts.Font;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;
/**
 * The class to handle the weather post cards.
 * @author Group 3
 * @version 6/2
 */
public class WeatherPost implements Serializable {
    private String mTitle;
    private final String mWeather;



    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private String mTitle = "";
        private String mWeather = "";



        public Builder(String title, String weather) {
            this.mTitle = title;
            this.mWeather = weather;

        }


        public WeatherPost build() {
            return new WeatherPost(this);
        }
    }

    /**
     * Constructor of connection post
     * @param builder the connection builder.
     */
    private WeatherPost(final WeatherPost.Builder builder){
        this.mTitle = builder.mTitle;
        this.mWeather = builder.mWeather;

    }
    /**
     * Returns each weather title.
     * @return
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set a weather title color.
     * @return newTitle the colored title for weather
     */
    public SpannableStringBuilder setFont () {
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        SpannableString redSpannable= new SpannableString(getTitle());
        redSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, getTitle().length(), 0);
        spanBuilder.append(redSpannable);
        return spanBuilder;
    }

    /**
     * Returns each weather data.
     * @return
     */
    public String getWeather() {
        return mWeather;
    }

}
