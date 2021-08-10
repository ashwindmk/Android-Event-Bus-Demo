package com.example.ashwin.eventbusdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button submitButton = (Button) view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etMessage = (EditText) view.findViewById(R.id.editText);
                Event.FragmentActivityMessage fragmentActivityMessageEvent = new Event.FragmentActivityMessage(String.valueOf(etMessage.getText()));
                EventBus.getDefault().post(fragmentActivityMessageEvent);
            }
        });

        Button registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            EventBus.getDefault().register(UserFragment.this);
            Log.d(Constant.TAG, UserFragment.this.getClass().getSimpleName() + ": registered");
        });

        Button unregisterButton = view.findViewById(R.id.unregister_button);
        unregisterButton.setOnClickListener(v -> {
            EventBus.getDefault().unregister(UserFragment.this);
            Log.d(Constant.TAG, UserFragment.this.getClass().getSimpleName() + ": unregistered");
        });
    }

    @Subscribe
    public void onMessageEvent(Event.ActivityFragmentMessage activityFragmentMessage) {
        TextView messageView = (TextView) getView().findViewById(R.id.message);
        messageView.setText(getString(R.string.message_received) + " " + activityFragmentMessage.getMessage());

        Toast.makeText(getActivity(), getString(R.string.message_fragment) + " " + activityFragmentMessage.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBus.getDefault().unregister(this);
    }
}
