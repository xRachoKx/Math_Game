package com.example.mathgame;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private double points;
    private ArrayList<String> expression;
    private HashMap<String, Integer> numbers;
    private HashMap<String, Integer> operators;
    private int rebirthPoints;
    private int startNumberLevel;
    private int powerLevel;
    private Context context;
    private static final String PREFS_NAME = "MathGamePrefs";
    private static final String LAST_EXIT_TIME = "last_exit_time";
    private static final String POINTS_KEY = "points";
    private static final String EXPRESSION_KEY = "expression";
    private static final String NUMBERS_KEY = "numbers_";
    private static final String OPERATORS_KEY = "operators_";
    private static final String REBIRTH_POINTS_KEY = "rebirth_points";
    private static final String START_NUMBER_LEVEL_KEY = "start_number_level";
    private static final String POWER_LEVEL_KEY = "power_level";
    private static final String NUMBER_COST_KEY = "number_cost";
    private static final String PLUS_COST_KEY = "plus_cost";
    private static final String MINUS_COST_KEY = "minus_cost";
    private static final String MULTIPLY_COST_KEY = "multiply_cost";
    private static final String DIVIDE_COST_KEY = "divide_cost";
    private static final String START_NUMBER_UPGRADE_COST_KEY = "start_number_upgrade_cost";
    private static final String POWER_UPGRADE_COST_KEY = "power_upgrade_cost";
    private static final String REBIRTH_REQUIREMENT_KEY = "rebirth_requirement";

    public PlayerData(Context context) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        points = prefs.getFloat(POINTS_KEY, 0);
        rebirthPoints = prefs.getInt(REBIRTH_POINTS_KEY, 0);
        startNumberLevel = prefs.getInt(START_NUMBER_LEVEL_KEY, 1);
        powerLevel = prefs.getInt(POWER_LEVEL_KEY, 0);
        numberCost = prefs.getFloat(NUMBER_COST_KEY, 5);
        plusCost = prefs.getFloat(PLUS_COST_KEY, 10);
        minusCost = prefs.getFloat(MINUS_COST_KEY, 10);
        multiplyCost = prefs.getFloat(MULTIPLY_COST_KEY, 50);
        divideCost = prefs.getFloat(DIVIDE_COST_KEY, 50);
        startNumberUpgradeCost = prefs.getInt(START_NUMBER_UPGRADE_COST_KEY, 1);
        powerUpgradeCost = prefs.getInt(POWER_UPGRADE_COST_KEY, 1);
        rebirthRequirement = prefs.getFloat(REBIRTH_REQUIREMENT_KEY, 1000);

        expression = new ArrayList<>();
        String expr = prefs.getString(EXPRESSION_KEY, "1");
        String[] exprArray = expr.split(",", -1); // -1 чтобы сохранить все пустые элементы
        for (String item : exprArray) {
            expression.add(item.isEmpty() ? null : item);
        }

        numbers = new HashMap<>();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(NUMBERS_KEY)) {
                String num = key.replace(NUMBERS_KEY, "");
                numbers.put(num, prefs.getInt(key, 0));
            }
        }

        operators = new HashMap<>();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(OPERATORS_KEY)) {
                String op = key.replace(OPERATORS_KEY, "");
                operators.put(op, prefs.getInt(key, 0));
            }
        }

        if (numbers.isEmpty()) {
            numbers.put("1", 1);
        }
        if (operators.isEmpty()) {
            operators.put("+", 0);
            operators.put("-", 0);
            operators.put("×", 0);
            operators.put("÷", 0);
        }
    }

    public void save(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(POINTS_KEY, (float) points);
        editor.putInt(REBIRTH_POINTS_KEY, rebirthPoints);
        editor.putInt(START_NUMBER_LEVEL_KEY, startNumberLevel);
        editor.putInt(POWER_LEVEL_KEY, powerLevel);
        editor.putFloat(NUMBER_COST_KEY, (float) numberCost);
        editor.putFloat(PLUS_COST_KEY, (float) plusCost);
        editor.putFloat(MINUS_COST_KEY, (float) minusCost);
        editor.putFloat(MULTIPLY_COST_KEY, (float) multiplyCost);
        editor.putFloat(DIVIDE_COST_KEY, (float) divideCost);
        editor.putInt(START_NUMBER_UPGRADE_COST_KEY, startNumberUpgradeCost);
        editor.putInt(POWER_UPGRADE_COST_KEY, powerUpgradeCost);
        editor.putFloat(REBIRTH_REQUIREMENT_KEY, (float) rebirthRequirement);

        StringBuilder exprBuilder = new StringBuilder();
        for (String item : expression) {
            exprBuilder.append(item != null ? item : "").append(",");
        }
        // Удаляем последнюю запятую, если есть
        String exprString = exprBuilder.length() > 0 ? exprBuilder.substring(0, exprBuilder.length() - 1) : "1";
        editor.putString(EXPRESSION_KEY, exprString);

        for (Map.Entry<String, Integer> entry : numbers.entrySet()) {
            editor.putInt(NUMBERS_KEY + entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : operators.entrySet()) {
            editor.putInt(OPERATORS_KEY + entry.getKey(), entry.getValue());
        }

        editor.putLong(LAST_EXIT_TIME, System.currentTimeMillis());
        editor.apply();
    }

    public long getLastExitTime() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(LAST_EXIT_TIME, 0);
    }

    public void addPoints(double amount) {
        points += amount;
    }

    public double getPoints() {
        return points;
    }

    public ArrayList<String> getExpression() {
        return expression;
    }

    public void addNumber(String number) {
        numbers.put(number, numbers.getOrDefault(number, 0) + 1);
    }

    public void addOperator(String operator) {
        operators.put(operator, operators.getOrDefault(operator, 0) + 1);
    }

    public int getNumberCount(String number) {
        return numbers.getOrDefault(number, 0);
    }

    public int getOperatorCount(String operator) {
        return operators.getOrDefault(operator, 0);
    }

    public void removeNumbers(String number, int count) {
        int current = numbers.getOrDefault(number, 0);
        if (current >= count) {
            numbers.put(number, current - count);
        }
    }

    public void returnItem(String item) {
        if (item.matches("[+\\-×÷]")) {
            int count = operators.getOrDefault(item, 0);
            operators.put(item, count);
        } else {
            int count = numbers.getOrDefault(item, 0);
            numbers.put(item, count);
        }
    }

    public void reset() {
        points = 0;
        expression.clear();
        expression.add(String.valueOf(startNumberLevel));
        numbers.clear();
        numbers.put(String.valueOf(startNumberLevel), 1);
        operators.clear();
    }

    public void addRebirthPoints(int amount) {
        rebirthPoints += amount;
    }

    public int getRebirthPoints() {
        return rebirthPoints;
    }

    public void spendRebirthPoints(int amount) {
        rebirthPoints -= amount;
    }

    public int getStartNumberLevel() {
        return startNumberLevel;
    }

    public void increaseStartNumberLevel() {
        startNumberLevel++;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void increasePowerLevel() {
        powerLevel++;
    }

    public HashMap<String, Integer> getNumbers() {
        return numbers;
    }

    public HashMap<String, Integer> getOperators() {
        return operators;
    }

    public int getMaxNumber() {
        int max = 1;
        for (String number : numbers.keySet()) {
            max = Math.max(max, Integer.parseInt(number));
        }
        return max;
    }

    protected double numberCost = 5;
    protected double plusCost = 10;
    protected double minusCost = 10;
    protected double multiplyCost = 50;
    protected double divideCost = 50;
    protected int startNumberUpgradeCost = 1;
    protected int powerUpgradeCost = 1;
    protected double rebirthRequirement = 1000;

    public double getNumberCost() { return numberCost; }
    public double getPlusCost() { return plusCost; }
    public double getMinusCost() { return minusCost; }
    public double getMultiplyCost() { return multiplyCost; }
    public double getDivideCost() { return divideCost; }
    public int getStartNumberUpgradeCost() { return startNumberUpgradeCost; }
    public int getPowerUpgradeCost() { return powerUpgradeCost; }
    public double getRebirthRequirement() { return rebirthRequirement; }
}