package com.customer.orderproupdated;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;


import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.utility.MySSLSocketFactory;
import com.customer.orderproupdated.database.DatabaseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadFileAsyn extends AsyncTask<String, String, String> {

    DatabaseHandler db;
    String responseString = "";
    Context ctx;
    ChatBody chatBody;
    String mScreen,broadcastkey;
    boolean isbrodcasted = false;
    //............................

    String filePath = "";
    int count = 0;
    int lenghtOfFile = 0;
    String mediaDirectory = PreferenceConstant.rootDirectory + File.separator + PreferenceConstant.subRootMediaDirectory + File.separator;




    public DownloadFileAsyn()
    {

    }

    public DownloadFileAsyn(Context context, ChatBody chatBody,String mScreen) {

        this.ctx = context;
        this.chatBody = chatBody;
        this.mScreen = mScreen;
        db = new DatabaseHandler(context);

    }

    @Override
    protected String doInBackground(String... params) {

        responseString = Download(chatBody);
        return responseString;
    }

    public String  Download(ChatBody chatBody) {

        try {

            String type = chatBody.getMessageType();
            String downloadUrl = chatBody.getUrl();




            if (type.contains(PreferenceConstant.IMAGE)) {
                filePath = getFilename();
            }



            URL u = new URL(downloadUrl);
            HttpURLConnection c = MySSLSocketFactory.getHttpUrlConnection(u);
            //c.connect();


            FileOutputStream f = new FileOutputStream(new File(filePath));
            InputStream in;


            //HttpURLConnection c = (HttpURLConnection) u.openConnection();

            c.setUseCaches(false);

            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            lenghtOfFile = c.getContentLength();


            in = c.getInputStream();


            byte[] buffer = new byte[1024];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0) {
                count += len1;
                f.write(buffer, 0, len1);

            }

            try {


                chatBody.setStatus(PreferenceConstant.fileDownloaded);
                chatBody.setLocalUri(filePath);
                chatBody.setProgress("100");
               db.updateFileInfo(chatBody);

                db.updateFileInfoFileUploaded(chatBody);

                // chatWindow.fileDownloaded(chatBody);

            }catch (Exception e)
            {
                uploadingFailed();

            }


            f.flush();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
            uploadingFailed();

        }

        return  responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


//        Intent intent = new Intent(RoosterConnectionService.FILE_DOWNLOAD_RECEIVER);
//        intent.putExtra(RoosterConnectionService.BUNDLE_FROM_JID, chatBody.getFriendJID());
//        intent.putExtra("CHAT", chatBody);
//
//        ctx.sendBroadcast(intent);



    }


 public void uploadingFile() {
        chatBody.setProgress("0");
        chatBody.setUrl("");
        chatBody.setLocalUri(chatBody.getUrl());
        chatBody.setStatus(PreferenceConstant.fileUploding);
        db.updateFileInfo(chatBody);
    }

    public void uploadingFailed() {
        chatBody.setProgress("0");
        chatBody.setUrl("");
        chatBody.setLocalUri(chatBody.getUrl());
        chatBody.setStatus(PreferenceConstant.fileFail);
        db.updateFileInfo(chatBody);
    }



    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), mediaDirectory + PreferenceConstant.dirImages);
        if (!file.exists()) {
            file.mkdirs();
        }

        String uriSting = (file.getPath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }


}



