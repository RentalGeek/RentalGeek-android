package com.rentalgeek.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.rentalgeek.android.ui.activity.ActivityHome;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class UncaughtExceptionReporter implements
		Thread.UncaughtExceptionHandler {

	private final String SUBJECT = "Connected App Crashed";
	private final String SEND_EMAIL = "georgethms10@gmail.com";
	private Context mContext;
	String crashReportString = "";

	public UncaughtExceptionReporter(Context context) {
		mContext = context;
	}

	public void uncaughtException(Thread thread, Throwable throwable) {
		Log.d("Connected",throwable.toString());
		StringBuilder report = new StringBuilder();
		Date currentDate = new Date();
		report.append("\nUncaughtException collected on : "
				+ currentDate.toString());

		report.append("\n\n");
//		report.append(DeviceInfo.getDeviceInformation());
		report.append("\n\n");
		report.append("\n____________________________________________________________________________");
		report.append("\nUncaughtException");
		report.append("\n____________________________________________________________________________");
		report.append("\n[STACK] : ");
		final Writer result = new StringWriter();
		try {
			final PrintWriter printWriter = new PrintWriter(result);
			throwable.printStackTrace(printWriter);
			crashReportString = result.toString();
			Log.d("Connected","crashReportString " + crashReportString);
			report.append(result.toString());
			printWriter.close();
		} catch (Exception e) {
			report.append("\n\n");
		}

		report.append("\n\nCAUSE:");

		try {
			// If the exception was thrown in a background thread inside
			// AsyncTask, then the actual exception can be found with getCause
			Throwable cause = throwable.getCause();
			final PrintWriter printWriter = new PrintWriter(result);
			while (cause != null) {
				cause.printStackTrace(printWriter);
				report.append(result.toString());
				cause = cause.getCause();
			}
			printWriter.close();
		} catch (Exception e) {
			report.append("\n\n");
		}
		report.append("\n____________________________________________________________________________\n\n");
		// System.err.println("REPORT " + report.toString());
		// writeToFile(report.toString());


 		sendMail(report);
	}

	public void sendMail(final StringBuilder errorContent) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				builder.setTitle("Rental Geek App crashed");
				builder.create();
				builder.setNegativeButton(("Cancel"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
							}
						});
				builder.setPositiveButton(("Report"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent sendIntent = new Intent(
										Intent.ACTION_SEND);
								sendIntent.setPackage("com.google.android.gm");
								StringBuilder body = new StringBuilder();
								body.append('\n').append('\n');
								body.append(errorContent).append('\n')
										.append('\n');
								sendIntent.setType("message/rfc822");
								sendIntent.putExtra(Intent.EXTRA_EMAIL,
										new String[] { SEND_EMAIL });
								sendIntent.putExtra(Intent.EXTRA_TEXT,
										crashReportString);
								sendIntent.putExtra(Intent.EXTRA_SUBJECT,
										SUBJECT);
//								sendIntent.putExtra(Intent.EXTRA_CC,
//										"info@suprcall.com");
								mContext.startActivity(sendIntent);
								((ActivityHome) mContext).finish();

							}
						});
				builder.setMessage("Please send us crash message");
				builder.show();
				Looper.loop();
			}
		}.start();
	}

}