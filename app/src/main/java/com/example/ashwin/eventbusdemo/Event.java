package com.example.ashwin.eventbusdemo;

import java.util.concurrent.atomic.AtomicBoolean;

public class Event {
    // Event used to send message from fragment to activity
    public static class FragmentActivityMessage {
        private String message;
        public FragmentActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to fragment
    public static class ActivityFragmentMessage {
        private String message;
        public ActivityFragmentMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to activity
    public static class ActivityActivityMessage {
        private String message;
        public ActivityActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event can be used to get the value only once by using getIfNotHandled method.
    public static class SingleMessage<T> {
        private final AtomicBoolean isHandled;
        private final T value;

        public SingleMessage(T value) {
            isHandled = new AtomicBoolean(false);
            this.value = value;
        }

        public T getIfNotHandled() {
            if (isHandled.compareAndSet(false, true)) {
                return value;
            }
            return null;
        }

        public T get() {
            return value;
        }
    }
}
