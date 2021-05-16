package com.classicinvoice.app.classes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.classicinvoice.app.SplashActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;


@SuppressWarnings("deprecation")
public class GlobalFunctions {

    static ArrayList<Integer> startIndex, endIndex;

    private static int PERMISSION_CODE = 23;

    public static File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public static Bitmap fixOrientationFromCamIntent(String path, Bitmap mBitmap) {
        try {
            File f = new File(path);
            if (mBitmap.getWidth() > mBitmap.getHeight()) {
                Matrix mat = new Matrix();
                mat.postRotate(90);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
                        null, options);
                Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                        bmp.getHeight(), mat, true);
                // ByteArrayOutputStream outstudentstreamOutputStream = new
                // ByteArrayOutputStream();
                // bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                // outstudentstreamOutputStream);
                return bitmap;
            } else {
                return mBitmap;
            }
        } catch (IOException e) {
            Log.w("TAG", "-- Error IOException");
            return null;
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- Error Out Of Memory");
            return null;
        }
    }

    public static Bitmap FixBitmapRotation(Bitmap myBitmap, String src) {
        try {
            ExifInterface exif = new ExifInterface(src);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(),
                    myBitmap.getHeight(), matrix, true); // rotating bitmap
            return myBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap FixOrientationFromExif(Bitmap myBitmap,
                                                String imagePath) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    Log.d("EXIF", "Exif: " + orientation);
                    matrix.postRotate(270);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    Log.d("EXIF", "Exif: " + orientation);
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    Log.d("EXIF", "Exif: " + orientation);
                    matrix.postRotate(90);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;
                    Log.d("EXIF", "Exif: " + orientation);
                    matrix.postRotate(0);
                    break;
                default:
                    break;
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(),
                    myBitmap.getHeight(), matrix, true); // rotating bitmap
            return myBitmap;
        } catch (IOException e) {
            Log.e("EXIF ORIENTATIONPROBLEM",
                    "Unable to get image exif orientation", e);
            return null;
        }
    }

    public static boolean isFilePortrait(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            if (exifOrientation == ExifInterface.ORIENTATION_NORMAL) {
                return true;
            }
            return false;
        } catch (IOException e) {
            Log.e("EXIF ORIENTATIONPROBLEM",
                    "Unable to get image exif orientation", e);
            return false;
        }
    }

    public static void DisableLayout(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                DisableLayout((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public static void EnableLayout(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                EnableLayout((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static void VisibleLayout(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                VisibleLayout((ViewGroup) child);
            } else {
                child.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void HideLayout(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                HideLayout((ViewGroup) child);
            } else {
                child.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("NewApi")
    public static int[] getScreenWidthAndHeight(Context context) {
        int columnWidth;
        int columnHeigh;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        try {
            if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
                display.getSize(point);
            } else {
                point.x = display.getWidth();
                point.y = display.getHeight();
            }

        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        columnHeigh = point.y;
        return new int[]{columnWidth, columnHeigh};
    }

    public static String EncodeParameter(String s) {
        if (s.length() > 0) {
            try {
                return (URLEncoder.encode(s, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else
            return s;
        return "";
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static boolean isNumeric(String s) {
        Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+$");
        if (pattern.matcher(s).matches()) {
            return true;
        }
        return false;
    }

    // @SuppressLint("NewApi")
    // public static String encodeToBase64(String string) {
    // String encodedString = "";
    // try {
    // byte[] byteData = null;
    // if (Build.VERSION.SDK_INT >= 8) // Build.VERSION_CODES.FROYO --> 8
    // {
    // byteData = android.util.Base64.encode(string.getBytes(),
    // android.util.Base64.DEFAULT);
    // } else {
    // byteData = Base64Utility.encode(string.getBytes(),
    // Base64Utility.DEFAULT);
    // }
    // encodedString = new String(byteData);
    // } catch (Exception e) {
    // }
    // return encodedString;
    // }

    public static Uri GetMapUri(String lat, String lng, String label,
                                String zoomValue) {
        String latitude = lat;
        String longitude = lng;
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=" + zoomValue;
        return Uri.parse(uriString);
    }

    public static File saveBitmap(Bitmap bitmap, String ext) {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString() + "/";

        String fileName = UUID.randomUUID().toString() + ext;
        File file = null;
        file = new File(extStorageDirectory, fileName);
        Log.e("file exist", "" + String.valueOf(file.exists()));
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            // write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;
    }

    public static String CutTextAfterSpecificChar(String text, int number) {
        if (text.length() >= 120) {
            String s = text.substring(0, number);
            return s;
        } else {
            return text;
        }
    }

    public static byte[] ConvertBitMapToByteArray(Bitmap b) {

        long bytes = getSizeInBytes(b);

        ByteBuffer buffer = ByteBuffer.allocate((int) bytes);
        b.copyPixelsToBuffer(buffer);

        byte[] array = buffer.array();
        return array;
    }

    public static long getSizeInBytes(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static void returnToFirstActivity(Context con) {
        Intent intent = new Intent(con, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", true);
        con.startActivity(intent);
    }

    public static String CutTextAfterSpecificNumberSpace(String text, int spaceNumber) {
        try {
            Integer[] intArr;
            if (text != null && text.length() > 0) {
                intArr = chekHowManyContainsSpace(text.toCharArray(),
                        spaceNumber);
                if (intArr[0] > spaceNumber) {
                    text = text.substring(0, intArr[1]) + " ...";
                    return text;
                } else {
                    text = text + " .";
                    return text;
                }
            } else
                return text;
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0
                && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be"
                    + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public static String GetYoutubeURLFromIFrame(String text) {
        String firstText = null, secondText = null, thirdText = null;
        try {
            int firstIndex = 0;
            if (text != null && text.length() > 0) {
                if (text.contains("src")) {
                    firstIndex = text.indexOf('"', text.indexOf("src"));
                    firstText = text.substring(0, firstIndex + 1);
                    secondText = text.substring(firstIndex + 1, text.length());
                    thirdText = secondText
                            .substring(0, secondText.indexOf('"'));
                    return thirdText;
                } else {
                    return text;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
        return thirdText;
    }

    public static Integer[] chekHowManyContainsSpace(char[] charArr, int spaceNumber) {
        int _countSpace = 0, indexOfSpecificSpace = 0, _counter = 0;
        try {

            for (int i = 0; i < charArr.length; i++) {
                if (charArr[i] == ' ')
                    _countSpace++;
            }
            for (int j = 0; j < charArr.length; j++) {
                if (charArr[j] == ' ') {
                    if (charArr[j + 1] != ' ') {
                        _counter++;
                    }
                    if (_counter == spaceNumber) {
                        indexOfSpecificSpace = j;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Integer[]{_countSpace, indexOfSpecificSpace};
    }

    public static String getPath(Uri uri, Activity act) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = act.getContentResolver().query(uri, projection, null,
                null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Upload Image To Specific Folder On Server
     *
     * @param fileName            image to upload
     * @param serverUri           web address Server Uri
     * @param serverFolderUploads specify folder on server to store image, you can pass it as
     *                            empty string
     * @return Saved path to our image file
     * @author A.Hegazy
     */
    public static String uploadImage(String fileName, String serverUri,
                                     String serverFolderUploads) {

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String pathToOurFile = fileName;
        String urlServer = serverUri;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String charset = "UTF-8";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            pathToOurFile = UUID.randomUUID().toString() + ".jpg";

            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\""
                    + serverFolderUploads + "\"" + ";  filename=\""
                    + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            try {
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            if (serverResponseCode == 200 && serverResponseMessage.equals("OK"))
                return pathToOurFile;
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Upload Image To Specific Folder On Server
     *
     * @param bitmap              image to upload
     * @param serverUri           web address Server Uri
     * @param serverFolderUploads specify folder on server to store image, you can pass it as
     *                            empty string
     * @return Saved path to our image file
     * @author A.Hegazy
     */
    public static String uploadImage(Bitmap bitmap, String serverUri,
                                     String serverFolderUploads) {

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String urlServer = serverUri;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String charset = "UTF-8";
        String videoName;
        byte[] buffer;
        try {

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            videoName = UUID.randomUUID().toString() + ".mp4";

            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\""
                    + serverFolderUploads + "\"" + ";  filename=\"" + videoName
                    + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            buffer = ConvertBitMapToByteArray(bitmap);
            try {
                outputStream.write(buffer, 0, (int) getSizeInBytes(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            outputStream.flush();
            outputStream.close();
            if (serverResponseCode == 200 && serverResponseMessage.equals("OK"))
                return videoName;
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Upload video To Specific Folder On Server
     *
     * @param fileName            image to upload
     * @param serverUri           web address Server Uri
     * @param serverFolderUploads specify folder on server to store video, you can pass it as
     *                            empty string
     * @return Saved path to our video file
     * @author A.Hegazy
     */
    public static String uploadVideo(String fileName, String serverUri,
                                     String serverFolderUploads) {

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String pathToOurFile = fileName;
        String urlServer = serverUri;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String charset = "UTF-8";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {
            File f = new File(pathToOurFile);
            FileInputStream fileInputStream = new FileInputStream(f);

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            pathToOurFile = UUID.randomUUID().toString() + ".mp4";

            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes(("Content-Disposition: form-data; name=\""
                    + serverFolderUploads + "\"" + ";  filename=\""
                    + pathToOurFile + "\"" + lineEnd));
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            // buffer = new byte[bufferSize];
            buffer = null;//FileUtils.readFileToByteArray(f); @#$^@%^@$%^@$%&@$%&@$&&&&&&^%%%%%%%@##############@@@@@@@@@@@@@@
            // ******************************************************************************************************
            /*
             * byte byt[]=new byte[bufferSize]; fileInputStream.read(byt);
             *
             * bytesRead = fileInputStream.read(buffer, 0, bufferSize);
             *
             * outputStream.write(buffer, 0, bufferSize);
             */
            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            try {
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
            } catch (Exception e) {
                Log.e("Uploading Video:::", e.getMessage());
                e.printStackTrace();
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            Log.e("serverResponseCode:::", String.valueOf(serverResponseCode));
            String serverResponseMessage = connection.getResponseMessage();
            Log.e("serverResponseMessage:", serverResponseMessage);
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            if (serverResponseCode == 200 && serverResponseMessage.equals("OK"))
                return pathToOurFile;
            else
                return "";
        } catch (Exception e) {
            Log.e("Uploading Video:::", e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap ResizeBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    public static void sendEmail(Activity act, String[] emailAddresses,
                                 String[] carbonCopies, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        String[] cc = carbonCopies;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        act.startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    public static void GetWordStartWithHashTags(String text) {
        if (text.length() > 0) {
            char[] charLst = text.toCharArray();
            for (int i = 0; i < charLst.length; i++) {
                if (charLst[i] == '#' || charLst[i] == '@') {
                    startIndex.add(i);
                }
            }
        }
    }

    public static void GetWordEndWithHashTags(String text,
                                              ArrayList<Integer> arrLst) {
        if (text.length() > 0 && arrLst.size() > 0) {
            char[] charLst = text.toCharArray();
            for (int j = 0; j < arrLst.size(); j++) {
                for (int i = arrLst.get(j); i < charLst.length; i++) {
                    if (charLst[i + 1] < charLst.length) {
                        if (charLst[i + 1] == '#' || charLst[i + 1] == '@'
                                || charLst[i + 1] == ' ') {
                            endIndex.add(i + 1);
                            break;
                        }
                    }
                }
            }
        }
    }


    public static String convertTimeFormat(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }


    public static String convert24TimeFormat(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }


    public static String convertDateFormat(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM", Locale.US);
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }


    public static String convertDateFormat1(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM", Locale.US);
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }


    public static String convertDateFormat2(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }


    public static String convertTimeFormat2(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static boolean isEarlyMorningTime(String currentTime) throws ParseException {

        String initialTime = "00:00:00";
        String finalTime = "05:59:00";

        Log.d("isEarlyMorningTime 1", ""+convertTimeFormat2(currentTime));

        Log.d("isEarlyMorningTime 111", ""+isTimeBetweenTwoTime(initialTime,
                finalTime, "12:05:00"));

        Log.d("isEarlyMorningTime 2", ""+isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime)));

        return isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime));

    }

    public static boolean isMorningTime(String currentTime) throws ParseException {

        String initialTime = "06:00:00";

        String finalTime = "11:59:00";

        Log.d("isMorningTime", ""+isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime)));

        return isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime));

    }

    public static boolean isAfternoonTime(String currentTime) throws ParseException {

        String initialTime = "12:01:00";
        String finalTime = "17:59:00";

        Log.d("isAfternoonTime", ""+isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime)));

        return isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime));
    }

    public static boolean isNightTime(String currentTime) throws ParseException {

        String initialTime = "18:00:00";
        String finalTime = "23:59:00";

        Log.d("isNightTime", ""+isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime)));

        return isTimeBetweenTwoTime(initialTime, finalTime, convertTimeFormat2(currentTime));
    }

    public static boolean checkTimeBetweenTwo(String initialTime, String finalTime, String currentTime) throws ParseException {

        boolean valid=false;
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

        if (initialTime.matches(reg) && finalTime.matches(reg) && convertTimeFormat2(currentTime).matches(reg)) {
            //Start Time
            Date inTime = new SimpleDateFormat("HH:mm:ss", Locale.US).parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            Date checkTime = new SimpleDateFormat("HH:mm:ss", Locale.US).parse(convertTimeFormat2(currentTime));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            Date finTime = new SimpleDateFormat("HH:mm:ss", Locale.US).parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }

            if(checkTime.before(calendar1.getTime())){
                Log.d("isEarlyMorningTime 3", "true");
            }

            if(checkTime.after(calendar2.getTime())){
                Log.d("isEarlyMorningTime 4", "true");
            }

            if(checkTime.before(inTime) && checkTime.after(finTime)){
                Log.d("isEarlyMorningTime 5", "true");
            }

            Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0) && actualTime.before(calendar2.getTime())) {
                valid = true;
                return valid;
            } else {
                throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
            }
        }

        return valid;

    }


//    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime) throws ParseException {
//        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
//        boolean valid = false;
//
//        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg))
//        {
//
//            //Start Time
//            java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
//            Calendar calendar1 = Calendar.getInstance();
//            calendar1.setTime(inTime);
//
//            //Current Time
//            java.util.Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
//            Calendar calendar3 = Calendar.getInstance();
//            calendar3.setTime(checkTime);
//
//            //End Time
//            java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(finTime);
//
//            if (finalTime.compareTo(initialTime) < 0)
//            {
//                calendar2.add(Calendar.DATE, 1);
//                calendar3.add(Calendar.DATE, 1);
//            }
//
//            java.util.Date actualTime = calendar3.getTime();
//            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0) && actualTime.before(calendar2.getTime()))
//            {
//                Log.d("isTimeBetweenTwoTime ", "true");
//                valid = true;
//                return valid;
//            } else {
//                throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
//            }
//        }
//
//        return valid;
//
//    }

    public static boolean isTimeBetweenTwoTime(String argStartTime,
                                               String argEndTime, String argCurrentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        //
        if (argStartTime.matches(reg) && argEndTime.matches(reg)
                && argCurrentTime.matches(reg)) {
            boolean valid = false;
            // Start Time
            Date startTime = new SimpleDateFormat("HH:mm:ss", Locale.US)
                    .parse(argStartTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);

            // Current Time
            Date currentTime = new SimpleDateFormat("HH:mm:ss", Locale.US)
                    .parse(argCurrentTime);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentTime);

            // End Time
            Date endTime = new SimpleDateFormat("HH:mm:ss", Locale.US)
                    .parse(argEndTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);

            //
            if (currentTime.compareTo(endTime) < 0) {

                currentCalendar.add(Calendar.DATE, 1);
                currentTime = currentCalendar.getTime();

            }

            if (startTime.compareTo(endTime) < 0) {

                startCalendar.add(Calendar.DATE, 1);
                startTime = startCalendar.getTime();

            }
            //
            if (currentTime.before(startTime)) {

                System.out.println(" Time is Lesser ");

                valid = false;
            } else {

                if (currentTime.after(endTime)) {
                    endCalendar.add(Calendar.DATE, 1);
                    endTime = endCalendar.getTime();

                }

                System.out.println("Comparing , Start Time /n " + startTime);
                System.out.println("Comparing , End Time /n " + endTime);
                System.out
                        .println("Comparing , Current Time /n " + currentTime);

                if (currentTime.before(endTime)) {
                    System.out.println("RESULT, Time lies b/w");
                    valid = true;
                } else {
                    valid = false;
                    System.out.println("RESULT, Time does not lies b/w");
                }

            }
            return valid;

        } else {
            throw new IllegalArgumentException(
                    "Not a valid time, expecting HH:MM:SS format");
        }

    }


    //We are calling this method to check the permission status
    public static boolean isReadCallAllowed(FragmentActivity act) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    public static void requestCallPermission(FragmentActivity act){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CALL_PHONE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
    }

    //We are calling this method to check the permission status
    public static boolean isWriteExternalStorageAllowed(FragmentActivity act) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    public static void requestWriteExternalStoragePermission(FragmentActivity act){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CODE);
    }

    //We are calling this method to check the permission status
    public static boolean isReadStorageAllowed(FragmentActivity act) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    public static void requestStoragePermission(FragmentActivity act){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_CODE);
    }

    //We are calling this method to check the permission status
    public static boolean isGPSAllowed(FragmentActivity act) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    public static void requestGPSPermission(FragmentActivity act){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);
    }

    public static boolean isWriteStorageAllowed(FragmentActivity act) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    public static void requestWritePermission(FragmentActivity act){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CODE);
    }


    //We are calling this method to check the permission status
    public static boolean isCameraAllowed(FragmentActivity act) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //Requesting permission
    public static void requestCameraePermission(FragmentActivity act){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CAMERA)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},PERMISSION_CODE);
    }


    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


    public static String getLayOverTime(String fromDate, String toDate){

        Log.d("fromDate", ""+fromDate);

        Log.d("toDate", ""+toDate);

        String layOver = "";

        try {
            //Dates to compare
            String CurrentDate=  fromDate;
            String FinalDate=  toDate;

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);

            int numOfDays = (int) (difference / (1000 * 60 * 60 * 24));
//            int hours = (int) (difference / (1000 * 60 * 60));
//            int minutes = (int) (difference / (1000 * 60));
            int seconds = (int) (difference / (1000));

            int day = (int) TimeUnit.SECONDS.toDays(seconds);
            long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
            long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
            long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

            if(numOfDays>0){

                layOver = numOfDays + "d";

            }

            if(hours>0){

                layOver = layOver.length()>0 ? (layOver + " " + hours + "h") : (hours + "h");

            }

            if(minutes>0){

                layOver = layOver.length()>0 ? (layOver + " " + minutes + "m") : (minutes + "m");

            }

            Log.e("HERE","HERE: " + dayDifference);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }

        return layOver;

    }


    public static boolean isNumericString(String input) {
        boolean result = false;

        if(input != null && input.length() > 0) {
            char[] charArray = input.toCharArray();

            for(char c : charArray) {
                if(c >= '0' && c <= '9') {
                    // it is a digit
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }


    public static boolean checkMobileFormat(String mobile){

        boolean isError=false;

        int countPlus = mobile.split("\\+", -1).length - 1;

        int countMinus = mobile.split("\\-", -1).length - 1;

        Log.d("countPlus", ""+countPlus);

        Log.d("countMinus", ""+countMinus);

        if(countMinus == 0 || countMinus>2){

            isError = true;

        }

        if(countPlus == 0 || countPlus>1){

            isError = true;

        }

        return  isError;

    }


    public static void showErrorMessage(ResponseBody response, View mainLayout){

        if(response != null){

            if(response.byteStream() != null){

//                TypedInput body = response.getBody();

                String outResponse = "";

                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.byteStream()));

                    StringBuilder out = new StringBuilder();

                    String newLine = System.getProperty("line.separator");

                    String line;

                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                        out.append(newLine);
                    }

                    outResponse = out.toString();

                    Log.d("outResponse", "" + outResponse);


                } catch (Exception ex) {

                    ex.printStackTrace();

                }

                if(outResponse != null){

                    JSONObject jsonObject = null;

                    try {

                        jsonObject = new JSONObject(outResponse);

                        if(jsonObject.has("errors")) {

                            outResponse = jsonObject.getString("errors").replaceAll("\"", "");

                            if(outResponse.split(",").length>0){

                                if(outResponse.split(",")[0].split(":").length>1){

                                    outResponse = outResponse.split(",")[0].split(":")[1].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\}", "");

                                    Log.d("outResponse", "3 " + outResponse);

                                    Snackbar.make(mainLayout, outResponse, Snackbar.LENGTH_LONG).show();

                                }

                            }

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                }

            }

        }

    }


    public static String convertDateToString(String date){

        String date1="";

        SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        dateFormatter1.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa", Locale.ENGLISH);

        int index = date.lastIndexOf('/');

        try {

            date1 = dateFormatter2.format(dateFormatter1.parse(date.substring(index + 1)));

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return date1;

    }


    public static String convertDateToString1(String date){

        String date1="";

        SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        dateFormatter1.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH);

        int index = date.lastIndexOf('/');

        try {

            date1 = dateFormatter2.format(dateFormatter1.parse(date.substring(index + 1)));

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return date1;

    }


    //function to get the device id
    public static String getIMEI(Context context) {

        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }


    public static void setViewAttributes(View view, int width, int height, int marginLeftDp,
                                         int marginTopDp, int marginRightDp, int marginBottomDp) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                width,
                height);

        params.setMargins(convertDpToPixel(marginLeftDp),
                convertDpToPixel(marginTopDp),
                convertDpToPixel(marginRightDp), convertDpToPixel(marginBottomDp)
        );

        view.setLayoutParams(params);

    }


    public static void setEditTextAttributes(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(12),
                0,
                0, 0
        );

        view.setLayoutParams(params);
    }


    public static void setTitleAttributes(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(8),
                convertDpToPixel(8),
                0, 0
        );

        view.setLayoutParams(params);
    }


    //This function to convert DPs to pixels
    private static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


    public static void addLineSeperator(Context act, LinearLayout linearLayout) {
        LinearLayout lineLayout = new LinearLayout(act);
        lineLayout.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2);
        params.setMargins(0, convertDpToPixel(10), 0, convertDpToPixel(10));
        lineLayout.setLayoutParams(params);
        linearLayout.addView(lineLayout);
    }


    public static String getPathFromURI(Uri contentUri, Context act) {

        String res = null;

        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = act.getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor.moveToFirst()) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            res = cursor.getString(column_index);

        }

        cursor.close();

        return res;

    }


    public static void makeCall(String mobile, FragmentActivity context){

        if(mobile.length()>0){

            if (!GlobalFunctions.isReadCallAllowed(context)) {

                GlobalFunctions.requestCallPermission(context);

            } else {

                Intent intent = new Intent("android.intent.action.CALL");

                intent.setData(Uri.parse("tel:"+mobile));

                context.startActivity(intent);

                return;

            }

        }

    }


    public static String getSubDate(String date){

        String value = "";

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        SimpleDateFormat formatter1 = new SimpleDateFormat("EEE, dd MMMM", Locale.US);

        try {
            value = formatter1.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return value;

    }


    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {

        long timeDiff = endDate.getTime() - startDate.getTime();

        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);

    }



}
