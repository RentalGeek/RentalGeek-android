package com.rentalgeek.android.mvp.geekscore;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ShowGeekScore;
import com.rentalgeek.android.bus.events.ShowGeekScoreWait;

/**
 * Created by Alan R on 10/3/15.
 */
public class GeekScorePresenter implements Presenter {

    private GeekScoreView geekScoreView;

    public GeekScorePresenter(GeekScoreView geekScoreView) {
        this.geekScoreView = geekScoreView;
    }

    @Override
    public void getGeekScore() {

        String geek_score = SessionManager.Instance.getGeekScore();

        if (geek_score != null && !geek_score.isEmpty()) {
            if (geekScoreView != null) {
                AppEventBus.post(new ShowGeekScore(geek_score));
            }
        } else {
            if (geekScoreView != null) {
                AppEventBus.post(new ShowGeekScoreWait());
            }
        }
    }
}
