package kayanteam.weather.Retrofit;

import java.util.List;

/**
 * Created by mosta on 5/25/2018.
 */

public class CitisModel {
    public class Model {
        String id, name;
        Croods coord;
        Main main;
        List <Weather> weather;
        Wind wind;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Croods getCroods() {
            return coord;
        }

        public void setCroods(Croods croods) {
            this.coord = croods;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public List <Weather> getWeather() {
            return weather;
        }

        public void setWeather(List <Weather> weather) {
            this.weather = weather;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }
    }

    public class Croods {
        double lat, lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }

    public class Main {
        String temp, pressure, humidity,temp_min,temp_max;

        public String getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(String temp_min) {
            this.temp_min = temp_min;
        }

        public String getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(String temp_max) {
            this.temp_max = temp_max;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }
    }

    public class Wind {
        String speed, deg;

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getDeg() {
            return deg;
        }

        public void setDeg(String deg) {
            this.deg = deg;
        }
    }

    public class Weather {
        String description,icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public List<Model> list;

    public List<Model> getList() {
        return list;
    }

    public void setList(List<Model> list) {
        this.list = list;
    }
}
