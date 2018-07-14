package com.meteor.homework4.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meteor.homework4.activity.R;
import com.meteor.homework4.model.Person;

import java.util.LinkedList;

public class CustomRvAdapter extends RecyclerView.Adapter {
    private LinkedList<Person> personList;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView itemView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=(TextView) itemView;
        }
    }

    /*public static View.OnClickListener itemClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setVisibility(View.INVISIBLE);
            SystemVariableManager.getInstance().setRv_infoClickedItem((int) view.getTag());         //only need one clickListener for all items
        }
    };
    */

    public CustomRvAdapter() {
        super();
        this.personList=new LinkedList<Person>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        TextView itemView= (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item,
                                                          viewGroup,false);
        CustomViewHolder viewHolder=new CustomViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((CustomViewHolder)viewHolder).itemView.setText(this.personList.get(position).getInformation());
        viewHolder.itemView.setTag(viewHolder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void addItem(String name, String contactInfo) {
        Person person=new Person(name, contactInfo);
        personList.add(person);

        this.notifyItemInserted(personList.size()-1);
    }

    public void updateItem(int position, String name, String contactInfo) {
        Person person=personList.get(position);

        person.setName(name);
        person.setContactInfo(contactInfo);
        this.notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        this.personList.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, this.personList.size());
    }
}
