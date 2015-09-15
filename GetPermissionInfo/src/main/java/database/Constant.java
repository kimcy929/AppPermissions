package database;

import android.Manifest;

/**
 * Created by kimcy on 11/09/2015.
 */
public final class Constant {

    public static final int UPDATE_PROGRESS = 9;//Message
    public static final String PACKAGE_NAME = "PACKAGE_NAME";

    /*--------- For fragment -----------------*/
    public static final int ALL_APP = 0;
    public static final int CAN_COST_ME_MONEY = 1;
    public static final int CAN_SEE_PERSON_INFO = 2;
    public static final int CAN_SEE_LOCATION_INFO = 3;
    public static final int CAN_USE_CAMERA_AUDIO = 4;
    public static final int CAN_START_ON_BOOT = 5;
    public static final int CAN_CHANGE_SYSTEM = 6;
    public static final int CUSTOM_FILTER = 9;

    /*-------------------- Filter Fragment -----------------------------*/
    public static final String[] ARRAY_CAN_COST_ME_MONEY = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            "com.android.vending.BILLING"
    };

    public static final String[] ARRAY_CAN_SEE_PERSON_INFO = new String[]{
            Manifest.permission.READ_CALENDAR,
            "android.permission.READ_CALL_LOG",
            Manifest.permission.READ_CONTACTS,
            "com.android.voicemail.permission.READ_VOICEMAIL",
            Manifest.permission.READ_SMS,
            "android.permission.READ_PROFILE",
            "android.permission.READ_SOCIAL_STREAM"
    };

    public static final String[] ARRAY_CAN_SEE_LOCATION_INFO = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] ARRAY_CAN_USE_CAMERA_AUDIO = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    public static final String[] ARRAY_CAN_START_ON_BOOT = new String[]{
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };

    public static final String[] ARRAY_CAN_CHANGE_SYSTEM = new String[]{
            Manifest.permission.WRITE_SETTINGS
    };

    /*------------------- Advance Type Filter --------------*/
    public static final int FILTER_OR_TYPE = 0;
    public static final int FILTER_AND_TYPE = 1;

    public static final String FILTER_TYPE = "FILTER_TYPE";
    public static final String FILTER_ARRAY = "FILER_ARRAY";
    public static final String APP_NAME = "APP_NAME";
    public static final String ARRAY_ALL_PERMISSION = "ARRAY_ALL_PERMISSION";
    public static final String ARRAY_PERMISSION_FILTERED = "ARRAY_PERMISSION_FILTERED";
    public static final String CUR_APP = "CUR_APP";
    public static final String FRAGMENT_POSITION = "FRAGMENT_POSITION";
    public static final String PREFIX_PERMISSION = "android.permission";

    /*-------------- Vendor ---------------*/
    public static final String VENDOR = "VENDOR";
    public static final String PLAY_STORE = "PLAY STORE";
    public static final String SAMSUNG_STORE = "SAMSUNG STORE";
    public static final String AMAZON_STORE = "AMAZON STORE";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String PRE_INSTALLED = "PRE-INSTALLED";

    /*------------ Filter by ------------*/
    public static final String FILTER_BY = "FILTER_BY";
    public static final int FILER_BY_PERMISSION = 0;
    public static final int FILER_BY_VENDOR = 1;
}
