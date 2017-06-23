package com.mellis.expenseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mellis on 6/20/2017.
 */

public class ExpenseListAdapter extends BaseAdapter {

    private final ArrayList<Expense> expenseList;
    private final LayoutInflater layoutInflater;
    private final Context ctx;

    public ExpenseListAdapter(ArrayList<Expense> expenseList, Context ctx) {
        this.expenseList = expenseList;
        layoutInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Object getItem(int position) {
        return expenseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //This makes ListView the same as RecycleView because it reduces calls
        //to findViewById and allows us to keep the views fresh and clean
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.current_expense_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.expenseItemName);
            holder.tvAmount = (TextView) convertView.findViewById(R.id.expenseItemAmount);
            holder.tvDate = (TextView) convertView.findViewById(R.id.expenseItemDate);
            holder.editExpenseButton = (ImageButton) convertView.findViewById(R.id.imageButtonEditExpense);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(expenseList.get(position).getName());
        holder.tvAmount.setText("Cost: $"+expenseList.get(position).getAmount());
        holder.tvDate.setText("Date: "+expenseList.get(position).getDate());
        holder.editExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExpenseDetailsFragment.EditExpenseCallback)ctx).beginEditExpense(expenseList.get(position));
            }
        });
        return convertView;
    }
    //Represents all the widgets in a layout resouce file
    static class ViewHolder {
        TextView tvName;
        TextView tvAmount;
        TextView tvDate;
        ImageButton editExpenseButton;
    }
}
