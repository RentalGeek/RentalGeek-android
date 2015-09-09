package com.rentalgeek.android.storage;

import android.support.v4.util.LruCache;
import com.rentalgeek.android.pojos.Rental;

public class RentalCache {
    private static RentalCache instance;
    
    private final LruCache<String,Rental> cache = new LruCache(2048);//2mb should be plenty...I think

    public static synchronized RentalCache getInstance() {
        if( instance == null ) {
            instance = new RentalCache();
        }

        return instance;
    }

    public void add(Rental rental) {
        synchronized(cache) {
            cache.put(rental.getId(),rental);
        }
    }

    public Rental get(String id) {
        
        if( id == null || id.isEmpty() ) {
            return null;
        }

        synchronized(cache) {
            return cache.get(id);
        }
    }
}
