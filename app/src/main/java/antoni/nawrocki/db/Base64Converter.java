package antoni.nawrocki.db;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import antoni.nawrocki.R;

// inspired by: https://stackoverflow.com/questions/41396194/how-to-convert-image-to-string-in-android
public final class Base64Converter {

    private Base64Converter() {
//        private constructor to avoid initialization
    }

    public static String encodeBase64String(int resID, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void decodeBase64StringAndSetImage(String imageData, ImageView imageView) {
//        String imageDataBytes = imageData.substring(imageData.indexOf(",")+1);
//        Log.i("AN", "decodeBase64StringAndSetImage: " + imageData);
        InputStream stream = new ByteArrayInputStream(Base64.decode(imageData.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);

        imageView.setImageBitmap(bitmap);
    }

    public static String encodeBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
