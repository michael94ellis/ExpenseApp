package com.mellis.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExpenseDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "details";
    private Expense expense;

    EditExpenseCallback editExpenseCallback;

    public interface EditExpenseCallback {
        public void beginEditExpense(Expense expense);
    }

    public ExpenseDetailsFragment() {
    }

    public static ExpenseDetailsFragment newInstance(Expense expense) {
        ExpenseDetailsFragment fragment = new ExpenseDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.expense = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_details, container, false);

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editExpenseCallback.beginEditExpense(expense);
            }
        });

        ((TextView) view.findViewById(R.id.nameDetail)).setText("Name: " + expense.getName());
        ((TextView) view.findViewById(R.id.amountDetail)).setText("Amount: $" + expense.getAmount());
        ((TextView) view.findViewById(R.id.dateDetail)).setText("Date: " + expense.getDate());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof EditExpenseCallback) {
            editExpenseCallback = (EditExpenseCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditExpenseCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
