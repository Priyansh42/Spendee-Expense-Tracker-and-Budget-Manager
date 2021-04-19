package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class ExpenseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    private RecyclerView recyclerView;

    private TextView expenseTotalSum;

    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    private Button btnUpdate;
    private Button btnDelete;

    private String type;
    private String note;
    private int amount;

    private  String post_key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_expense, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mAuth!=null) {
            String uid = mUser.getUid();

            mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        }

        expenseTotalSum=myview.findViewById(R.id.expense_txt_result);
        recyclerView=myview.findViewById(R.id.recycler_id_expense);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mExpenseDatabase.addValueEventListener(new ValueEventListener() {

            int totalvalue=0;

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot mysnapshot:dataSnapshot.getChildren()){
                    Data data=mysnapshot.getValue(Data.class);
                    totalvalue=totalvalue+data.getAmount();
                    String stTotalvalue=String.valueOf(totalvalue);
                    expenseTotalSum.setText(stTotalvalue+".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return  myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, ExpenseFragment.MyViewHolder> adapter= new FirebaseRecyclerAdapter<Data, ExpenseFragment.MyViewHolder>(

                Data.class,
                R.layout.expense_recycler_data,
                ExpenseFragment.MyViewHolder.class,
                mExpenseDatabase
        ) {
            @Override
            protected void populateViewHolder(ExpenseFragment.MyViewHolder myViewHolder, Data model, int position) {

                myViewHolder.setType(model.getType());
                myViewHolder.setNote(model.getNote());
                myViewHolder.setDate(model.getDate());
                myViewHolder.setAmount(model.getAmount());

                myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(position).getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }
        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }
        private void setAmount(int amount){
            TextView mAmount=mView.findViewById(R.id.amount_txt_expense);
            String stamount=String.valueOf(amount);
            mAmount.setText("-"+stamount);
        }
    }

    private void updateDataItem() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.update_data_item, null);
        mydialog.setView(myview);

        edtAmount = myview.findViewById(R.id.amount_edt);
        edtType = myview.findViewById(R.id.type_edt);
        edtNote = myview.findViewById(R.id.note_edt);

        //Set data to edit text..
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        btnUpdate = myview.findViewById(R.id.btn_upd_Update);
        btnDelete = myview.findViewById(R.id.btnuPD_Delete);

        AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = edtType.getText().toString().trim();
                note = edtNote.getText().toString().trim();

                String mdAmount = String.valueOf(amount);
                mdAmount = edtAmount.getText().toString().trim();

                int myAmount = Integer.parseInt(mdAmount);

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(myAmount, type, note, post_key, mDate);

                mExpenseDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpenseDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
