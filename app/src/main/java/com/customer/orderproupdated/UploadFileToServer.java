package com.customer.orderproupdated;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;
import com.customer.orderproupdated.database.DatabaseHandler;
import com.customer.orderproupdated.http.NullHostNameVerifier;
import com.customer.orderproupdated.http.NullX509TrustManager;
import com.customer.orderproupdated.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class UploadFileToServer extends AsyncTask<String, String, String> {

    DatabaseHandler db;

    String responseString = "";

    public static Context ctx;
    File chatFile = null;
    ChatBody chatBody;
    String ext_;
    public UploadFileToServer() {

    }

    public UploadFileToServer(Context context, ChatBody chatBody) {

        this.ctx = context;
        this.chatBody = chatBody;
        db = new DatabaseHandler(ctx);
    }

    @Override
    protected String doInBackground(String... params) {

        responseString = uploadFile(chatBody.getLocalUri());

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Intent intent = new Intent(RoosterConnectionService.SEND_MESSAGE);
        intent.putExtra("CHAT", chatBody);
        ctx.sendBroadcast(intent);

    }


    @SuppressLint("TrulyRandom")
    private javax.net.ssl.SSLSocketFactory createSslSocketFactory() throws Exception {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    @SuppressWarnings({"unused", "deprecation"})
    public String uploadFile(String sourceFileUri) {

        try {

            File actualFile = new File(sourceFileUri);

            if (actualFile.exists()) {
                ext_ = getFileExtension(actualFile);
                chatFile = getFilename(ext_);
                copy(actualFile, chatFile);

                uploadingFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            uploadingFailed();
        }


        try {

            if (chatBody.getMessageType().equals(PreferenceConstant.IMAGE))
            {
                sourceFileUri = compressImage(sourceFileUri, ext_);
                chatBody.setLocalUri(sourceFileUri);
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }

        DataInputStream inStream;
        String fileName = sourceFileUri;
       // HttpsURLConnection conn = null;
        HttpURLConnection httpConn=null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        // int maxBufferSize = 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            uploadingFailed();
            return "Fail";
        } else {
            try {
                HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(PreferenceConstant.FILE_TRANSFER);

                // Open a HTTP connection to the URL
                 httpConn = (HttpURLConnection) url.openConnection();

               // conn = (HttpsURLConnection) url.openConnection();

                httpConn.setDoInput(true); // Allow Inputs
                httpConn.setDoOutput(true); // Allow Outputs
                httpConn.setUseCaches(false); // Don't use a Cached Copy
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("Connection", "Keep-Alive");
                httpConn.setRequestProperty("ENCTYPE", "multipart/form-data");
                httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                httpConn.setRequestProperty("file", fileName);
                dos = new DataOutputStream(httpConn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=file;filename=" + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                int downloadedSize = 0;


                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);

                    downloadedSize += bufferSize;
                    // publishProgress((int)(downloadedSize * 100 /
                    // fileSize));
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                fileInputStream.close();
                dos.flush();
                dos.close();

                // Responses from the server (code and message)
                // ------------------ read the SERVER RESPONSE
                try {
                    inStream = new DataInputStream(httpConn.getInputStream());
                    responseString = inStream.readLine();
                    inStream.close();
                    JSONParser parser = new JSONParser(ctx, responseString);
                    responseString = parser.URL;
                    String status = parser.STATUS;
                    if(status.equals("1"))
                    {
                        chatBody.setProgress("100");
                        chatBody.setStatus(PreferenceConstant.SENT_LATER); //PreferenceConstant.fileUploded
                        chatBody.setUrl(responseString);
                      // chatBody.setLocalUri(chatFile.getAbsolutePath());

                        db.updateFileInfo(chatBody);

                        chatBody.setStatus(PreferenceConstant.fileUploded);
                        db.updateFileInfoFileUploaded(chatBody);
                    }
                    else
                    {
                        uploadingFailed();
                    }

                } catch (Exception ioex) {
                    Log.e("Debug", "error: " + ioex.getMessage(), ioex);
                    uploadingFailed();
                }
                // close the streams //

            } catch (Exception e) {
                e.printStackTrace();
                uploadingFailed();
            }

            return responseString;
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public File getFilename(String ext) {

        String filePath = PreferenceConstant.rootDirectory + File.separator +
                PreferenceConstant.subRootMediaDirectory + File.separator;
        String fileName = "";

        if (chatBody.getMessageType().equals(PreferenceConstant.IMAGE)){
            filePath = filePath + PreferenceConstant.dirImages + File.separator + PreferenceConstant.dirSent;
            fileName = "IMG_" + System.currentTimeMillis();
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath(), filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        return new File(file.getPath() + File.separator + fileName + "." + ext);
    }

    public static String getFileExtension(File file) {

        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public void uploadingFile() {
        chatBody.setProgress("0");
        chatBody.setUrl("");
//        chatBody.setLocalUri(chatFile.getAbsolutePath());
        chatBody.setStatus(PreferenceConstant.fileUploding);
        db.updateFileInfo(chatBody);
    }

    public void uploadingFailed() {
        chatBody.setProgress("0");
        chatBody.setUrl("");
//        chatBody.setLocalUri(chatFile.getAbsolutePath());
        chatBody.setStatus(PreferenceConstant.fileFail);
        db.updateFileInfo(chatBody);
    }


    public static String compressImage(String imageUri,String ext) {

        String filename = "";

        try {
            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;

            if (actualHeight == 0) {
                actualHeight = 500;
            }

            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {

            }
            FileOutputStream out = null;

            filename = getcompressFilename(ext,"image");
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            } catch (FileNotFoundException e) {

            }
        } catch (Exception e) {


            filename = imageUri;
        }

        return filename;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public static String getcompressFilename(String ext,String type) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "OrderPro" + "/"+type);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName  = type + System.currentTimeMillis();
        String uriSting = (file.getAbsolutePath() + "/" + fileName+"."+ext);
        return uriSting;

    }
    private static String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = ctx.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }





}
