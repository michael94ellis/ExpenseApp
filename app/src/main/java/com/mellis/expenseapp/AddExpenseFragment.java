package com.mellis.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddExpenseFragment extends Fragment {

    EditText etName, etAmount;
    Spinner etCategory;

    private AddExpenseCallback addExpenseCallback;

    public interface AddExpenseCallback {
        public void dispatchNewExpense(Expense newExpense);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        etName = (EditText) view.findViewById(R.id.etExpenseName);
        etAmount = (EditText) view.findViewById(R.id.etExpenseAmount);
        etCategory = (Spinner) view.findViewById(R.id.spinner);

        view.findViewById(R.id.submitExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if(!name.equals("")) {
                    String amount = etAmount.getText().toString();
                    if(!amount.equals("")) {
                    String category = etCategory.getSelectedItem().toString();
                    String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
                    addExpenseCallback.dispatchNewExpense(new Expense(name, category, amount, formattedDate));
                    getActivity().getSupportFragmentManager().beginTransaction().remove(AddExpenseFragment.this).commit();
                    }else{
                        Toast.makeText(getContext(),"Add an amount for the expense",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Add a name for the expense",Toast.LENGTH_SHORT).show();
                }
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
