package com.rentalgeek.android.backend.model;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alan R on 10/1/15.
 */
public class Profile {

    private String id;
    private String ssn;
    private String first_name;
    private String last_name;
    private String born_on;
    private String drivers_license_number;
    private String drivers_license_state;
    private String phone_number;
    private String pets_description;
    private String vehicles_description;
    private boolean was_ever_evicted;
    private String was_ever_evicted_explanation;
    private boolean is_felon;
    private String is_felon_description;
    private String character_reference_name;
    private String character_reference_contact_info;
    private String character_reference_email;
    private String emergency_contact_name;
    private String emergency_contact_phone_number;
    private String emergency_contact_email;
    private String current_home_street;
    private String current_home_city;
    private String current_home_state;
    private String current_home_zipcode;
    private String current_home_moved_in_on;
    private String current_home_dissatisfaction_explanation;
    private String current_home_owner;
    private String current_home_owner_contact_info;
    private String previous_home_street;
    private String previous_home_city;
    private String previous_home_state;
    private String previous_home_zipcode;
    private String previous_home_moved_in_on;
    private String previous_home_moved_out;
    private String previous_home_dissatisfaction_explanation;
    private String previous_home_owner;
    private String previous_home_owner_contact_info;
    private String employment_status;
    private String current_employment_supervisor;
    private String cosigner_name;
    private String cosigner_email_address;
    private String desires_to_move_in_on;
    private String geek_score;

    public List<String> getFieldNames() {

        Field[] fields = Profile.class.getDeclaredFields();

        List<String> field_names = new LinkedList<>();

        for(Field field : fields) {
            if( ! field.isAccessible() )
                field.setAccessible(true);

            field_names.add(field.getName());
        }

        return field_names;
    }

    public synchronized Object get(String param) {

        try {
            Field field = Profile.class.getDeclaredField(param);

            if( ! field.isAccessible() )
                field.setAccessible(true);

            return field.get(this);
        }

        catch (NoSuchFieldException e) {
            System.out.println("Parameter not found");

        }

        catch (IllegalAccessException e) {
            System.out.println("Illegal access to class");
        }

        return null;
    }

    public synchronized void set(String param, Object value) {
        if( param == null || value == null)
            return;

        try {
            Field field = Profile.class.getDeclaredField(param);

            if( ! field.isAccessible() )
                field.setAccessible(true);

            field.set(this,value);

            System.out.println(String.format("Setting %s to %s",param,value.toString()));
        }

        catch(NoSuchFieldException e) {
            System.out.println("Parameter not found");
        }

        catch (IllegalAccessException e) {
            System.out.println("Illegal access to class");
        }
    }
}