package com.example.mathgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class RebirthFragment extends Fragment {
    private TextView pointsRequirementText;
    private TextView rebirthPointsText;
    private ProgressBar rebirthProgress;
    private Button rebirthOverlayButton;
    private Button startNumberButton;
    private Button powerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rebirth, container, false);
        pointsRequirementText = view.findViewById(R.id.points_requirement_text);
        rebirthPointsText = view.findViewById(R.id.rebirth_points_text);
        rebirthProgress = view.findViewById(R.id.rebirth_progress);
        rebirthOverlayButton = view.findViewById(R.id.rebirth_overlay_button);
        startNumberButton = view.findViewById(R.id.start_number_upgrade_button);
        powerButton = view.findViewById(R.id.power_upgrade_button);

        updateUI();

        rebirthOverlayButton.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            GameManager gameManager = activity.getGameManager();
            if (gameManager.getPlayerData().getPoints() >= gameManager.getRebirthRequirement()) {
                activity.showRebirthConfirmFragment();
            }
        });

        startNumberButton.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            GameManager gameManager = activity.getGameManager();
            gameManager.upgradeStartNumber();
            updateUI();
        });

        powerButton.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            GameManager gameManager = activity.getGameManager();
            gameManager.upgradePower();
            updateUI();
        });

        return view;
    }

    public void updateUI() {
        MainActivity activity = (MainActivity) getActivity();
        GameManager gameManager = activity.getGameManager();

        double points = gameManager.getPlayerData().getPoints();
        double requirement = gameManager.getRebirthRequirement();
        int progress = (int) Math.min(100, (points / requirement) * 100);
        pointsRequirementText.setText(String.format("%.0f/%.0f", points, requirement));
        rebirthPointsText.setText(String.format("ОП: %d", gameManager.getPlayerData().getRebirthPoints()));
        rebirthProgress.setProgress(progress);

        startNumberButton.setText(String.format("Стартовая цифра: %d (%d ОП)", gameManager.getPlayerData().getStartNumberLevel(), gameManager.getStartNumberUpgradeCost()));
        powerButton.setText(String.format("Степень: %.1f -> %.1f (%d ОП)", 1 + gameManager.getPlayerData().getPowerLevel() * 0.1, 1 + (gameManager.getPlayerData().getPowerLevel() + 1) * 0.1, gameManager.getPowerUpgradeCost()));
    }
}