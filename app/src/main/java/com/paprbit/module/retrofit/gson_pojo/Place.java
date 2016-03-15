package com.paprbit.module.retrofit.gson_pojo;

import java.io.Serializable;

/**
 * Created by ankush38u on 3/15/2016.
 */
public class Place implements Serializable {


    public String id;
    public String name;
    public String reference;
    public String icon;
    public String vicinity;
    public Geometry geometry;
    public String formatted_address;
    public String formatted_phone_number;
    public String place_id;
    public OpeningHours opening_hours;

    public static class Geometry implements Serializable {

        public Location location;

        public Location getLocation() {
            return location;
        }
    }

    public static class Location implements Serializable {

        public double lat;
        public double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    public static class OpeningHours implements Serializable {
        public boolean isOpen_now() {
            return open_now;
        }

        public boolean open_now;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReference() {
        return reference;
    }

    public String getIcon() {
        return icon;
    }

    public String getVicinity() {
        return vicinity;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public String getPlace_id() {
        return place_id;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", reference='" + reference + '\'' +
                ", icon='" + icon + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", geometry=" + geometry +
                ", formatted_address='" + formatted_address + '\'' +
                ", formatted_phone_number='" + formatted_phone_number + '\'' +
                ", place_id='" + place_id + '\'' +
                ", opening_hours=" + opening_hours +
                '}';
    }
}
