package com.example.asus.bookingreal.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.asus.bookingreal.Model.Room;
import com.example.asus.bookingreal.R;
import com.example.asus.bookingreal.Util.Common;

import java.util.List;

public class MultichoiceAdp extends  RecyclerView.Adapter<MultichoiceAdp.MultiChoiceViewHolder>{

    Context context;
    List<Room> optionList;

    public MultichoiceAdp(Context context, List<Room> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.multichoice,null);
        return new MultiChoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolder holder, final int position) {
        holder.checkBox.setText(optionList.get(position).Name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.additionaladd.add(buttonView.getText().toString());
                }
                else{
                    Common.additionaladd.remove(buttonView.getText().toString());

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    class MultiChoiceViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public MultiChoiceViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.chk_add);
        }
    }
}
