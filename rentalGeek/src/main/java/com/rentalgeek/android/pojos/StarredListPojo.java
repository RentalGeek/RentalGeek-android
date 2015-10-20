package com.rentalgeek.android.pojos;

public class StarredListPojo {

    public class StarredList {
        public String id;
        public String user_id;
        public String rental_offering_id;
        public String property_address;
        public String bedroom_count;
        public String full_bathroom_count;
        public String square_footage_floor;
        public String monthly_rent_floor;
        public String salesy_description;
        public String image;
        public String sold_out;
        public String headline;

        public StarredList(String id, String user_id,
                           String rental_offering_id, String property_address,
                           String bedroom_count, String full_bathroom_count,
                           String square_footage_floor, String monthly_rent_floor,
                           String salesy_description, String image, String sold_out, String headline) {
            super();
            this.id = id;
            this.user_id = user_id;
            this.rental_offering_id = rental_offering_id;
            this.property_address = property_address;
            this.bedroom_count = bedroom_count;
            this.full_bathroom_count = full_bathroom_count;
            this.square_footage_floor = square_footage_floor;
            this.monthly_rent_floor = monthly_rent_floor;
            this.salesy_description = salesy_description;
            this.image = image;
            this.sold_out = sold_out;
            this.headline = headline;
        }


    }

}
