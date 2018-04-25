package app.witkey.live.utils.eventbus;

import java.util.ArrayList;

import app.witkey.live.items.ProgrammeBO;

/**
 * created by developer on 10/03/2017.
 */

public class Events {

    // EVENT USED TO SEND STRING FROM BOTTOM SHEET FRAGMENT TO ACTIVITY.
    public static class FragmentToSignUpActivityMessage {
        private String message;

        public FragmentToSignUpActivityMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // EVENT USED TO SEND STRING FROM BOTTOM SHEET FRAGMENT TO FRAGMENT.
    public static class FragmentToProfileSettingMessage {
        private String message;

        public FragmentToProfileSettingMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // EVENT USED TO SEND STRING FROM BOTTOM SHEET FRAGMENT TO FRAGMENT.
    public static class FragmentToUserProfileScrollViewMessage {
        private String message;

        public FragmentToUserProfileScrollViewMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // EVENT USED TO SEND STRING FROM BOTTOM SHEET FRAGMENT TO ACTIVITY.
    public static class FragmentToGoLiveUpActivityMessage {
        private String message;
        private int giftLevel;
        private int giftPrice;
        private String giftName;

        public String getGiftURL() {
            return giftURL;
        }

        public void setGiftURL(String giftURL) {
            this.giftURL = giftURL;
        }

        private String giftURL;
        private int id;
        private int giftCount;

        public int getGiftCount() {
            return giftCount;
        }

        public void setGiftCount(int giftCount) {
            this.giftCount = giftCount;
        }


        public FragmentToGoLiveUpActivityMessage(String message) {
            this.message = message;
        }

        public FragmentToGoLiveUpActivityMessage(String message, int id) {
            this.message = message;
            this.id = id;
        }

        public FragmentToGoLiveUpActivityMessage(String message, String giftName, int id, int giftPrice_, int giftCount, String giftURL, int giftLevel) {
            this.giftName = giftName;
            this.message = message;
            this.id = id;
            this.giftPrice = giftPrice_;
            this.giftCount = giftCount;
            this.giftURL = giftURL;
            this.giftLevel = giftLevel;
        }

        public int getGiftLevel() {
            return giftLevel;
        }

        public void setGiftLevel(int giftLevel) {
            this.giftLevel = giftLevel;
        }

        public int getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public String getGiftName() {
            return giftName;
        }

        public int getGiftPrice() {
            return giftPrice;
        }
    }

    // EVENT USED TO SEND STRING FROM MAIN FRAGMENT TO CHILD.
    public static class MainFragmentToGoLiveFragmentMessage {
        private String message;

        public MainFragmentToGoLiveFragmentMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // EVENT USED TO SEND STRING FROM TUTORIAL FRAGMENT TO HOME.
    public static class TutorialFragmentToHomeFragmentMessage {
        private String message;

        public TutorialFragmentToHomeFragmentMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // EVENT USED TO SEND STRING FROM UserMomentsImageAddDialog TO UserMoments FRAGMENT.
    public static class UserMomentsFragmentMessage {
        private String message;

        public UserMomentsFragmentMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class BlockUserFragmentMessage {
        private String message;

        public BlockUserFragmentMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class EventPosterActivityMessage {
        private ArrayList<ProgrammeBO> programmeBOs;

        public EventPosterActivityMessage(ArrayList<ProgrammeBO> programmeBOs) {
            this.programmeBOs = programmeBOs;
        }

        public ArrayList<ProgrammeBO> getProgrammeBOs() {
            return programmeBOs;
        }
    }
}
