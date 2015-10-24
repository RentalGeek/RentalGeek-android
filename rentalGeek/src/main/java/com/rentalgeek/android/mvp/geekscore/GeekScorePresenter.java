package com.rentalgeek.android.mvp.geekscore;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ShowGeekScore;
import com.rentalgeek.android.bus.events.ShowGeekScoreWait;

/**
 * Created by Alan R on 10/3/15.
 */
public class GeekScorePresenter implements Presenter {

    @Override
    public void getGeekScore() {

        String geek_score = SessionManager.Instance.getGeekScore();

        if (geek_score != null && !geek_score.isEmpty()) {
                System.out.println("Showing Geek score.");
                AppEventBus.post(new ShowGeekScore(geek_score));
        } else {
            System.out.println("Showing Geek score wait");
                AppEventBus.post(new ShowGeekScoreWait());
        }
    }
}
