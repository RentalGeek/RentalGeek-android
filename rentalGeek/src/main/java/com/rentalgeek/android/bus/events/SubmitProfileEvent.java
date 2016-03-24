package com.rentalgeek.android.bus.events;

public class SubmitProfileEvent {

    public boolean resubmitting = false;

    public SubmitProfileEvent(boolean resubmitting) {
        this.resubmitting = resubmitting;
    }

}
