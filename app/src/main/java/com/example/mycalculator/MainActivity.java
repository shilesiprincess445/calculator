package com.example.mycalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvHistory, tvResult;
    private String currentInput = "";
    private String lastInput = "";
    private String operator = "";
    private boolean isNewOp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.activity.EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvHistory = findViewById(R.id.tvHistory);
        tvResult = findViewById(R.id.tvResult);

        setNumberListeners();
        setOperatorListeners();

        findViewById(R.id.btnAC).setOnClickListener(v -> {
            currentInput = "";
            lastInput = "";
            operator = "";
            tvHistory.setText("");
            tvResult.setText("0");
            isNewOp = true;
        });

        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            if (!currentInput.isEmpty() && !lastInput.isEmpty()) {
                calculate();
                tvHistory.setText(lastInput + " " + operator + " " + currentInput + " =");
                operator = "";
                isNewOp = true;
            }
        });

        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!currentInput.contains(".")) {
                if (currentInput.isEmpty()) {
                    currentInput = "0.";
                } else {
                    currentInput += ".";
                }
                tvResult.setText(currentInput);
            }
        });

        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            if (!currentInput.isEmpty()) {
                double val = Double.parseDouble(currentInput) / 100;
                currentInput = String.valueOf(val);
                tvResult.setText(formatResult(val));
            }
        });
    }

    private void setNumberListeners() {
        int[] ids = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        View.OnClickListener listener = v -> {
            MaterialButton btn = (MaterialButton) v;
            if (isNewOp) {
                currentInput = btn.getText().toString();
                isNewOp = false;
            } else {
                currentInput += btn.getText().toString();
            }
            tvResult.setText(currentInput);
        };
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorListeners() {
        int[] ids = {R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide, R.id.btnPower};
        View.OnClickListener listener = v -> {
            MaterialButton btn = (MaterialButton) v;
            if (!currentInput.isEmpty()) {
                if (!lastInput.isEmpty()) {
                    calculate();
                } else {
                    lastInput = currentInput;
                }
                operator = btn.getText().toString();
                tvHistory.setText(lastInput + " " + operator);
                isNewOp = true;
            }
        };
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void calculate() {
        double val1 = Double.parseDouble(lastInput);
        double val2 = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+": result = val1 + val2; break;
            case "-": result = val1 - val2; break;
            case "×": result = val1 * val2; break;
            case "÷":
                if (val2 != 0) result = val1 / val2;
                break;
            case "^": result = Math.pow(val1, val2); break;
        }

        lastInput = String.valueOf(result);
        tvResult.setText(formatResult(result));
    }

    private String formatResult(double d) {
        if (d == (long) d)
            return String.format(Locale.getDefault(), "%d", (long) d);
        else
            return String.valueOf(d);
    }
}
