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

public class MultichoiceAdpT extends RecyclerView.Adapter<MultichoiceAdpT.MultiChoiceViewHolder> {

    Context context;
    List<Room> optionList;
    static int count;
    public MultichoiceAdpT(Context context, List<Room> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.multitime,null);
        return new MultichoiceAdpT.MultiChoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolder holder, int position) {
        holder.checkBox.setText(optionList.get(position).Name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.timeeadd.add(buttonView.getText().toString());
                    count++;
                }
                else{
                    Common.timeeadd.remove(buttonView.getText().toString());
                    count--;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
    public int getCountcheck(){
        return count;
    }
    class MultiChoiceViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public MultiChoiceViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.chk_timeadd);

        }
    }
}
