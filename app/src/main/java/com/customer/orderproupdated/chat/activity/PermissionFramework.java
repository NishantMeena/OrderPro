package com.customer.orderproupdated.chat.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.customer.orderproupdated.Utility.SharedPrefrence;

import java.util.ArrayList;

/**
 * Created by Advantal on 07-02-2017.
 */
public class PermissionFramework extends Activity {

    String pName = "", pGroup = "";

    public static final String PERMISSION_NAME = "PERMISSION_NAME";
    public static final String PERMISSION_GROUP = "PERMISSION_GROUP";

    boolean PERMISSION_CALENDAR = false;
    boolean PERMISSION_CAMERA = false;
    boolean PERMISSION_CONTACTS = false;
    boolean PERMISSION_LOCATION = false;
    boolean PERMISSION_MICROPHONE = false;
    boolean PERMISSION_PHONE = false;
    boolean PERMISSION_SENSORS = false;
    boolean PERMISSION_SMS = false;
    boolean PERMISSION_STORAGE = false;

    public static final String PG_CALENDAR = "PG_CALENDAR";
    public static final String PG_CAMERA = "PG_CAMERA";
    public static final String PG_CONTACTS = "PG_CONTACTS";
    public static final String PG_LOCATION = "PG_LOCATION";
    public static final String PG_MICROPHONE = "PG_MICROPHONE";
    public static final String PG_PHONE = "PG_PHONE";
    public static final String PG_SENSORS = "PG_SENSORS";
    public static final String PG_SMS = "PG_SMS";
    public static final String PG_STORAGE = "PG_STORAGE";

    public static final String READ_CALENDAR = "READ_CALENDAR";
    public static final String WRITE_CALENDAR = "WRITE_CALENDAR";

    public static final String CAMERA = "CAMERA";

    public static final String READ_CONTACTS = "READ_CONTACTS";
    public static final String WRITE_CONTACTS = "WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "ACCESS_COARSE_LOCATION";

    public static final String RECORD_AUDIO = "RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "READ_PHONE_STATE";
    public static final String CALL_PHONE = "CALL_PHONE";
    public static final String READ_CALL_LOG = "READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "WRITE_CALL_LOG";
    public static final String USE_SIP = "USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = "PROCESS_OUTGOING_CALLS";

    public static final String BODY_SENSORS = "BODY_SENSORS";

    public static final String SEND_SMS = "SEND_SMS";
    public static final String RECEIVE_SMS = "RECEIVE_SMS";
    public static final String READ_SMS = "READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "RECEIVE_MMS";

    public static final String READ_EXTERNAL_STORAGE = "READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "WRITE_EXTERNAL_STORAGE";


    /**//**//**//**//**//**//**/
    public static final int PERMISSION_REQUEST_CODE = 1501;

    public static final String PERMISSION_RESULT = "Permission_Result";
    public static final String PERMISSION_SUCCESS = "Success";
    public static final String PERMISSION_FAIL = "Fail";

    private String permissionName;
    private String permissionGroup;

    private SharedPrefrence sharedPrefrence;

    public void haveAPermissionName(String permissionName) {

        this.permissionName = permissionName;

// CALENDAR
        if (permissionName.equals(READ_CALENDAR) ||
                permissionName.equals(WRITE_CALENDAR)) {
            PERMISSION_CALENDAR = true;
        } else
// CAMERA
            if (permissionName.equals(CAMERA)) {
                PERMISSION_CAMERA = true;
            } else
// CONTACTS
                if (permissionName.equals(READ_CONTACTS) ||
                        permissionName.equals(WRITE_CONTACTS) ||
                        permissionName.equals(GET_ACCOUNTS)) {
                    PERMISSION_CONTACTS = true;
                } else
// LOCATION
                    if (permissionName.equals(ACCESS_FINE_LOCATION) ||
                            permissionName.equals(ACCESS_COARSE_LOCATION)) {
                        PERMISSION_LOCATION = true;
                    } else
// MICROPHONE
                        if (permissionName.equals(RECORD_AUDIO)) {
                            PERMISSION_MICROPHONE = true;
                        } else
// PHONE
                            if (permissionName.equals(READ_PHONE_STATE) ||
                                    permissionName.equals(CALL_PHONE) ||
                                    permissionName.equals(READ_CALL_LOG) ||
                                    permissionName.equals(WRITE_CALL_LOG) ||
                                    permissionName.equals(USE_SIP) ||
                                    permissionName.equals(PROCESS_OUTGOING_CALLS)) {
                                PERMISSION_PHONE = true;
                            } else
// SENSORS
                                if (permissionName.equals(BODY_SENSORS)) {
                                    PERMISSION_SENSORS = true;
                                } else
// SMS
                                    if (permissionName.equals(SEND_SMS) ||
                                            permissionName.equals(RECEIVE_SMS) ||
                                            permissionName.equals(READ_SMS) ||
                                            permissionName.equals(RECEIVE_WAP_PUSH) ||
                                            permissionName.equals(RECEIVE_MMS)) {
                                        PERMISSION_SMS = true;
                                    } else
// STORAGE
                                        if (permissionName.equals(READ_EXTERNAL_STORAGE) ||
                                                permissionName.equals(WRITE_EXTERNAL_STORAGE)) {
                                            PERMISSION_STORAGE = true;
                                        }
    }

    public void haveAPermissionGroup(String permissionGroup) {

        this.permissionGroup = permissionGroup;

        switch (permissionGroup) {
            case PG_CALENDAR:
                PERMISSION_CALENDAR = true;
                break;

            case PG_CAMERA:
                PERMISSION_CAMERA = true;
                break;

            case PG_CONTACTS:
                PERMISSION_CONTACTS = true;
                break;

            case PG_LOCATION:
                PERMISSION_LOCATION = true;
                break;

            case PG_MICROPHONE:
                PERMISSION_MICROPHONE = true;
                break;

            case PG_PHONE:
                PERMISSION_PHONE = true;
                break;

            case PG_SENSORS:
                PERMISSION_SENSORS = true;
                break;

            case PG_SMS:
                PERMISSION_SMS = true;
                break;

            case PG_STORAGE:
                PERMISSION_STORAGE = true;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        sharedPrefrence = SharedPrefrence.getInstance(PermissionFramework.this);

        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {

                pName = b.getString(PermissionFramework.PERMISSION_NAME, "");
                pGroup = b.getString(PermissionFramework.PERMISSION_GROUP, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (pName.length() > 0) {
                    haveAPermissionName(pName);
                } else if (pGroup.length() > 0) {
                    haveAPermissionGroup(pGroup);
                }

                checkPermission();
                requestPermission();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

//            Toast.makeText(PermissionFramework.this, "Permission not required", Toast.LENGTH_SHORT).show();

            sentResult();
        }
    }

    public void sentResult() {

        Intent intent = new Intent();

        intent.putExtra(PermissionFramework.PERMISSION_NAME, pName);
        intent.putExtra(PermissionFramework.PERMISSION_GROUP, pGroup);

        intent.putExtra(PermissionFramework.PERMISSION_RESULT, PermissionFramework.PERMISSION_SUCCESS);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void sentFailResult() {

        Intent intent = new Intent();

        intent.putExtra(PermissionFramework.PERMISSION_NAME, pName);
        intent.putExtra(PermissionFramework.PERMISSION_GROUP, pGroup);

        intent.putExtra(PermissionFramework.PERMISSION_RESULT, PermissionFramework.PERMISSION_FAIL);

        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean checkPermission() {

        // CALENDAR
        int READ_CALENDAR = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.READ_CALENDAR);
        int WRITE_CALENDAR = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.WRITE_CALENDAR);

        // CAMERA
        int CAMERA = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.CAMERA);

        // CONTACTS
        int READ_CONTACTS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.READ_CONTACTS);
        int WRITE_CONTACTS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.WRITE_CONTACTS);
        int GET_ACCOUNTS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.GET_ACCOUNTS);

        // LOCATION
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        // MICROPHONE
        int RECORD_AUDIO = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.RECORD_AUDIO);

        // PHONE
        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.READ_PHONE_STATE);
        int CALL_PHONE = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.CALL_PHONE);
        int READ_CALL_LOG = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.READ_CALL_LOG);
        int WRITE_CALL_LOG = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.WRITE_CALL_LOG);
        int USE_SIP = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.USE_SIP);
        int PROCESS_OUTGOING_CALLS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.PROCESS_OUTGOING_CALLS);

        // SENSORS
        int BODY_SENSORS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.BODY_SENSORS);

        // SMS
        int SEND_SMS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.SEND_SMS);
        int RECEIVE_SMS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.RECEIVE_SMS);
        int READ_SMS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.READ_SMS);
        int RECEIVE_WAP_PUSH = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.RECEIVE_WAP_PUSH);
        int RECEIVE_MMS = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.RECEIVE_MMS);

        // STORAGE
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(PermissionFramework.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean checkPermissionResult = false;

        if (PERMISSION_CALENDAR) {
            // CALENDAR
            checkPermissionResult = READ_CALENDAR == PackageManager.PERMISSION_GRANTED &&
                    WRITE_CALENDAR == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_CAMERA) {
            // CAMERA
            checkPermissionResult = CAMERA == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_CONTACTS) {
            // CONTACTS
            checkPermissionResult = READ_CONTACTS == PackageManager.PERMISSION_GRANTED &&
                    WRITE_CONTACTS == PackageManager.PERMISSION_GRANTED &&
                    GET_ACCOUNTS == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_LOCATION) {
            // LOCATION
            checkPermissionResult = ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                    ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_MICROPHONE) {
            // MICROPHONE
            checkPermissionResult = RECORD_AUDIO == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_PHONE) {
            // PHONE
            checkPermissionResult = READ_PHONE_STATE == PackageManager.PERMISSION_GRANTED &&
                    CALL_PHONE == PackageManager.PERMISSION_GRANTED &&
                    READ_CALL_LOG == PackageManager.PERMISSION_GRANTED &&
                    WRITE_CALL_LOG == PackageManager.PERMISSION_GRANTED &&
                    USE_SIP == PackageManager.PERMISSION_GRANTED &&
                    PROCESS_OUTGOING_CALLS == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_SENSORS) {
            // SENSORS
            checkPermissionResult = BODY_SENSORS == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_SMS) {
            // SMS
            checkPermissionResult = SEND_SMS == PackageManager.PERMISSION_GRANTED &&
                    RECEIVE_SMS == PackageManager.PERMISSION_GRANTED &&
                    READ_SMS == PackageManager.PERMISSION_GRANTED &&
                    RECEIVE_WAP_PUSH == PackageManager.PERMISSION_GRANTED &&
                    RECEIVE_MMS == PackageManager.PERMISSION_GRANTED;
        } else if (PERMISSION_STORAGE) {
            // STORAGE
            checkPermissionResult = READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                    WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED;
        }

        return checkPermissionResult;
    }

    private void requestPermission() {

        if (PERMISSION_CALENDAR) {
            // CALENDAR
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_CAMERA) {
            // CAMERA
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.CAMERA,
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_CONTACTS) {
            // CONTACTS
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS,
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_LOCATION) {
            // LOCATION
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_MICROPHONE) {
            // MICROPHONE
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_PHONE) {
            // PHONE
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_SENSORS) {
            // SENSORS
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.BODY_SENSORS,
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_SMS) {
            // SMS
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS
            }, PERMISSION_REQUEST_CODE);

        } else if (PERMISSION_STORAGE) {
            // STORAGE
            ActivityCompat.requestPermissions(PermissionFramework.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    // CALENDAR
                    boolean READ_CALENDAR_Accepted = false;
                    boolean WRITE_CALENDAR_Accepted = false;

// CAMERA
                    boolean CAMERA_Accepted = false;

// CONTACTS
                    boolean READ_CONTACTS_Accepted = false;
                    boolean WRITE_CONTACTS_Accepted = false;
                    boolean GET_ACCOUNTS_Accepted = false;

// LOCATION
                    boolean ACCESS_FINE_LOCATION_Accepted = false;
                    boolean ACCESS_COARSE_LOCATION_Accepted = false;

// MICROPHONE
                    boolean RECORD_AUDIO_Accepted = false;

// PHONE
                    boolean READ_PHONE_STATE_Accepted = false;
                    boolean CALL_PHONE_Accepted = false;
                    boolean READ_CALL_LOG_Accepted = false;
                    boolean WRITE_CALL_LOG_Accepted = false;
                    boolean USE_SIP_Accepted = false;
                    boolean PROCESS_OUTGOING_CALLS_Accepted = false;

// SENSORS
                    boolean BODY_SENSORS_Accepted = false;

// SMS
                    boolean SEND_SMS_Accepted = false;
                    boolean RECEIVE_SMS_Accepted = false;
                    boolean READ_SMS_Accepted = false;
                    boolean RECEIVE_WAP_PUSH_Accepted = false;
                    boolean RECEIVE_MMS_Accepted = false;

// STORAGE
                    boolean READ_EXTERNAL_STORAGE_Accepted = false;
                    boolean WRITE_EXTERNAL_STORAGE_Accepted = false;

                    boolean isPermissionGranted = false;

                    // CALENDAR
                    if (PERMISSION_CALENDAR) {
                        READ_CALENDAR_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        WRITE_CALENDAR_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (READ_CALENDAR_Accepted &&
                                WRITE_CALENDAR_Accepted) {
                            isPermissionGranted = true;

                            sharedPrefrence.setValue(PG_CALENDAR, isPermissionGranted + "");
                        }
                    } else

                        // CAMERA
                        if (PERMISSION_CAMERA) {
                            CAMERA_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                            if (CAMERA_Accepted) {
                                isPermissionGranted = true;

                                sharedPrefrence.setValue(PG_CAMERA, isPermissionGranted + "");
                            }
                        } else

                            // CONTACTS
                            if (PERMISSION_CONTACTS) {
                                READ_CONTACTS_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                                WRITE_CONTACTS_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                                GET_ACCOUNTS_Accepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                                if (READ_CONTACTS_Accepted &&
                                        WRITE_CONTACTS_Accepted &&
                                        GET_ACCOUNTS_Accepted) {
                                    isPermissionGranted = true;

                                    sharedPrefrence.setValue(PG_CONTACTS, isPermissionGranted + "");
                                }
                            } else

                                // LOCATION
                                if (PERMISSION_LOCATION) {
                                    ACCESS_FINE_LOCATION_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                                    ACCESS_COARSE_LOCATION_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                                    if (ACCESS_FINE_LOCATION_Accepted &&
                                            ACCESS_COARSE_LOCATION_Accepted) {
                                        isPermissionGranted = true;

                                        sharedPrefrence.setValue(PG_LOCATION, isPermissionGranted + "");
                                    }
                                } else

                                    // MICROPHONE
                                    if (PERMISSION_MICROPHONE) {
                                        RECORD_AUDIO_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                                        if (RECORD_AUDIO_Accepted) {
                                            isPermissionGranted = true;

                                            sharedPrefrence.setValue(PG_MICROPHONE, isPermissionGranted + "");
                                        }
                                    } else

                                        // PHONE
                                        if (PERMISSION_PHONE) {
                                            READ_PHONE_STATE_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                                            CALL_PHONE_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                                            READ_CALL_LOG_Accepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                                            WRITE_CALL_LOG_Accepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                                            USE_SIP_Accepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                                            PROCESS_OUTGOING_CALLS_Accepted = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                                            if (READ_PHONE_STATE_Accepted &&
                                                    CALL_PHONE_Accepted &&
                                                    READ_CALL_LOG_Accepted &&
                                                    WRITE_CALL_LOG_Accepted &&
                                                    USE_SIP_Accepted &&
                                                    PROCESS_OUTGOING_CALLS_Accepted) {
                                                isPermissionGranted = true;

                                                sharedPrefrence.setValue(PG_PHONE, isPermissionGranted + "");
                                            }
                                        } else

                                            // SENSORS
                                            if (PERMISSION_SENSORS) {
                                                BODY_SENSORS_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                                                if (BODY_SENSORS_Accepted) {
                                                    isPermissionGranted = true;

                                                    sharedPrefrence.setValue(PG_SENSORS, isPermissionGranted + "");
                                                }
                                            } else

                                                // SMS
                                                if (PERMISSION_SMS) {
                                                    SEND_SMS_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                                                    RECEIVE_SMS_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                                                    READ_SMS_Accepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                                                    RECEIVE_WAP_PUSH_Accepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                                                    RECEIVE_MMS_Accepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                                                    if (SEND_SMS_Accepted &&
                                                            RECEIVE_SMS_Accepted &&
                                                            READ_SMS_Accepted &&
                                                            RECEIVE_WAP_PUSH_Accepted &&
                                                            RECEIVE_MMS_Accepted) {
                                                        isPermissionGranted = true;

                                                        sharedPrefrence.setValue(PG_SMS, isPermissionGranted + "");
                                                    }
                                                } else

                                                    // STORAGE
                                                    if (PERMISSION_STORAGE) {
                                                        READ_EXTERNAL_STORAGE_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                                                        WRITE_EXTERNAL_STORAGE_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                                                        if (READ_EXTERNAL_STORAGE_Accepted &&
                                                                WRITE_EXTERNAL_STORAGE_Accepted) {
                                                            isPermissionGranted = true;

                                                            sharedPrefrence.setValue(PG_STORAGE, isPermissionGranted + "");
                                                        }
                                                    }

                    if (isPermissionGranted) {

//                        Toast.makeText(PermissionFramework.this, "Permission Accepted", Toast.LENGTH_SHORT).show();

                        sentResult();

                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (rotationalPermission()) {

                                sentFailResult();
//                                Toast.makeText(PermissionFramework.this, "Please allow permission to use this feature", Toast.LENGTH_SHORT).show();

                                return;
                            } else {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                    if (pName.length() > 0) {
//                                        Toast.makeText(PermissionFramework.this, "Please enable " + pName, Toast.LENGTH_SHORT).show();
                                    } else if (pGroup.length() > 0) {
//                                        Toast.makeText(PermissionFramework.this, "Please enable " + pGroup, Toast.LENGTH_SHORT).show();
                                    }

                                    Intent i = new Intent();
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    i.setData(Uri.parse("package:" + getPackageName()));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(i);

                                } else {

                                    sentFailResult();
                                }

                            }

                        }
                    }
                }


                break;
        }
    }

    private boolean rotationalPermission() {

        boolean permission = false;

        if (PERMISSION_CALENDAR) {
            // CALENDAR

            permission = shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR);

        } else if (PERMISSION_CAMERA) {
            // CAMERA

            permission = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);

        } else if (PERMISSION_CONTACTS) {
            // CONTACTS

            permission = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS);

        } else if (PERMISSION_LOCATION) {
            // LOCATION

            permission = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);

        } else if (PERMISSION_MICROPHONE) {
            // MICROPHONE

            permission = shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO);

        } else if (PERMISSION_PHONE) {
            // PHONE

            permission = shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALL_LOG) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.USE_SIP) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.PROCESS_OUTGOING_CALLS);

        } else if (PERMISSION_SENSORS) {
            // SENSORS

            permission = shouldShowRequestPermissionRationale(Manifest.permission.BODY_SENSORS);

        } else if (PERMISSION_SMS) {
            // SMS
            permission = shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_WAP_PUSH) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_MMS);

        } else if (PERMISSION_STORAGE) {
            // STORAGE

            permission = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        return permission;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PermissionFramework.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                        // .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public static void configurePermissions(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndSetPermission(PermissionFramework.PG_CALENDAR, ctx);
            checkAndSetPermission(PermissionFramework.PG_CAMERA, ctx);
            checkAndSetPermission(PermissionFramework.PG_CONTACTS, ctx);
            checkAndSetPermission(PermissionFramework.PG_LOCATION, ctx);
            checkAndSetPermission(PermissionFramework.PG_MICROPHONE, ctx);
            checkAndSetPermission(PermissionFramework.PG_PHONE, ctx);
            checkAndSetPermission(PermissionFramework.PG_SENSORS, ctx);
            checkAndSetPermission(PermissionFramework.PG_SMS, ctx);
            checkAndSetPermission(PermissionFramework.PG_STORAGE, ctx);
        }
    }

    public static void checkAndSetPermission(String key, Context ctx) {

        boolean permissionStatus = false;

        SharedPrefrence sharedPrefrence = SharedPrefrence.getInstance(ctx);
        String value = sharedPrefrence.getValue(key) + "";
        if (value.equals("null") || value.length() == 0 || value == null || value.equals("" + permissionStatus)) {
            // Permission not set
        } else {
            if(checkSpecificPermission(ctx, key)) {
                permissionStatus = true;
            }
        }

        sharedPrefrence.setValue(key, permissionStatus + "");
    }

    public static boolean isDirectAccessSinglePermission(Context ctx, String key) {
        boolean directAccess = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            SharedPrefrence sharedPrefrence = SharedPrefrence.getInstance(ctx);
            String value = sharedPrefrence.getValue(key) + "";
            if (value.equals("null") || value.length() == 0 || value == null || value.equals("false")) {
                // Permission not set
                directAccess = false;
            } else {
                directAccess = true;
            }

        } else {
            directAccess = true;
        }

        return directAccess;
    }

    public static boolean isDirectAccessMultiplePermission(Context ctx, ArrayList<String> permissions) {
        boolean directAccess = true;

        if(permissions == null) {
            permissions = new ArrayList<String>();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for(int i=0 ; i<permissions.size() ; i++) {

                if(directAccess == true) {
                    SharedPrefrence sharedPrefrence = SharedPrefrence.getInstance(ctx);
                    String value = sharedPrefrence.getValue(permissions.get(i)) + "";
                    if (value.equals("null") || value.length() == 0 || value == null || value.equals("false")) {
                        // Permission not set
                        directAccess = false;
                    } else {
                        directAccess = true;
                    }
                }
            }

        } else {
            directAccess = true;
        }

        return directAccess;
    }

    public static boolean checkSpecificPermission(Context context, String key) {

        // CALENDAR
        int READ_CALENDAR = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
        int WRITE_CALENDAR = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);

        // CAMERA
        int CAMERA = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

        // CONTACTS
        int READ_CONTACTS = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        int WRITE_CONTACTS = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS);
        int GET_ACCOUNTS = ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS);

        // LOCATION
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        // MICROPHONE
        int RECORD_AUDIO = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);

        // PHONE
        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        int CALL_PHONE = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        int READ_CALL_LOG = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG);
        int WRITE_CALL_LOG = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG);
        int USE_SIP = ContextCompat.checkSelfPermission(context, Manifest.permission.USE_SIP);
        int PROCESS_OUTGOING_CALLS = ContextCompat.checkSelfPermission(context, Manifest.permission.PROCESS_OUTGOING_CALLS);

        // SENSORS
        int BODY_SENSORS = ContextCompat.checkSelfPermission(context, Manifest.permission.BODY_SENSORS);

        // SMS
        int SEND_SMS = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        int RECEIVE_SMS = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        int READ_SMS = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        int RECEIVE_WAP_PUSH = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_WAP_PUSH);
        int RECEIVE_MMS = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_MMS);

        // STORAGE
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean checkPermissionResult = false;

        if (key.equals(PG_CALENDAR)) {
            // CALENDAR
            checkPermissionResult = READ_CALENDAR == PackageManager.PERMISSION_GRANTED &&
                    WRITE_CALENDAR == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_CAMERA)) {
            // CAMERA
            checkPermissionResult = CAMERA == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_CONTACTS)) {
            // CONTACTS
            checkPermissionResult = READ_CONTACTS == PackageManager.PERMISSION_GRANTED &&
                    WRITE_CONTACTS == PackageManager.PERMISSION_GRANTED &&
                    GET_ACCOUNTS == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_LOCATION)) {
            // LOCATION
            checkPermissionResult = ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                    ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_MICROPHONE)) {
            // MICROPHONE
            checkPermissionResult = RECORD_AUDIO == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_PHONE)) {
            // PHONE
            checkPermissionResult = READ_PHONE_STATE == PackageManager.PERMISSION_GRANTED &&
                    CALL_PHONE == PackageManager.PERMISSION_GRANTED &&
                    READ_CALL_LOG == PackageManager.PERMISSION_GRANTED &&
                    WRITE_CALL_LOG == PackageManager.PERMISSION_GRANTED &&
                    USE_SIP == PackageManager.PERMISSION_GRANTED &&
                    PROCESS_OUTGOING_CALLS == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_SENSORS)) {
            // SENSORS
            checkPermissionResult = BODY_SENSORS == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_SMS)) {
            // SMS
            checkPermissionResult = SEND_SMS == PackageManager.PERMISSION_GRANTED &&
                    RECEIVE_SMS == PackageManager.PERMISSION_GRANTED &&
                    READ_SMS == PackageManager.PERMISSION_GRANTED &&
                    RECEIVE_WAP_PUSH == PackageManager.PERMISSION_GRANTED &&
                    RECEIVE_MMS == PackageManager.PERMISSION_GRANTED;
        } else if (key.equals(PG_STORAGE)) {
            // STORAGE
            checkPermissionResult = READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                    WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED;
        }

        return checkPermissionResult;
    }
}
