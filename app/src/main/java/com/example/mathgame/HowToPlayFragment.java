package com.example.mathgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class HowToPlayFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_to_play, container, false);
        TextView titleText = view.findViewById(R.id.how_to_play_title);
        TextView instructionsText = view.findViewById(R.id.instructions_text);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        titleText.setText("Как играть?");
        instructionsText.setText(
            "🛒 - магазин. Здесь можно купить ячейки с цифрами и знаками.\n" +
            "⏫ - улучшения. Здесь можно превратить ТРИ одинаковых числа в ОДНО, но на единицу больше.\n" +
            "✏ - редактирование. Перетягивайте ячейки с цифрами и знаками, чтобы изменить выражение.\n" +
            "🔄 - перерождение. При достижении определённого количества очков можно переродиться, чтобы получить очки перерождения (ОП), с помощью которых можно купить мощные улучшения.\n" +
            "Чтобы увеличить количество клеток, нажмите на кнопку в верху экрана с числом в квадратных скобках."
        );

        confirmButton.setBackgroundResource(R.drawable.button_enabled_background);
        confirmButton.setText("Понятно");
        confirmButton.setOnClickListener(v -> dismiss());

        return view;
    }
}