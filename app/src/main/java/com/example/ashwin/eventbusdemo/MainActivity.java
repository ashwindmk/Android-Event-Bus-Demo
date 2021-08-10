package com.example.ashwin.eventbusdemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new UserFragment())
                .commit();

        Button postMessageButton = findViewById(R.id.post_message_button);
        postMessageButton.setOnClickListener(view -> {
            EditText etMessage = (EditText) findViewById(R.id.activityData);
            Event.ActivityFragmentMessage activityFragmentMessageEvent = new Event.ActivityFragmentMessage(String.valueOf(etMessage.getText()));
            EventBus.getDefault().post(activityFragmentMessageEvent);
        });

        Button postCountButton = findViewById(R.id.post_count_button);
        postCountButton.setOnClickListener(view -> {
            count++;
            EventBus.getDefault().post(count);
            EventBus.getDefault().post(new Event.SingleMessage<Integer>(count));
        });

        Button postStickyButton = findViewById(R.id.post_sticky_button);
        postStickyButton.setOnClickListener(view -> {
            count++;
            EventBus.getDefault().postSticky(count);
            EventBus.getDefault().postSticky(new Event.SingleMessage<Integer>(count));
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            EventBus.getDefault().register(MainActivity.this);
            Log.d(Constant.TAG, MainActivity.this.getClass().getSimpleName() + ": registered");
        });

        Button unregisterButton = findViewById(R.id.unregister_button);
        unregisterButton.setOnClickListener(v -> {
            EventBus.getDefault().unregister(MainActivity.this);
            Log.d(Constant.TAG, MainActivity.this.getClass().getSimpleName() + ": unregistered");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMessageEvent(Event.FragmentActivityMessage fragmentActivityMessage) {
        TextView messageView = (TextView) findViewById(R.id.message);
        messageView.setText(getString(R.string.message_received) + " " + fragmentActivityMessage.getMessage());
        Toast.makeText(getApplicationContext(), getString(R.string.message_main_activity) + " " + fragmentActivityMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCountEvent(Integer n) {
        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": count: " + n);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSingleEvent(Event.SingleMessage<Integer> message) {
        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": onSingleEvent");
        if (message.getIfNotHandled() != null) {
            Log.d(Constant.TAG, this.getClass().getSimpleName() + ": single message: " + message.get());
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStickyEvent(Integer n) {
        // Will get the value if postSticky was invoked.
        // Will not get value if only post was invoked.
        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": sticky count: " + n);
    }

    public void removeSticky() {
        Integer stickyEvent = EventBus.getDefault().getStickyEvent(Integer.class);
        // Better check that an event was actually posted before
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }
}
