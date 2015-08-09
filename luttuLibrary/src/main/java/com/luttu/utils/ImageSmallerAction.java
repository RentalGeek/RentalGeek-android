package com.luttu.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;

public class ImageSmallerAction {

	private static int dimension;


	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(res, resId, option);
		option.inSampleSize = calculateInSampleSize(option, reqWidth, reqHeight);
		option.inTargetDensity = 1;
		option.inJustDecodeBounds = false;
		option.inScaled = false;
		return BitmapFactory.decodeResource(res, resId, option);

	}

	public Bitmap decodeSampledBitmapFromGallery(String path, int reqWidth,
			int reqHeight) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		option.inScaled = false;
		BitmapFactory.decodeFile(path, option);
		option.inSampleSize = calculateInSampleSize(option, reqWidth, reqHeight);
		option.inJustDecodeBounds = false;
		option.inScaled = false;
		return BitmapFactory.decodeFile(path, option);

	}

	public Bitmap decodeSampledBitmapFromCamera(Uri fileUri, int reqWidth,
			int reqHeight) {
		System.out.println("inside decode----" + fileUri.getPath());
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		option.inScaled = false;
		BitmapFactory.decodeFile(fileUri.getPath(), option);
		option.inSampleSize = calculateInSampleSize(option, reqWidth, reqHeight);
		option.inJustDecodeBounds = false;
		option.inScaled = false;
		return BitmapFactory.decodeFile(fileUri.getPath(), option);

	}

	private int calculateInSampleSize(Options option, int reqWidth,
			int reqHeight) {
		// TODO Auto-generated method stub
		int imageHeight = option.outHeight;
		int imageWidth = option.outWidth;
		int inSampleSize = 1;
		if (imageHeight > reqHeight || imageWidth > reqWidth) {
			int halfHeight = imageHeight / 2;
			int halfWidth = imageWidth / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					|| (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
//		int dimension = getSquareCropDimensionForBitmap(resizedBitmap);
//		resizedBitmap = ThumbnailUtils.extractThumbnail(resizedBitmap, dimension, dimension, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		return resizedBitmap;

	}

	public static Bitmap decodeFile(String path) {

		int orientation;

		try {

			if (path == null) {

				return null;
			}
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 4;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);

			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);
			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == 3)) {

				m.postRotate(180);
				m.postScale((float) bm.getWidth(), (float) bm.getHeight());

				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 6) {

				m.postRotate(90);

				Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}

			else if (orientation == 8) {

				m.postRotate(270);

				Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}
	
	
//	public static int getSquareCropDimensionForBitmap(Bitmap bitmap)
//	{
//	    //If the bitmap is wider than it is tall
//	    //use the height as the square crop dimension
//	    if (bitmap.getWidth() >= bitmap.getHeight())
//	    {
//	        dimension = bitmap.getHeight();
//	    }
//	    //If the bitmap is taller than it is wide
//	    //use the width as the square crop dimension
//	    else
//	    {
//	        dimension = bitmap.getWidth();
//	    } 
//	    
//	    
//	    return dimension;
//	}
	
	
}
