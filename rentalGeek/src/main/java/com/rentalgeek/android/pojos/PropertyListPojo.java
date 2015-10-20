package com.rentalgeek.android.pojos;

public class PropertyListPojo {

    public class PropertyList {
        public int id;
        public int count;
        public double rental_complex_latitude;
        public double rental_complex_longitude;
        public boolean starred;
        public int bedroom_count;
        public int monthly_rent_floor;
        public int monthly_rent_ceiling;
        public String headline;
        public int full_bathroom_count;
        public String prop_image;

        public PropertyList(int count, double rental_complex_latitude,
                            double rental_complex_longitude, int bedroom_count,
                            int monthly_rent_floor, int monthly_rent_ceiling,
                            String headline, int full_bathroom_count, boolean starred,
                            int id, String prop_image) {
            super();
            this.id = id;
            this.count = count;
            this.rental_complex_latitude = rental_complex_latitude;
            this.rental_complex_longitude = rental_complex_longitude;
            this.bedroom_count = bedroom_count;
            this.monthly_rent_floor = monthly_rent_floor;
            this.monthly_rent_ceiling = monthly_rent_ceiling;
            this.headline = headline;
            this.full_bathroom_count = full_bathroom_count;
            this.starred = starred;
            this.prop_image = prop_image;
        }

        public boolean isStarred() {
            return starred;
        }

        public void setStarred(boolean starred) {
            this.starred = starred;
        }

    }

}
