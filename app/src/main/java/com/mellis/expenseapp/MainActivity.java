package com.mellis.expenseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditExpenseFragment.ExpenseEditCompleteCallback, ExpenseDetailsFragment.EditExpenseCallback, ExpenseListFragment.ExpenseListCallback, AddExpenseFragment.AddExpenseCallback {
    private static final String LOGNAME = "Main";
    FragmentManager fm;
    static ArrayList<Expense> expenseList;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("expenses");
        expenseList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                expenseList.clear();
                for(DataSnapshot node: dataSnapshot.getChildren()){
                    Log.d(LOGNAME, "Expense is: " + node);
                    String name = (String) node.child("name").getValue();
                    String date = (String) node.child("date").getValue();
                    String category = (String) node.child("category").getValue();
                    String amount = node.child("amount").getValue().toString();
                    expenseList.add(new Expense(name,category,amount,date));
                }
                openListFragment();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(LOGNAME, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void addExpense() {
        FragmentTransaction ft = fm.beginTransaction();
        AddExpenseFragment a = (AddExpenseFragment) fm.findFragmentByTag("AddExpenseFragment");
        a = new AddExpenseFragment();
        ft.replace(R.id.mainLayout, a, "AddExpenseFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void deleteExpense(final Expense expense) {
        expenseList.remove(expense);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot node: dataSnapshot.getChildren()){
                    Log.d(LOGNAME, "Expense is: " + node);
                    String name = (String) node.child("name").getValue();
                    String date = (String) node.child("date").getValue();
                    String category = (String) node.child("category").getValue();
                    String amount = node.child("amount").getValue().toString();
                    Expense e = new Expense(name,category,amount,date);
                    if (e.equals(expense)){
                       node.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        openListFragment();
    }

    @Override
    public void displayExpenseDetails(Expense expense) {
        FragmentTransaction ft = fm.beginTransaction();
        ExpenseDetailsFragment a = new ExpenseDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("details", expense);
        a.setArguments(bundle);
        ft.replace(R.id.mainLayout, a, "ExpenseDetailsFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void dispatchNewExpense(final Expense expense) {
        expenseList.add(expense);
        Log("Pushing new expense");
        myRef.push().setValue(expense);
        openListFragment();
    }

    private void openListFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        Log("Creating new List Frag");
        ExpenseListFragment f = new ExpenseListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", expenseList);
        f.setArguments(bundle);
        ft.replace(R.id.mainLayout, f, "ExpenseListFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void Log(String text) {
        Log.d(LOGNAME, text);
    }

    @Override
    public void beginEditExpense(Expense expense) {
        FragmentTransaction ft = fm.beginTransaction();
        EditExpenseFragment a = new EditExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("edit", expense);
        a.setArguments(bundle);
        ft.replace(R.id.mainLayout, a, "EditExpenseFrag");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean editExpenseComplete() {

        return false;
    }
}
