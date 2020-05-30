package edu.uw.main.ui.weather;

import java.io.Serializable;

public class WeatherPost implements Serializable {
    private final String mTitle;
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
     * Returns each weather data.
     * @return
     */
    public String getWeather() {
        return mWeather;
    }

}
