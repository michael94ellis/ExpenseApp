package com.mellis.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ExpenseListFragment extends Fragment {

    private static final String ARG_PARAM1 = "list";
    private static ArrayList<Expense> expenseList;
    private ExpenseListCallback expenseCallback;

    public interface ExpenseListCallback{
        public void addExpense();
        public void deleteExpense(Expense expense);
        public void displayExpenseDetails(Expense expense);
    }

    public ExpenseListFragment() {}

    public static ExpenseListFragment newInstance() {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, expenseList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             expenseList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);
        view.findViewById(R.id.addExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseCallback.addExpense();
                getActivity().getSupportFragmentManager().beginTransaction().remove(ExpenseListFragment.this).commit();
            }
        });

        ExpenseListAdapter adapter = new ExpenseListAdapter(expenseList, getContext());
        ListView listView = (ListView) view.findViewById(R.id.lvCurrentExpenses);
        if(expenseList != null && !expenseList.isEmpty()) {
            listView.setVisibility(View.VISIBLE);
            (view.findViewById(R.id.textView7)).setVisibility(View.GONE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    expenseCallback.displayExpenseDetails(expenseList.get(position));
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    expenseCallback.deleteExpense(expenseList.get(position));
                    return true;
                }
            });
        }else{
            listView.setVisibility(View.GONE);
            (view.findViewById(R.id.textView7)).setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExpenseListCallback) {
            expenseCallback = (ExpenseListCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        expenseCallback = null;
    }

    public void Log(String t){
        Log.d("ListFrag", t);
    }
}
