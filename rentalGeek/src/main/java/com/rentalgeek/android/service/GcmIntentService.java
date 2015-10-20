package com.rentalgeek.android.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.receiver.GcmBroadcastReceiver;

public class GcmIntentService extends NonStopIntentService {

    public GcmIntentService(String name) {
        super(name);
    }

    protected static final String TAG = "GcmIntentService";

    private GoogleCloudMessaging gcm;

    public static String deviceId;

    //private static final String ADMIN_TYPE_LOC_INTERVAL = "loc_interval";
    private static final String MSG_TYPE_JOB_CHANGED = "job_changed";
    private static final String MSG_TYPE_DRIVER_MESSAGE = "driver_message";
    private static final String MSG_TYPE_VEHICLE_CHANGED = "vehicle_changed";
    //private static final String MSG_TYPE_UPGRADE_AVAILABLE = "upgrade_available";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Bundle extras = intent != null ? intent.getExtras() : null;
                gcm = GoogleCloudMessaging.getInstance(this);
                if (gcm == null) return;
                String messageType = gcm.getMessageType(intent);
                AppLogger.log(TAG, "onhandle gcm:" + messageType);
                if (extras != null && !extras.isEmpty()) {
                    if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                        //sendNotification("Send error: " + extras.toString());
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                        //sendNotification("Deleted messages on server: " + extras.toString());
                        // If it's a regular GCM message, do some work.
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                        String message = extras.getString("message");
                        if (!TextUtils.isEmpty(message)) {
//                            Message gcmMessage = Message.deserialize(this, new JSONObject(message));
//                            onGcmMessageReceived(gcmMessage);
                        }
                        //sendNotification("Received: " + extras.toString());
                    }
                }
            } catch (Exception e) {
                AppLogger.log(TAG, e);
            } finally {
                // Release the wake lock provided by the WakefulBroadcastReceiver.
                //GcmBroadcastReceiver.completeWakefulIntent(intent);
            }
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }

    }

    @SuppressWarnings("incomplete-switch")
    private void onGcmMessageReceived(Message message) {
        if (message == null) return;
        try {
//            switch (message.getOperation()) {
//
//                    final String type = message.getType();
//                    AppSystem.Instance.wakeDevice();
//                    if (!TextUtils.isEmpty(type)) {
//                        //final String jobId = message.getTarget();
////                            final String messageData = message.getData();
////                            final String owner = message.getOwner();
////                        if (!TextUtils.isEmpty(messageData)) {
////                            JSONObject msgJson = new JSONObject(messageData);
////                            //final GeekMessage cargoMessage = GeekMessage.deserialize(this, msgJson);
////
////                        }
//                    }
//                    break;
//            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

//	var gcm_message = {
//	"operation":"notify",
//	"type":"driver_message",
//	"data":cargo_message,
//	"target":"?",
//	"owner":driver.id
//}
//var cargo_message = {
//	"guid":"Server Unique Id (if needed)",
//	"data":"custom message payload",
//	"reference_id":"additional id if needed for job/offer/etc",
//	"created_at":timestamp
//}

//	public static void sendToApp(Bundle extras, Context context){
//		Intent newIntent = new Intent();
//		newIntent.setClass(context, DriverStatusActivity.class);
//		newIntent.putExtras(extras);
//		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(newIntent);
//	}

//	@SuppressLint("Wakelock")
//	public static void postNotification(Bundle extras, Context context){
//		//	        SharedPreferences.Editor editor=savedValues.edit();
//		//	        String numOfMissedMessages = context.getString(R.string.num_of_missed_messages);
//		//	        int linesOfMessageCount = 0;
//		//	        for(String key : extras.keySet()){
//		//	            String line = String.format("%s=%s", key, extras.getString(key));
//		//	            editor.putString("MessageLine" + linesOfMessageCount, line);
//		//	            linesOfMessageCount++;
//		//	        }
//		//	        editor.putInt(context.getString(R.string.lines_of_message_count), linesOfMessageCount);
//		//	        editor.putInt(context.getString(R.string.lines_of_message_count), linesOfMessageCount);
//		//	        editor.putInt(numOfMissedMessages, savedValues.getInt(numOfMissedMessages, 0) + 1);
//		//	        editor.commit();
//
//		try {
//			Common.wakeupPhone();
//			postNotification(new Intent(context, DriverStatusActivity.class), context);
//		} catch (Exception e) {
//			AppLogger.log(TAG, e);
//		}
//	}

//	protected static void postNotification(Intent intentAction, Context context){
//		final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAction, Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL);
//		final Notification notification = new NotificationCompat.Builder(context)
//		.setSmallIcon(R.drawable.ic_launcher_cargomatic)
//		.setContentTitle("Message Received!")
//		.setContentText("")
//		.setContentIntent(pendingIntent)
//		.setAutoCancel(true)
//		.build();
//
//		notificationManager.notify(Common.NOTIFY_GCM_MESSAGE, notification);
//	}


}
