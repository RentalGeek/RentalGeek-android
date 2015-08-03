package com.luttu.fragmentutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.LuttuBaseFragmentActivity.OnBackButtonClickedListener;
import com.luttu.gson.DetailFalse;
import com.luttu.luttulibrary.R;
import com.luttu.utils.GlobalFunctions;
import com.luttu.utils.GlobalFunctions.HttpResponseHandler;
import com.luttu.utils.ImageSmallerAction;
import com.squareup.picasso.Picasso;

/****
 * extend LuttuBaseCamGal for camera operations.
 * */
public abstract class LuttuBaseCamGal extends LuttuBaseFragment {
	Uri fileUri;

	private static int RESULT_LOAD_IMAGE = 1;

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

	ImageSmallerAction imageSmalerObj = new ImageSmallerAction();

	public abstract void parseresult(String response, boolean success, int value);

	public abstract void error(String response);

	public abstract void imagepic(String picpath, boolean gallery, Bitmap bitmap);

	int mround;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			LuttuBaseFragmentActivity mainActivity = (LuttuBaseFragmentActivity) getActivity();
			// mainActivity.backButtonClickedListener = this;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void parse(RequestParams params, final int value, String link,
			boolean progrss) {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		if (progrss)
			progressshow();
		GlobalFunctions.postApiCall(getActivity(), link, params, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {
						// TODO Auto-generated method stub
						progresscancel();
						if (success && getActivity() != null
								&& response != null) {
							if (!response.equals("null")) {
								DetailFalse session = (new Gson()).fromJson(
										response.toString(), DetailFalse.class);
								parseresult(response, session.result, value);
							} else {
								toast("Database error");
							}
						} else if (getActivity() != null) {
							error(response);
							toast("Connection error");
						}
					}
				});
	}

	/****
	 * image picker camera and gallery pass 1 for rounded image. pass 0 for
	 * normal image.
	 * */
	public void picimage(final int round) {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.camgal);
		Button bt_camera = (Button) dialog.findViewById(R.id.bt_camera);
		Button bt_gallery = (Button) dialog.findViewById(R.id.bt_gallery);
		bt_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureImage(round);
				dialog.cancel();
			}
		});
		bt_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				galleryImage(round);
				dialog.cancel();
			}
		});
		dialog.show();

	}

	/****
	 * image picker gallery
	 * */
	public void galleryImage(int round) {
		mround = round;
		Intent in = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(in, RESULT_LOAD_IMAGE);
	}

	/****
	 * image picker camera
	 * */
	public void captureImage(int round) {
		mround = round;
		File photo;
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		String filename = Environment.getExternalStorageDirectory().getPath()
				+ "/Justask/";
		File newfile = new File(filename);

		long lDateTime = new Date().getTime();
		newfile = new File(Environment.getExternalStorageDirectory(),
				String.valueOf("Justask" + lDateTime) + ".jpg");

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newfile));
		fileUri = Uri.fromFile(newfile);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("onactivity");
		try {
			if (requestCode == RESULT_LOAD_IMAGE
					&& resultCode == Activity.RESULT_OK && null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath2 = cursor.getString(columnIndex);
				cursor.close();
				int file_size = Integer.parseInt(String.valueOf(new File(
						picturePath2).length() / 1024));
				System.out.println("picture path" + picturePath2 + "  "
						+ file_size);
				resize(new File(picturePath2));
			} else {
				if (resultCode == Activity.RESULT_OK) {
					System.out.println("picture path" + fileUri.getPath());
					if (fileUri != null) {
						resize(new File(fileUri.getPath()));
					}
				}
			}
		} catch (Exception e) {

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/****
	 * image to rounded bitmap.
	 * */
	public static Bitmap getCircleBitmap(Bitmap bitmap, int width, int height) {
		Bitmap croppedBitmap = scaleCenterCrop(bitmap, width, height);
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, width, height);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		int radius = 0;
		if (width > height) {
			radius = height / 2;
		} else {
			radius = width / 2;
		}

		canvas.drawCircle(width / 2, height / 2, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(croppedBitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight,
			int newWidth) {
		if (source == null) {
			return source;
		}
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	/****
	 * save bitmap to sdcard.
	 * */
	public void savesdcard(final Bitmap bmp) {
		// TODO Auto-generated method stub
		progressshow();
		File dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/Justask/");
		try {
			dir.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String filename = Environment.getExternalStorageDirectory()
						.getPath() + "/Justask/";
				File newfile = new File(filename);
				String Unedited_Img_Name = "Justask"
						+ String.valueOf(System.currentTimeMillis()) + ".jpg";
				File file = new File(newfile, Unedited_Img_Name);
				FileOutputStream fOut;
				try {
					fOut = new FileOutputStream(file);
					bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
					fOut.flush();
					fOut.close();
					bmp.recycle();
				} catch (Exception e) { // TODO

				}
				return null;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				// TODO Auto-generated method stub
				progresscancel();
				toastsuccess("Image saved to sdcard");
				super.onPostExecute(result);

			}
		}.execute();
	}

	/****
	 * resize bitmap.
	 * */
	public File resize(final File f) {
		// TODO Auto-generated method stub
		File dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/Justask/");
		try {
			dir.mkdir();
		} catch (Exception e) {
			e.printStackTrace();

		}
		String filename = Environment.getExternalStorageDirectory().getPath()
				+ "/Justask/";
		File newfile = new File(filename);
		String Unedited_Img_Name = "Justask"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		final File file = new File(newfile, Unedited_Img_Name);

		progressshow();
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// TODO Auto-generated method stub
				// Bitmap bitmap =
				try {
					Bitmap bmp = imageSmalerObj.decodeSampledBitmapFromGallery(
							f.getAbsolutePath(), 300, 300);
					// Bitmap b =
					// ImageSmallerAction.decodeFile(f.getAbsolutePath());
					// Bitmap b = ImageSmallerAction.getResizedBitmap(bitmap,
					// 150,
					// 150);
					System.out.println("width " + bmp.getWidth());
					System.out.println("height " + bmp.getHeight());
					Bitmap b = null;

					b = Picasso.with(getActivity()).load(f)
							.resize(bmp.getWidth(), bmp.getHeight()).get();

					FileOutputStream fOut;
					try {
						fOut = new FileOutputStream(file);
						b.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
						fOut.flush();
						fOut.close();
						b.recycle();
					} catch (Exception e) { // TODO

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progresscancel();
				String newfilepath = file.getAbsolutePath();
				int file_size = Integer
						.parseInt(String.valueOf(file.length() / 1024));
				System.out.println("picture path size" + file_size + "  "
						+ file_size);
				Bitmap bitmap = imageSmalerObj.decodeSampledBitmapFromGallery(
						newfilepath, 200, 200);
				if (mround == 0)
					imagepic(newfilepath, true,
							getCircleBitmap(bitmap, 200, 200));
				else
					imagepic(newfilepath, true, bitmap);
			}
		}.execute();
		return file;
	}

}
