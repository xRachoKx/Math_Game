package com.example.mathgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ShopFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        LinearLayout shopLayout = view.findViewById(R.id.shop_layout);

        MainActivity activity = (MainActivity) getActivity();
        GameManager gameManager = activity.getGameManager();
        int startNumber = gameManager.getPlayerData().getStartNumberLevel();

        LinearLayout numberLayout = new LinearLayout(getContext());
        numberLayout.setOrientation(LinearLayout.HORIZONTAL);
        numberLayout.setPadding(0, 8, 0, 8);

        TextView numberView = new TextView(getContext());
        numberView.setText(String.valueOf(startNumber));
        numberView.setTextColor(getResources().getColor(android.R.color.black));
        numberView.setBackgroundResource(R.drawable.cell_background);
        numberView.setTextSize(20);
        numberView.setGravity(android.view.Gravity.CENTER);
        numberView.setPadding(10, 10, 10, 10);
        numberLayout.addView(numberView);

        double points = gameManager.getPlayerData().getPoints();
        Button buy1Button = new Button(getContext());
        double cost1 = gameManager.getNumberCost(1);
        buy1Button.setText(String.format("x1 (%.0f)", cost1));
        buy1Button.setBackgroundResource(points >= cost1 ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
        buy1Button.setPadding(5, 3, 5, 3);
        buy1Button.setOnClickListener(v -> {
            gameManager.buyNumber(startNumber, 1);
        });
        numberLayout.addView(buy1Button);

        Button buy5Button = new Button(getContext());
        double cost5 = gameManager.getNumberCost(5);
        buy5Button.setText(String.format("x5 (%.0f)", cost5));
        buy5Button.setBackgroundResource(points >= cost5 ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
        buy5Button.setPadding(5, 3, 5, 3);
        buy5Button.setOnClickListener(v -> {
            gameManager.buyNumber(startNumber, 5);
        });
        numberLayout.addView(buy5Button);

        Button buyMaxButton = new Button(getContext());
        int max = gameManager.getMaxBuyableNumbers();
        double costMax = gameManager.getNumberCost(max);
        buyMaxButton.setText(String.format("max (%.0f)", costMax));
        buyMaxButton.setBackgroundResource(points >= costMax ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
        buyMaxButton.setPadding(5, 3, 5, 3);
        buyMaxButton.setOnClickListener(v -> {
            gameManager.buyNumber(startNumber, max);
        });
        numberLayout.addView(buyMaxButton);

        shopLayout.addView(numberLayout);

        String[] operators = {"+", "-", "×", "÷"};
        for (String op : operators) {
            LinearLayout operatorLayout = new LinearLayout(getContext());
            operatorLayout.setOrientation(LinearLayout.HORIZONTAL);
            operatorLayout.setPadding(0, 8, 0, 8);

            TextView operatorView = new TextView(getContext());
            operatorView.setText(op);
            operatorView.setTextColor(getResources().getColor(android.R.color.black));
            operatorView.setBackgroundResource(R.drawable.cell_background);
            operatorView.setTextSize(20);
            operatorView.setGravity(android.view.Gravity.CENTER);
            operatorView.setPadding(10, 10, 10, 10);
            operatorLayout.addView(operatorView);

            Button opBuy1Button = new Button(getContext());
            double opCost1 = gameManager.getOperatorCost(op, 1);
            opBuy1Button.setText(String.format("x1 (%.0f)", opCost1));
            opBuy1Button.setBackgroundResource(points >= opCost1 ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
            opBuy1Button.setPadding(5, 3, 5, 3);
            opBuy1Button.setOnClickListener(v -> {
                gameManager.buyOperator(op, 1);
            });
            operatorLayout.addView(opBuy1Button);

            Button opBuy5Button = new Button(getContext());
            double opCost5 = gameManager.getOperatorCost(op, 5);
            opBuy5Button.setText(String.format("x5 (%.0f)", opCost5));
            opBuy5Button.setBackgroundResource(points >= opCost5 ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
            opBuy5Button.setPadding(5, 3, 5, 3);
            opBuy5Button.setOnClickListener(v -> {
                gameManager.buyOperator(op, 5);
            });
            operatorLayout.addView(opBuy5Button);

            if (!op.equals("×") && !op.equals("÷")) {
                Button opBuyMaxButton = new Button(getContext());
                int opMax = gameManager.getMaxBuyableOperators(op);
                double opCostMax = gameManager.getOperatorCost(op, opMax);
                opBuyMaxButton.setText(String.format("max (%.0f)", opCostMax));
                opBuyMaxButton.setBackgroundResource(points >= opCostMax ? R.drawable.button_enabled_background : R.drawable.button_disabled_background);
                opBuyMaxButton.setPadding(5, 3, 5, 3);
                opBuyMaxButton.setOnClickListener(v -> {
                    gameManager.buyOperator(op, opMax);
                });
                operatorLayout.addView(opBuyMaxButton);
            }

            shopLayout.addView(operatorLayout);
        }

        return view;
    }
}