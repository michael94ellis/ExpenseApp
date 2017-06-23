package com.mellis.expenseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditExpenseFragment extends Fragment {
    private static final String ARG_PARAM1 = "edit";
    Expense expense;
    EditText etName, etAmount;
    Spinner spinner;

    ExpenseEditCompleteCallback expenseEditCompleteCallback;
    AddExpenseFragment.AddExpenseCallback addExpenseCallback;
    ExpenseListFragment.ExpenseListCallback expenseListCallback;

    public interface ExpenseEditCompleteCallback{
        public boolean editExpenseComplete();
    }
    public EditExpenseFragment() {
        // Required empty public constructor
    }

    public static EditExpenseFragment newInstance(String param1, String param2) {
        EditExpenseFragment fragment = new EditExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.expense = getArguments().getParcelable(ARG_PARAM1);
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("expenses");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot node: dataSnapshot.getChildren()){
                    String name = (String) node.child("name").getValue();
                    String date = (String) node.child("date").getValue();
                    String category = (String) node.child("category").getValue();
                    String amount = node.child("amount").getValue().toString();
                    Expense e = new Expense(name,category,amount,date);
                    if (e.equals(expense)){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_expense, container, false);

        view.findViewById(R.id.cancelNewExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.submitExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                if(!name.equals("")) {
                    String amount = etAmount.getText().toString();
                    if(!amount.equals("")) {
                        expenseListCallback.deleteExpense(expense);
                        String category = spinner.getSelectedItem().toString();
                        String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
                        addExpenseCallback.dispatchNewExpense(new Expense(name, category, amount, formattedDate));
                        getActivity().getSupportFragmentManager().beginTransaction().remove(EditExpenseFragment.this).commit();
                    }else{
                        Toast.makeText(getContext(),"Add an amount for the expense",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Add a name for the expense",Toast.LENGTH_SHORT).show();
                }
            }
        });
        etName = ((EditText)view.findViewById(R.id.etExpenseName));
        etAmount =  ((EditText)view.findViewById(R.id.etExpenseAmount));
        etName.setText(expense.getName());
        etAmount.setText(expense.getAmount());
        spinner = ((Spinner)view.findViewById(R.id.spinner));
        String compareValue = expense.getCategory();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExpenseEditCompleteCallback) {
            expenseEditCompleteCallback = (ExpenseEditCompleteCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ExpenseEditCompleteCallback");
        }
        if (context instanceof AddExpenseFragment.AddExpenseCallback) {
            addExpenseCallback = (AddExpenseFragment.AddExpenseCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement addExpenseCallback");
        }
        if (context instanceof ExpenseListFragment.ExpenseListCallback) {
            expenseListCallback = (ExpenseListFragment.ExpenseListCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement addExpenseCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
