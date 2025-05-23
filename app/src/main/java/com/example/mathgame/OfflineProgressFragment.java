package com.example.mathgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class OfflineProgressFragment extends DialogFragment {
    private static final String LAST_EXIT_TIME_KEY = "last_exit_time";
    private static final String OFFLINE_POINTS_KEY = "offline_points";
    private TextView absenceTimeText;
    private TextView earnedPointsText;
    private double offlinePoints;

    public static OfflineProgressFragment newInstance(long lastExitTime, double offlinePoints) {
        OfflineProgressFragment fragment = new OfflineProgressFragment();
        Bundle args = new Bundle();
        args.putLong(LAST_EXIT_TIME_KEY, lastExitTime);
        args.putDouble(OFFLINE_POINTS_KEY, offlinePoints);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_progress, container, false);
        TextView titleText = view.findViewById(R.id.offline_title);
        absenceTimeText = view.findViewById(R.id.absence_time_text);
        TextView earnedTitleText = view.findViewById(R.id.earned_title);
        earnedPointsText = view.findViewById(R.id.earned_points_text);
        Button claimButton = view.findViewById(R.id.claim_button);

        titleText.setText("Вы отсутствовали:");
        earnedTitleText.setText("Вы заработали:");

        if (getArguments() != null) {
            long lastExitTime = getArguments().getLong(LAST_EXIT_TIME_KEY);
            offlinePoints = getArguments().getDouble(OFFLINE_POINTS_KEY);
            long timeDiff = System.currentTimeMillis() - lastExitTime;
            String timeStr = formatTime(timeDiff);
            absenceTimeText.setText(timeStr);
            earnedPointsText.setText(String.format("%.0f", offlinePoints));
        }

        claimButton.setBackgroundResource(R.drawable.button_enabled_background);
        claimButton.setText("ПОЛУЧИТЬ");
        claimButton.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    @Override
    public void onDismiss(android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getArguments() != null) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.getGameManager().getPlayerData().addPoints(offlinePoints);
                activity.updateUI();
            }
        }
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long days = seconds / (24 * 3600);
        seconds %= (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        if (days > 0) {
            return String.format("%dd %dh", days, hours);
        } else if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm %ds", minutes, seconds);
        }
    }
}