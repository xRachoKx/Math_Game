package com.example.mathgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeFragment extends Fragment {
    private ScrollView upgradeScrollView;
    private int scrollYPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade, container, false);
        upgradeScrollView = view.findViewById(R.id.upgrade_scroll_view);
        LinearLayout upgradeLayout = view.findViewById(R.id.upgrade_layout);

        MainActivity activity = (MainActivity) getActivity();
        GameManager gameManager = activity.getGameManager();
        HashMap<String, Integer> numbers = new HashMap<>(gameManager.getPlayerData().getNumbers());
        int maxNumber = gameManager.getPlayerData().getMaxNumber();

        for (int num = 1; num <= maxNumber; num++) {
            String number = String.valueOf(num);
            int count = numbers.getOrDefault(number, 0);
            ArrayList<String> expression = gameManager.getPlayerData().getExpression();
            for (String cell : expression) {
                if (cell != null && cell.equals(number)) {
                    count--;
                }
            }

            LinearLayout numberLayout = new LinearLayout(getContext());
            numberLayout.setOrientation(LinearLayout.HORIZONTAL);
            numberLayout.setPadding(0, 8, 0, 8);

            TextView numberText = new TextView(getContext());
            numberText.setText(String.format("%s: %d", number, Math.max(0, count)));
            numberText.setTextColor(getResources().getColor(android.R.color.white));
            numberText.setPadding(0, 0, 16, 0);
            numberLayout.addView(numberText);

            final int finalCount = count;
            Button upgradeButton = new Button(getContext());
            upgradeButton.setText(String.format("x3 %s -> x1 %d", number, num + 1));
            upgradeButton.setBackgroundResource(finalCount >= 3 ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
            upgradeButton.setPadding(15, 3, 15, 3);
            upgradeButton.setOnClickListener(v -> {
                if (finalCount >= 3) {
                    scrollYPosition = upgradeScrollView.getScrollY();
                    gameManager.upgradeNumber(number, 1);
                    activity.updateUI();
                    UpgradeFragment newFragment = new UpgradeFragment();
                    newFragment.setScrollYPosition(scrollYPosition);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).commit();
                }
            });
            numberLayout.addView(upgradeButton);

            Button upgradeAllButton = new Button(getContext());
            upgradeAllButton.setText("Улучшить все");
            upgradeAllButton.setBackgroundResource(finalCount >= 6 ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
            upgradeAllButton.setPadding(15, 3, 15, 3);
            upgradeAllButton.setOnClickListener(v -> {
                if (finalCount >= 6) {
                    scrollYPosition = upgradeScrollView.getScrollY();
                    gameManager.upgradeNumber(number, finalCount / 3);
                    activity.updateUI();
                    UpgradeFragment newFragment = new UpgradeFragment();
                    newFragment.setScrollYPosition(scrollYPosition);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).commit();
                }
            });
            numberLayout.addView(upgradeAllButton);

            upgradeLayout.addView(numberLayout);
        }

        upgradeScrollView.post(() -> upgradeScrollView.setScrollY(scrollYPosition));

        return view;
    }

    public void setScrollYPosition(int position) {
        this.scrollYPosition = position;
    }
}