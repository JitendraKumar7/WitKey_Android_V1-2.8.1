package app.witkey.live.utils;

/**
 * created by developer on 6/9/2017.
 * <p>
 * Common Keys for web-services, intents and sharedprefs.
 */
public class Keys {
    /**
     * Label of the element in json that contains the actual data returned from webservice.
     */
    public static final String DATA_ITEM_JSON_WEBSERVICE = "Result";

    /**
     * Keys used for passing params in webservice.
     */
    public static String Body = "body";
    public static final String METHOD = "_method";

    public static final String ID = "id";
    public static final String STREAM_TIME = "stream_time";
    public static final String FOLLOW_USER = "follow_user_id";
    public static final String UNFOLLOW_USER = "unfollow_user";

    // USER SIGNUP CALL KEYS
    public static final String USER_NAME = "username";
    public static final String USER_FULL_NAME = "full_name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_GENDER = "gender";
    public static final String USER_DOB = "dob";
    public static final String USER_COUNTRY = "country";
    public static final String USER_EMAIL = "email";
    public static final String USER_STATUS_TEXT = "status_text";
    public static final String USER_PROFILE_PICTURE = "profile_picture";
    public static final String OLD_PASSWORD = "old_password";
    public static final String PASSWORD_CONFIRMATION = "password_confirmation";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String GOOGLE_ACCESS_DATA = "google_access_data";
    public static final String FULLNAME = "fullname";
    public static final String IMAGE_URL = "image_url";

    // USER DEVICE INFO KEYS
    public static final String DEVICE_REGISTERATION_ID = "registration_id";
    public static final String APP_SETTINGS_OBJECT = "appSettingsObject";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_TYPE = "device_type";
    public static final String APP_VERSION = "android_version";
    public static final String DEVICE_USER_ID = "ud_id";
    public static final String DEVICE_OS_VERSION = "os_version";
    public static final String DEVICE_RESOLUTION = "device_resolution";
    // NOT USED YET
    public static final String DEVICE_ID = "device_id";

    // USER SHARED PREFERENCE KEYS
    public static final String USER_ID = "user_id";
    public static final String USER_LOGIN_STATUS = "isUserLogin";
    public static final String USER_TOKEN = "userToken";
    public static final String USER_OBJECT = "userObject";
    public static final String TOKEN = "token";
    public static final String FIRST_VISIT = "first_visit";
    public static final String WOWZA_KEY = "wowza_key";
    public static final String PUBNUB_PUBLISH_KEY = "pubnub_publish_key";
    public static final String PUBNUB_SUB_KEY = "pubnub_sub_key";
    public static final String PAYMENT_KEY = "payment_key";
    public static final String REGISTER_TOKEN = "RegToken";
    public static final String USER_PROGRESS_DETAIL = "user_progress_detail";
    public static final String USER_PRIVATE_CHAT_KEY = "chat_signature";
    public static final String USER_STREAM_CHAT_KEY = "stream_chat_signature";
    public static final String USER_GIFTS_CURRENT_VERSION = "gift_version";
    public static final String USER_GIFTS_CURRENT_LIST = "gift_list_zip";

    // USER LIVE STREAMS KEYS
    public static final String STREAM_ID = "stream_id";
    public static final String STREAM_USER_ID = "stream_user_id";
    public static final String NAME = "name";
    public static final String QUALITY = "quality";
    public static final String IS_PUBLIC = "is_public";
    public static final String ALLOW_COMMENTS = "allow_comments";
    public static final String ALLOW_TAG_REQUEST = "allow_tag_requests";
    public static final String AVAILABLE_LATER = "available_later";
    public static final String LONGITUDE = "lng";
    public static final String LATITUDE = "lat";
    public static final String CATEGORY_ID = "category_id";
    public static final String LIST_OFFSET = "offset";
    public static final String LIST_LIMIT = "limit";
    public static final String LIST_TYPE = "list_type";
    public static final String TYPE = "type";
    public static final String IS_DELETE = "is_deleted";
    public static final String TIME_DURATION = "duration";
    public static final String STATUS = "status";

    // GIFT KEYS
    public static final String GIFT_DETAIL = "gift_detail";

    // PAYMENT KEYS
    public static final String PAYMENT_INVOICE = "invoice";
    public static final String PAYMENT_TRANSACTION_ID = "transaction_id";
    public static final String PAYMENT_CHECK_SIGN = "checksign";
    public static final String PAYMENT_STATUS = "status";
    public static final String PAYMENT_PRODUCT_ID = "productid";
    public static final String PAYMENT_CUSTOM_EMAIL = "custom_email";
    public static final String PAYMENT_AMOUNT = "amount";
    public static final String PAYMENT_TRACE_ID = "trace_no";
    public static final String PAYMENT_PACKAGE_ID = "package_id";

    // USER MOMENTS
    public static final String MOMENTS_ID = "moment_id";
    public static final String MOMENTS_USER_ID = "moment_user_id";
    public static final String MOMENTS_TEXT = "status_text";
    public static final String MOMENTS_IMAGE_1 = "image_array1";
    public static final String MOMENTS_IMAGE = "image_array";
    public static final String MOMENTS_IMAGE_2 = "image_array2";
    public static final String MOMENTS_IMAGE_3 = "image_array3";
    public static final String MOMENTS_IMAGE_4 = "image_array4";
    public static final String MOMENTS_IMAGE_5 = "image_array5";

    // COMMENTS
    public static final String COMMENT_TEXT = "comment";
    public static final String BLOCK_USER = "block_user";
    public static final String FRIEND_USER_ID = "friend_user_id";
    public static final String PRIVATE_CHAT_ID = "chat_id";

    // FEEDBACK
    public static final String ISSUEES = "issues";
    public static final String OTHER_REMARKS = "other_remarks";

    // NOTIFICATION
    public static final String MESSAGE_NOTIFICATION = "message_notification_status";
    public static final String LIVE_NOTIFICATION = "live_notification_status";
}
