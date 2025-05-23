package com.example.mathgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class RebirthConfirmFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rebirth_confirm, container, false);

        TextView rebirthPointsText = view.findViewById(R.id.rebirth_points_gain_text);
        TextView lossInfoText = view.findViewById(R.id.loss_info_text);
        Button confirmButton = view.findViewById(R.id.confirm_rebirth_button);

        MainActivity activity = (MainActivity) getActivity();
        GameManager gameManager = activity.getGameManager();

        double points = gameManager.getPlayerData().getPoints();
        double rebirthPointsGain = Math.log10(points);
        rebirthPointsText.setText(String.format(Locale.US, "Вы получите: %.0f ОП", rebirthPointsGain));
        lossInfoText.setText("Вы потеряете: очки, цифры, знаки, клетки");

        confirmButton.setOnClickListener(v -> {
            gameManager.rebirth();
            dismiss();
            getFragmentManager().popBackStack(); // Возвращаемся к главному экрану
        });

        // Закрытие фрагмента при клике вне области
        view.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}