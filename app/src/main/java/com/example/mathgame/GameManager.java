package com.example.mathgame;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;

public class GameManager {
    private MainActivity activity;
    private PlayerData playerData;

    public GameManager(MainActivity activity) {
        this.activity = activity;
        this.playerData = new PlayerData(activity);
    }

    public void updatePoints() {
        double pointsPerSecond = calculatePointsPerSecond();
        playerData.addPoints(pointsPerSecond);
        activity.updateUI();
    }

    public double calculatePointsPerSecond() {
        ArrayList<String> expression = playerData.getExpression();
        if (expression.isEmpty()) return 0;

        double result = 0;
        String operator = "+";
        boolean isNumberExpected = true;

        for (String cell : expression) {
            if (cell == null) continue;

            if (isNumberExpected) {
                try {
                    double num = Double.parseDouble(cell);
                    switch (operator) {
                        case "+": result += num; break;
                        case "-": result -= num; break;
                        case "×": result *= num; break;
                        case "÷": result /= (num != 0 ? num : 1); break;
                    }
                    isNumberExpected = false;
                } catch (NumberFormatException e) {
                    return 0;
                }
            } else {
                if (cell.equals("+") || cell.equals("-") || cell.equals("×") || cell.equals("÷")) {
                    operator = cell;
                    isNumberExpected = true;
                } else {
                    return 0;
                }
            }
        }

        int powerLevel = playerData.getPowerLevel();
        if (powerLevel > 0) {
            result = Math.pow(result, 1 + powerLevel * 0.1);
        }

        return Math.max(0, result);
    }

    public void addCell() {
        int cellCount = playerData.getExpression().size();
        double cost = 5 * Math.pow(1.5, cellCount);
        if (playerData.getPoints() >= cost) {
            playerData.addPoints(-cost);
            playerData.getExpression().add(null);
            activity.updateUI();
            Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof EditFragment) {
                ((EditFragment) currentFragment).updateExpressionLayout();
            }
        }
    }

    public void buyNumber(int number, int quantity) {
        double cost = 0;
        double baseCost = playerData.getNumberCost();
        for (int i = 0; i < quantity; i++) {
            cost += baseCost;
            baseCost += 2;
        }
        if (playerData.getPoints() >= cost) {
            playerData.addPoints(-cost);
            for (int i = 0; i < quantity; i++) {
                playerData.addNumber(String.valueOf(number));
            }
            playerData.numberCost = baseCost;
            activity.updateUI();
        }
    }

    public void buyOperator(String operator, int quantity) {
        double cost = 0;
        double baseCost = 0;
        if (operator.equals("+")) {
            baseCost = playerData.getPlusCost();
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost += 5;
            }
        } else if (operator.equals("-")) {
            baseCost = playerData.getMinusCost();
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost += 5;
            }
        } else if (operator.equals("×")) {
            baseCost = playerData.getMultiplyCost();
            int count = playerData.getOperatorCount(operator);
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost = playerData.getMultiplyCost() * (count + i + 2);
            }
        } else if (operator.equals("÷")) {
            baseCost = playerData.getDivideCost();
            int count = playerData.getOperatorCount(operator);
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost = playerData.getDivideCost() * (count + i + 2);
            }
        }

        if (playerData.getPoints() >= cost) {
            playerData.addPoints(-cost);
            for (int i = 0; i < quantity; i++) {
                playerData.addOperator(operator);
            }
            if (operator.equals("+")) playerData.plusCost = baseCost;
            else if (operator.equals("-")) playerData.minusCost = baseCost;
            else if (operator.equals("×")) playerData.multiplyCost = baseCost;
            else if (operator.equals("÷")) playerData.divideCost = baseCost;
            activity.updateUI();
        }
    }

    public void upgradeNumber(String number, int quantity) {
        int count = playerData.getNumberCount(number);
        ArrayList<String> expression = playerData.getExpression();
        for (String cell : expression) {
            if (cell != null && cell.equals(number)) {
                count--;
            }
        }
        int upgrades = Math.min(quantity, count / 3);
        for (int i = 0; i < upgrades; i++) {
            playerData.removeNumbers(number, 3);
            int newNumber = Integer.parseInt(number) + 1;
            playerData.addNumber(String.valueOf(newNumber));
        }
        activity.updateUI();
    }

    public void rebirth() {
        double points = playerData.getPoints();
        int rebirthPoints = (int) Math.floor(Math.log10(points));
        playerData.addRebirthPoints(rebirthPoints);
        playerData.reset();
        playerData.rebirthRequirement *= 1.5; // +50%
        playerData.numberCost = 5;
        playerData.plusCost = 10;
        playerData.minusCost = 10;
        playerData.multiplyCost = 50;
        playerData.divideCost = 50;
        activity.updateUI();
    }

    public void upgradeStartNumber() {
        if (playerData.getRebirthPoints() >= playerData.getStartNumberUpgradeCost()) {
            playerData.spendRebirthPoints(playerData.getStartNumberUpgradeCost());
            playerData.increaseStartNumberLevel();
            playerData.startNumberUpgradeCost += 1;
            activity.updateUI();
        }
    }

    public void upgradePower() {
        if (playerData.getRebirthPoints() >= playerData.getPowerUpgradeCost()) {
            playerData.spendRebirthPoints(playerData.getPowerUpgradeCost());
            playerData.increasePowerLevel();
            playerData.powerUpgradeCost += 1;
            activity.updateUI();
        }
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public double getRebirthRequirement() {
        return playerData.getRebirthRequirement();
    }

    public double getCellCost() {
        int cellCount = playerData.getExpression().size();
        return 5 * Math.pow(1.5, cellCount);
    }

    public double getNumberCost(int quantity) {
        double cost = 0;
        double baseCost = playerData.getNumberCost();
        for (int i = 0; i < quantity; i++) {
            cost += baseCost;
            baseCost += 2;
        }
        return cost;
    }

    public double getOperatorCost(String operator, int quantity) {
        double cost = 0;
        double baseCost = 0;
        if (operator.equals("+")) {
            baseCost = playerData.getPlusCost();
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost += 5;
            }
        } else if (operator.equals("-")) {
            baseCost = playerData.getMinusCost();
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost += 5;
            }
        } else if (operator.equals("×")) {
            baseCost = playerData.getMultiplyCost();
            int count = playerData.getOperatorCount(operator);
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost = playerData.getMultiplyCost() * (count + i + 2);
            }
        } else if (operator.equals("÷")) {
            baseCost = playerData.getDivideCost();
            int count = playerData.getOperatorCount(operator);
            for (int i = 0; i < quantity; i++) {
                cost += baseCost;
                baseCost = playerData.getDivideCost() * (count + i + 2);
            }
        }
        return cost;
    }

    public int getMaxBuyableNumbers() {
        double points = playerData.getPoints();
        int count = 0;
        double cost = playerData.getNumberCost();
        double totalCost = 0;
        while (totalCost + cost <= points) {
            totalCost += cost;
            cost += 2;
            count++;
        }
        return count;
    }

    public int getMaxBuyableOperators(String operator) {
        double points = playerData.getPoints();
        int count = 0;
        double cost = 0;
        double baseCost = 0;
        if (operator.equals("+")) {
            baseCost = playerData.getPlusCost();
            cost = baseCost;
        } else if (operator.equals("-")) {
            baseCost = playerData.getMinusCost();
            cost = baseCost;
        } else if (operator.equals("×")) {
            baseCost = playerData.getMultiplyCost();
            cost = baseCost;
            int currentCount = playerData.getOperatorCount(operator);
            cost = baseCost * (currentCount + count + 1);
        } else if (operator.equals("÷")) {
            baseCost = playerData.getDivideCost();
            cost = baseCost;
            int currentCount = playerData.getOperatorCount(operator);
            cost = baseCost * (currentCount + count + 1);
        }

        double totalCost = 0;
        while (totalCost + cost <= points) {
            totalCost += cost;
            if (operator.equals("+") || operator.equals("-")) {
                cost += 5;
            } else {
                int currentCount = playerData.getOperatorCount(operator);
                cost = (operator.equals("×") ? playerData.getMultiplyCost() : playerData.getDivideCost()) * (currentCount + count + 2);
            }
            count++;
        }
        return count;
    }

    public int getStartNumberUpgradeCost() {
        return playerData.getStartNumberUpgradeCost();
    }

    public int getPowerUpgradeCost() {
        return playerData.getPowerUpgradeCost();
    }

    public double calculateOfflinePoints(long offlineTimeSeconds) {
        return calculatePointsPerSecond() * offlineTimeSeconds;
    }
}