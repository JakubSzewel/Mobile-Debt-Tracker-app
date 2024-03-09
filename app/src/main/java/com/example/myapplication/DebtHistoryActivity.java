package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class DebtHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_history);

        RecyclerView recyclerViewDebtHistory = findViewById(R.id.recyclerViewDebtHistory);
        recyclerViewDebtHistory.setLayoutManager(new LinearLayoutManager(this));

        List<DebtEntry> debtHistory = (List<DebtEntry>) getIntent().getSerializableExtra("debtHistory");

        assert debtHistory != null;
        Collections.reverse(debtHistory);
        DebtHistoryAdapter debtHistoryAdapter = new DebtHistoryAdapter(debtHistory);
        recyclerViewDebtHistory.setAdapter(debtHistoryAdapter);
    }
}
