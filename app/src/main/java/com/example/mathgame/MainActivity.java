package com.example.mathgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView pointsText, pointsPerSecondText;
    private LinearLayout expressionLayout;
    private Button addCellButton, shopButton, upgradeButton, editButton, rebirthButton;
    private GameManager gameManager;
    private static final String PREFS_NAME = "MathGamePrefs";
    private static final String FIRST_LAUNCH_KEY = "first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointsText = findViewById(R.id.points_text);
        pointsPerSecondText = findViewById(R.id.points_per_second_text);
        expressionLayout = findViewById(R.id.expression_layout).findViewById(R.id.inner_expression_layout);
        addCellButton = findViewById(R.id.add_cell_button);
        shopButton = findViewById(R.id.shop_button);
        upgradeButton = findViewById(R.id.upgrade_button);
        editButton = findViewById(R.id.edit_button);
        rebirthButton = findViewById(R.id.rebirth_button);

        gameManager = new GameManager(this);

        updateUI();

        addCellButton.setOnClickListener(v -> gameManager.addCell());
        shopButton.setOnClickListener(v -> showShopFragment());
        upgradeButton.setOnClickListener(v -> showUpgradeFragment());
        editButton.setOnClickListener(v -> showEditFragment());
        rebirthButton.setOnClickListener(v -> showRebirthFragment());

        checkFirstLaunch();
        checkOfflineProgress();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> {
                        try {
                            gameManager.updatePoints();
                            updateUI();
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (currentFragment instanceof RebirthFragment) {
                                ((RebirthFragment) currentFragment).updateUI();
                            }
                            if (currentFragment instanceof ShopFragment) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment()).commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameManager.getPlayerData().save(this);
    }

    private void checkFirstLaunch() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean(FIRST_LAUNCH_KEY, true);
        if (isFirstLaunch) {
            HowToPlayFragment fragment = new HowToPlayFragment();
            fragment.show(getSupportFragmentManager(), "HowToPlayFragment");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRST_LAUNCH_KEY, false);
            editor.apply();
        }
    }

    private void checkOfflineProgress() {
        long lastExitTime = gameManager.getPlayerData().getLastExitTime();
        long currentTime = System.currentTimeMillis();
        if (lastExitTime > 0 && (currentTime - lastExitTime) > 1000) {
            long offlineTimeSeconds = (currentTime - lastExitTime) / 1000;
            double offlinePoints = gameManager.calculateOfflinePoints(offlineTimeSeconds);
            OfflineProgressFragment fragment = OfflineProgressFragment.newInstance(lastExitTime, offlinePoints);
            fragment.show(getSupportFragmentManager(), "OfflineProgressFragment");
        }
    }

    public void updateUI() {
        pointsText.setText(String.format("%.0f", gameManager.getPlayerData().getPoints()));
        double pointsPerSecond = gameManager.calculatePointsPerSecond();
        pointsPerSecondText.setText(String.format("+%.0f %s в секунду", pointsPerSecond, getPointsDeclension((int) pointsPerSecond)));
        updateExpressionLayout();
        double cost = gameManager.getCellCost();
        addCellButton.setText(String.format("[%.0f]", cost));
        addCellButton.setBackgroundResource(gameManager.getPlayerData().getPoints() >= cost ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
    }

    private String getPointsDeclension(int points) {
        int lastDigit = points % 10;
        int lastTwoDigits = points % 100;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            return "очков";
        }
        if (lastDigit == 1) {
            return "очко";
        }
        if (lastDigit >= 2 && lastDigit <= 4) {
            return "очка";
        }
        return "очков";
    }

    public void updateExpressionLayout() {
        expressionLayout.removeAllViews();
        for (String cell : gameManager.getPlayerData().getExpression()) {
            TextView cellView = new TextView(this);
            cellView.setText(cell != null ? cell : "");
            cellView.setTextColor(getResources().getColor(android.R.color.black));
            cellView.setBackgroundResource(cell != null ? R.drawable.cell_background : R.drawable.cell_empty_background);
            cellView.setTextSize(20);
            cellView.setGravity(android.view.Gravity.CENTER);
            cellView.setPadding(10, 10, 10, 10);
            expressionLayout.addView(cellView);
        }
        expressionLayout.addView(addCellButton);
    }

    private void showShopFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ShopFragment())
                .addToBackStack(null)
                .commit();
    }

    private void showUpgradeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new UpgradeFragment())
                .addToBackStack(null)
                .commit();
    }

    private void showEditFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EditFragment())
                .addToBackStack(null)
                .commit();
    }

    private void showRebirthFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RebirthFragment())
                .addToBackStack(null)
                .commit();
    }

    public void showRebirthConfirmFragment() {
        RebirthConfirmFragment confirmFragment = new RebirthConfirmFragment();
        confirmFragment.show(getSupportFragmentManager(), "RebirthConfirmFragment");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}