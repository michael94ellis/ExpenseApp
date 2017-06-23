package com.mellis.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AddExpenseFragment extends Fragment {

    EditText etName, etAmount;
    TextView tvDate;
    String date;

    private AddExpenseCallback addExpenseCallback;


    public interface AddExpenseCallback {
        public void dispatchNewExpense(Expense newExpense);
    }

    public void setDateText(String date){
        tvDate.setText("Date: "+date);
        this.date=date;
    }

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        etName = (EditText) view.findViewById(R.id.etExpenseName);
        etAmount = (EditText) view.findViewById(R.id.etExpenseAmount);
        tvDate = (TextView) view.findViewById(R.id.tvAddExpenseDate);

        view.findViewById(R.id.submitExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (!name.equals("")) {
                    String amount = etAmount.getText().toString();
                    if (!amount.equals("")) {
                        if (!date.equals("")) {
                            addExpenseCallback.dispatchNewExpense(new Expense(name, amount, date));
                            getActivity().getSupportFragmentManager().beginTransaction().remove(AddExpenseFragment.this).commit();
                        } else {
                            Toast.makeText(getContext(), " Please select a date", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Add an amount for the expense", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Add a name for the expense", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.addExpenseDateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datePickerFrag = new DatePickerFragment();
                datePickerFrag.show(getActivity().getFragmentManager(), "DatePicker");
            }
        });

        view.findViewById(R.id.cancelNewExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddExpenseCallback) {
            addExpenseCallback = (AddExpenseCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addExpenseCallback = null;
    }


}
