package com.example.bitcoinpriceindex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TransactionInfo> transactionsInfo;

    public RecyclerViewAdapter(ArrayList<TransactionInfo> transactionsInfo) {
        this.transactionsInfo = transactionsInfo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transactions_layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.transactionTextView.setText((i + 1) + ". Amount of BTC: " + transactionsInfo.get(i).getBTCAmount() + "\n"
            + "Time: " + transactionsInfo.get(i).getDate() + "\n"
            + "Type: " + transactionsInfo.get(i).getType());
        holder.transactionRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isExpanded) {
                    holder.isExpanded = false;
                    holder.transactionTextView.setText((i + 1) + ". Amount of BTC: " + transactionsInfo.get(i).getBTCAmount() + "\n"
                            + "Time: " + transactionsInfo.get(i).getDate() + "\n"
                            + "Type: " + transactionsInfo.get(i).getType());
                } else {
                    holder.isExpanded = true;
                    holder.transactionTextView.setText(holder.transactionTextView.getText() + "\n"
                        + "Exchange: 1 \u20BF = " + transactionsInfo.get(i).getBTCPrice() + "$\n"
                        + "Transactoin ID: " + transactionsInfo.get(i).getTransactionID() + "\n"
                        + "Sum: " + transactionsInfo.get(i).getSum() + "$");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transactionTextView;
        RelativeLayout transactionRelativeLayout;
        boolean isExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionTextView = (TextView) itemView.findViewById(R.id.transactionTextView);
            transactionRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.transactionRelativeLayout);
        }

    }
}
