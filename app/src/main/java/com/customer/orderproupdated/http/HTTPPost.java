/*
package com.customer.orderproupdated.http;


import android.content.Context;
import android.os.AsyncTask;


import com.customer.orderproupdated.Fragments.DashBoardFragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
*/
/**//*


import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class HTTPPost extends AsyncTask<String, String, String> {

    private Context context;
    private String apiresponse;
    private String whichActivity;
    private String groupJID;
    private List<NameValuePair> nameValuePairs;


    DashBoardFragment mDashBoardFragment;


    public HTTPPost(DashBoardFragment mDashBoardFragment, List<NameValuePair> nameValuePairs) {
        context = mDashBoardFragment.getActivity();
        this.mDashBoardFragment = mDashBoardFragment;
        this.nameValuePairs = nameValuePairs;
    }


    @Override
    protected String doInBackground(String... uri) {
        String responseString = "";
        whichActivity = uri[1];
        responseString = callHttpRequest(uri[0]);
        return responseString;
    }

    public String callHttpRequest(String url1) {
        try {
            URL url = new URL(url1);
            HttpsURLConnection conn = MySSLSocketFactory.getHttpUrlConnection(url);
            conn.setReadTimeout(60 * 1000);
            conn.setConnectTimeout(60 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //  System.setProperty("http.keepAlive", "false");
            conn.setRequestProperty("connection", "close");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(nameValuePairs));
            writer.flush();
            String line;

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                apiresponse = line;
            }
            writer.close();
            reader.close();
            os.close();
            conn.connect();
        } catch (MalformedURLException e) {
            apiresponse = "fail";
            e.printStackTrace();
        } catch (ProtocolException e) {
            apiresponse = "fail";
            e.printStackTrace();
        } catch (IOException e) {
            apiresponse = "fail";
            e.printStackTrace();
        }
        return apiresponse;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (NameValuePair pair : params) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

            }

            return result.toString();
        } catch (Exception e) {
        }
        return result.toString();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (whichActivity.equals(PreferenceConstant.PRODUCT_API_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals("1")) {
                        parser.getCategeoryLst(context);
                       //mDashBoardFragment.receiveResponse("Success");
                    }
                }else
                {
                    DialogUtility.pauseProgressDialog();
                  //  mDashBoardFragment.receiveResponse(context.getString(R.string.request_fail));

                }


            } catch (Exception e) {
                e.printStackTrace();
               // DialogUtility.showToast(context,"Loading Failed");
            }
        }

       */
/* if (whichActivity.equals(PreferenceConstants.GROUPLIST_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.REQUEST_STATUS)) {
                        parser.getGroupLst(context);
                    }
                }
                verification.receiveResponse("Success");

            } catch (Exception e) {
                e.printStackTrace();
                verification.receiveResponse("Success");

            }
        }
         else if (whichActivity.equals(PreferenceConstants.VERIFICATION_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        verification.receiveResponse(parser.jObj);
                    } else {
                        verification.receiveResponse(parser.MESSAGE);
                    }
                }else
                {
                    verification.receiveResponse(context.getString(R.string.request_fail));
                }

            } catch (Exception e) {
                verification.receiveResponse(context.getString(R.string.request_fail));
            }
        } else if (whichActivity.equals(PreferenceConstants.EXIT_FROM_GROUP)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);

                    if (parser.STATUS.equals(PreferenceConstants.REQUEST_STATUS)) {
                        groupDetail.deleteMemberResponse(parser.RESULT, parser.STATUS);
                    } else {
                        groupDetail.receiveResponse(parser.RESULT);
                    }
                }else
                {
                    groupDetail.receiveResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                groupDetail.receiveResponse(context.getString(R.string.request_fail));
            }

        } else if (whichActivity.equals(PreferenceConstants.GET_GROUP_DETAIL)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.REQUEST_STATUS)) {
                        parser.getGroupDetail(context);
                        groupDetail.updateView();
                    } else {
                        groupDetail.receiveResponse(parser.MESSAGE);
                    }

                }else
                {
                    groupDetail.receiveResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                groupDetail.receiveResponse(context.getString(R.string.request_fail));
            }
        } else if (whichActivity.equals(PreferenceConstants.GET_GROUP_DETAIL_MAIN)) {
            try {
                if(!apiresponse.equals("fail")) {
                    JSONParser parser = new JSONParser(context, result);
                    parser.getGroupDetail(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (whichActivity.equals(PreferenceConstants.ADD_MEMBER)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    addParticipants.addGroupMemberResponse(parser.MESSAGE, parser.STATUS);

                }else
                {
                    addParticipants.addGroupMemberResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                e.printStackTrace();
                addParticipants.addGroupMemberResponse(context.getString(R.string.request_fail));

            }
        }
        else if (whichActivity.equals(PreferenceConstants.GET_KEY_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        mChatWindow.receiveResponse(parser.jObj);
                    } else {
                        mChatWindow.receiveResponse(parser.MESSAGE);
                    }
                }else
                {
                    mChatWindow.receiveResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                e.printStackTrace();
                mChatWindow.receiveResponse(context.getString(R.string.request_fail));

            }
        } else if (whichActivity.equals(PreferenceConstants.SET_KEY_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {

                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        mChatWindow.receiveSetResponse(parser.jObj);
                    } else {
                        mChatWindow.receiveResponse(parser.MESSAGE);
                    }

                }else
                {
                    mChatWindow.receiveResponse(context.getString(R.string.request_fail));

                }
            } catch (Exception e) {
                e.printStackTrace();
                mChatWindow.receiveResponse(context.getString(R.string.request_fail));
            }
        } else if (whichActivity.equals(PreferenceConstants.REGISTER_PROFILE_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        mSetProfile.receiveResponse();
                    } else {
                        mSetProfile.receiveResponse(parser.MESSAGE);
                    }
                }else
                {
                    mSetProfile.receiveResponse(context.getString(R.string.request_fail));
                }

            } catch (Exception e) {
                e.printStackTrace();
                mSetProfile.receiveResponse(context.getString(R.string.request_fail));
            }
        }else if (whichActivity.equals(PreferenceConstants.CREATE_VOIP_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {

                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.REQUEST_STATUS)) {
                        mSetProfile.receiveVoipResponse(parser.MESSAGE);
                    } else {
                        mSetProfile.receiveResponse(parser.MESSAGE);
                    }

                }else
                {
                    mSetProfile.receiveResponse(context.getString(R.string.request_fail));

                }
            } catch (Exception e) {
                e.printStackTrace();
                mSetProfile.receiveResponse(context.getString(R.string.request_fail));

            }
        }
        else if (whichActivity.equals(PreferenceConstants.USER_ACTIVATION_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        mActivation.receiveResponse(parser.jObj);
                    } else {
                        mActivation.receiveResponse(parser.MESSAGE);
                    }

                }else
                {
                    mActivation.receiveResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                e.printStackTrace();
                mActivation.receiveResponse(context.getString(R.string.request_fail));

            }
        } else if (whichActivity.equals(PreferenceConstants.CHANGE_PASSPHRASE_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        mChangePassPhraseFragment.changePassphraseResponse();
                    } else {
                        mChangePassPhraseFragment.changePassphraseResponse(parser.MESSAGE);
                    }
                }else
                {
                    mChangePassPhraseFragment.changePassphraseResponse(context.getString(R.string.request_fail));

                }
            } catch (Exception e) {
                e.printStackTrace();

                mChangePassPhraseFragment.changePassphraseResponse(context.getString(R.string.request_fail));
            }
        }else if (whichActivity.equals(PreferenceConstants.RESONCE_ADDMEMBER_BROADCAST)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    mAddbroadcastContact.addGroupMemberResponse(parser.MESSAGE, parser.STATUS);
                }else
                {
                    mAddbroadcastContact.addGroupMemberResponse(context.getString(R.string.request_fail));
                }

            } catch (Exception e) {
                e.printStackTrace();
                mAddbroadcastContact.addGroupMemberResponse(context.getString(R.string.request_fail));

            }
        }
        else if (whichActivity.equals(PreferenceConstants.CREATE_BROADCAST_RESONCE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    mAddbroadcastContact.CreateBroadcastResponse(parser.MESSAGE, result);
                }else
                {
                    mAddbroadcastContact.addGroupMemberResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                e.printStackTrace();
                mAddbroadcastContact.addGroupMemberResponse(context.getString(R.string.request_fail));

            }
        }
        else if (whichActivity.equals(PreferenceConstants.RESONCE_DELETEMEMBER_BROADCAST)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    mBroadcastDetailList.deleteMemberResponse(parser.MESSAGE, parser.STATUS);
                }else
                {
                    mBroadcastDetailList.deleteMemberResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                e.printStackTrace();
                mBroadcastDetailList.deleteMemberResponse(context.getString(R.string.request_fail));
            }
        }else if (whichActivity.equals(PreferenceConstants.FORWARD_GET_KEY_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        forwardMessageToParticipants.receiveResponse(parser.jObj, chatBody);
                    } else {
                        forwardMessageToParticipants.receiveResponse(parser.MESSAGE);
                    }
                }else
                {
                    forwardMessageToParticipants.receiveResponse(context.getString(R.string.request_fail));
                }
            } catch (Exception e) {
                e.printStackTrace();
                forwardMessageToParticipants.receiveResponse(context.getString(R.string.request_fail));

            }
        } else if (whichActivity.equals(PreferenceConstants.FORWARD_SET_KEY_RESPONSE)) {
            try {
                if(!apiresponse.equals("fail"))
                {
                    JSONParser parser = new JSONParser(context, result);
                    if (parser.STATUS.equals(PreferenceConstants.PASS_STATUS)) {
                        forwardMessageToParticipants.receiveSetResponse(parser.jObj, chatBody);
                    } else {
                        forwardMessageToParticipants.receiveResponse(parser.MESSAGE);
                    }
                }else
                {
                    forwardMessageToParticipants.receiveResponse(context.getString(R.string.request_fail));

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*//*

    }
}

*/
