package com.example.mathgame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EditFragment extends Fragment {
    private LinearLayout expressionLayout, numbersLayout, operatorsLayout;
    private HorizontalScrollView expressionScrollView;
    private MainActivity activity;
    private GameManager gameManager;
    private ArrayList<String> expression;
    private int scrollXPosition = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        expressionScrollView = view.findViewById(R.id.edit_expression_layout);
        expressionLayout = view.findViewById(R.id.inner_expression_layout);
        numbersLayout = view.findViewById(R.id.numbers_layout).findViewById(R.id.inner_numbers_layout);
        operatorsLayout = view.findViewById(R.id.operators_layout).findViewById(R.id.inner_operators_layout);
        Button clearButton = view.findViewById(R.id.clear_button);

        activity = (MainActivity) getActivity();
        gameManager = activity.getGameManager();
        expression = gameManager.getPlayerData().getExpression();

        updateExpressionLayout();

        HashMap<String, Integer> availableNumbers = new HashMap<>();
        HashMap<String, Integer> availableOperators = new HashMap<>();
        for (Map.Entry<String, Integer> entry : gameManager.getPlayerData().getNumbers().entrySet()) {
            availableNumbers.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : gameManager.getPlayerData().getOperators().entrySet()) {
            availableOperators.put(entry.getKey(), entry.getValue());
        }
        for (String cell : expression) {
            if (cell != null) {
                if (cell.matches("[+\\-×÷]")) {
                    int count = availableOperators.getOrDefault(cell, 0);
                    if (count > 0) {
                        availableOperators.put(cell, count - 1);
                    }
                } else {
                    int count = availableNumbers.getOrDefault(cell, 0);
                    if (count > 0) {
                        availableNumbers.put(cell, count - 1);
                    }
                }
            }
        }

        ArrayList<Map.Entry<String, Integer>> sortedNumbers = new ArrayList<>(availableNumbers.entrySet());
        Collections.sort(sortedNumbers, (a, b) -> Integer.compare(Integer.parseInt(b.getKey()), Integer.parseInt(a.getKey())));
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        int numberCount = 0;
        for (Map.Entry<String, Integer> entry : sortedNumbers) {
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                if (count <= 0) continue;
                if (numberCount > 0 && numberCount % 6 == 0) {
                    numbersLayout.addView(row);
                    row = new LinearLayout(getContext());
                    row.setOrientation(LinearLayout.HORIZONTAL);
                }
                TextView numberView = new TextView(getContext());
                numberView.setText(entry.getKey());
                numberView.setTextColor(getResources().getColor(android.R.color.black));
                numberView.setBackgroundResource(R.drawable.cell_background);
                numberView.setTextSize(20);
                numberView.setGravity(android.view.Gravity.CENTER);
                numberView.setPadding(10, 10, 10, 10);
                numberView.setOnTouchListener((v, event) -> {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        android.content.ClipData clipData = android.content.ClipData.newPlainText("", entry.getKey());
                        v.startDragAndDrop(clipData, new View.DragShadowBuilder(v) {
                            @Override
                            public void onProvideShadowMetrics(android.graphics.Point shadowSize, android.graphics.Point shadowTouchPoint) {
                                super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
                                shadowSize.set(v.getWidth(), v.getHeight());
                                shadowTouchPoint.set(v.getWidth() / 2, v.getHeight() / 2);
                            }
                        }, v, 0);
                        v.setBackgroundResource(R.drawable.cell_background_dragged);
                    } else if (event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
                        v.setBackgroundResource(R.drawable.cell_background);
                    }
                    return true;
                });
                row.addView(numberView);
                numberCount++;
            }
        }
        if (numberCount > 0) {
            numbersLayout.addView(row);
        }

        row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        int operatorCount = 0;
        for (Map.Entry<String, Integer> entry : availableOperators.entrySet()) {
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                if (count <= 0) continue;
                if (operatorCount > 0 && operatorCount % 6 == 0) {
                    operatorsLayout.addView(row);
                    row = new LinearLayout(getContext());
                    row.setOrientation(LinearLayout.HORIZONTAL);
                }
                TextView operatorView = new TextView(getContext());
                operatorView.setText(entry.getKey());
                operatorView.setTextColor(getResources().getColor(android.R.color.black));
                operatorView.setBackgroundResource(R.drawable.cell_background);
                operatorView.setTextSize(20);
                operatorView.setGravity(android.view.Gravity.CENTER);
                operatorView.setPadding(10, 10, 10, 10);
                operatorView.setOnTouchListener((v, event) -> {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        android.content.ClipData clipData = android.content.ClipData.newPlainText("", entry.getKey());
                        v.startDragAndDrop(clipData, new View.DragShadowBuilder(v) {
                            @Override
                            public void onProvideShadowMetrics(android.graphics.Point shadowSize, android.graphics.Point shadowTouchPoint) {
                                super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
                                shadowSize.set(v.getWidth(), v.getHeight());
                                shadowTouchPoint.set(v.getWidth() / 2, v.getHeight() / 2);
                            }
                        }, v, 0);
                        v.setBackgroundResource(R.drawable.cell_background_dragged);
                    } else if (event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
                        v.setBackgroundResource(R.drawable.cell_background);
                    }
                    return true;
                });
                row.addView(operatorView);
                operatorCount++;
            }
        }
        if (operatorCount > 0) {
            operatorsLayout.addView(row);
        }

        clearButton.setOnClickListener(v -> {
            for (int i = 0; i < expression.size(); i++) {
                expression.set(i, null);
            }
            updateExpressionLayout();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditFragment()).commit();
        });

        expressionScrollView.post(() -> expressionScrollView.setScrollX(scrollXPosition));

        return view;
    }

    public void updateExpressionLayout() {
        expressionLayout.removeAllViews();
        for (int i = 0; i < expression.size(); i++) {
            TextView cellView = new TextView(getContext());
            final int index = i;
            cellView.setText(expression.get(i) != null ? expression.get(i) : "");
            cellView.setTextColor(getResources().getColor(android.R.color.black));
            cellView.setBackgroundResource(expression.get(i) != null ? R.drawable.cell_background : R.drawable.cell_empty_background);
            cellView.setTextSize(20);
            cellView.setGravity(android.view.Gravity.CENTER);
            cellView.setPadding(10, 10, 10, 10);
            cellView.setOnDragListener((v, event) -> {
                if (event.getAction() == android.view.DragEvent.ACTION_DROP) {
                    String data = event.getClipData().getItemAt(0).getText().toString();
                    if (expression.get(index) != null) {
                        gameManager.getPlayerData().returnItem(expression.get(index));
                    }
                    expression.set(index, data);
                    ((View) event.getLocalState()).setVisibility(View.GONE);
                    cellView.setText(data);
                    cellView.setBackgroundResource(R.drawable.cell_background);
                    scrollXPosition = expressionScrollView.getScrollX();
                    activity.updateUI();
                    EditFragment newFragment = new EditFragment();
                    newFragment.setScrollXPosition(scrollXPosition);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).commit();
                }
                expressionScrollView.post(() -> expressionScrollView.setScrollY(scrollXPosition));

                return true;
            });
            expressionLayout.addView(cellView);
        }
    }
    public void setScrollXPosition(int position) {
        this.scrollXPosition = position;
    }
}