package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

public class MapRental {

    // TODO (MINOR): DROP THE MAPRENTALS WRAPPER CLASS AND MAKE CONTAINER OBJECT

    @SerializedName("id") public Integer id;
    @SerializedName("latitude") public Double latitude;
    @SerializedName("longitude") public Double longitude;
    @SerializedName("bedroom_count") public Integer bedroomCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapRental rental = (MapRental) o;

        if (id != null ? !id.equals(rental.id) : rental.id != null) return false;
        if (latitude != null ? !latitude.equals(rental.latitude) : rental.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(rental.longitude) : rental.longitude != null)
            return false;
        return bedroomCount != null ? bedroomCount.equals(rental.bedroomCount) : rental.bedroomCount == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (bedroomCount != null ? bedroomCount.hashCode() : 0);
        return result;
    }
}
