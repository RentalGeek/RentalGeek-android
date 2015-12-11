package com.rentalgeek.android.utils;

import com.rentalgeek.android.pojos.Rental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeoLocationUtils {

    private static Map<String, Integer> numDuplicatesAtLocation = new HashMap<>();

    public static Rental[] offsetDuplicateLocations(Rental[] originalRentals) {
        Set<String> locations = new HashSet<>();
        List<Rental> offsetRentals = new ArrayList<>();

        for (Rental rental : originalRentals) {
            if (!locations.add(rental.coordinates())) {
                Rental offsetRental = slightlyOffsetDuplicate(rental);
                offsetRentals.add(offsetRental);
            } else {
                offsetRentals.add(rental);
            }
        }

        numDuplicatesAtLocation.clear();
        return offsetRentals.toArray(new Rental[offsetRentals.size()]);
    }

    private static Rental slightlyOffsetDuplicate(Rental rental) {
        Integer countAtThisLocation = 1;
        if (numDuplicatesAtLocation.containsKey(rental.coordinates())) {
            countAtThisLocation = numDuplicatesAtLocation.get(rental.coordinates());
            numDuplicatesAtLocation.put(rental.coordinates(), ++countAtThisLocation);
        } else {
            numDuplicatesAtLocation.put(rental.coordinates(), countAtThisLocation);
        }

        String tinyOffset = "0.00" + Integer.toString(countAtThisLocation);
        Double tinyOffsetNum = Double.parseDouble(tinyOffset);
        rental.offsetLatitude(tinyOffsetNum);
        rental.offsetLongitude(tinyOffsetNum);

        return rental;
    }

}
