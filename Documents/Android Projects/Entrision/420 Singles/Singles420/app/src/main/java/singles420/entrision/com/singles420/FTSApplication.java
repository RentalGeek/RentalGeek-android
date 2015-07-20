package singles420.entrision.com.singles420;

import android.app.Application;

/**
 * Created by travis on 7/14/15.
 */
public class FTSApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        initSingletons();
    }

    protected void initSingletons()
    {
        APIHelper.getInstance();
        APIHelper.getInstance().setContext(this.getApplicationContext());
    }

}

