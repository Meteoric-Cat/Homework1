package com.meteor.homework4.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meteor.homework4.activity.R;
import com.meteor.homework4.model.Person;

import java.util.LinkedList;

public class CustomRvAdapter extends RecyclerView.Adapter {
    private LinkedList<Person> personList;
    private LinkedList<CustomViewHolder> holderList;
    private boolean displayCheck;
    public CheckBoxListener checkboxListener;

    private static final short NOT_DISPLAY = 0;
    private static final short DISPLAY_NOT_CHECKED = 1;
    private static final short DISPLAY_AND_CHECKED = 2;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout itemView;
        public TextView tvName, tvContactNumber;
        public CheckBox cbChoice;
        public int dataID;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = (RelativeLayout) itemView;
            this.tvName = this.itemView.findViewById(R.id.tv_Name);
            this.tvContactNumber = this.itemView.findViewById(R.id.tv_ContactNumber);
            this.cbChoice = this.itemView.findViewById(R.id.cb_Choice);
        }
    }

    //private static View.OnClickListener checkboxesListener = new View.OnClickListener() {
    //    @Override
    //    public void onClick(View view) {
    //        ((CheckBox) view).setChecked(!((CheckBox) view).isChecked());
    //    }
    //};

    public static class CheckBoxListener implements View.OnClickListener {
        public LinkedList<Short> checkboxState;
        public int clickedDataID;

        public CheckBoxListener() {
            super();
            this.checkboxState = new LinkedList<Short>();
        }

        @Override
        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) {
                checkboxState.set(clickedDataID, DISPLAY_AND_CHECKED);
            } else {
                checkboxState.set(clickedDataID, DISPLAY_NOT_CHECKED);
            }
        }
    }


    public CustomRvAdapter() {
        super();
        this.personList = new LinkedList<Person>();
        this.holderList = new LinkedList<CustomViewHolder>();
        this.displayCheck = false;
        this.checkboxListener = new CheckBoxListener();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item,
                viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);
        this.holderList.add(viewHolder);
        viewHolder.cbChoice.setOnClickListener(this.checkboxListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((CustomViewHolder) viewHolder).tvName.setText(this.personList.get(position).getName());
        ((CustomViewHolder) viewHolder).tvContactNumber.setText(this.personList.get(position).getContactInfo());
        ((CustomViewHolder) viewHolder).dataID = position;
        if (this.checkboxListener.checkboxState.size() > position) {
            this.setCheckbox(((CustomViewHolder) viewHolder).cbChoice, this.checkboxListener.checkboxState.get(position));
        }
        //viewHolder.itemView.setTag(viewHolder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    private void setCheckbox(CheckBox view, short state) {
        switch (state) {
            case NOT_DISPLAY:
                view.setVisibility(View.INVISIBLE);
                break;
            case DISPLAY_NOT_CHECKED:
                view.setChecked(false);
                view.setVisibility(View.VISIBLE);
                break;
            case DISPLAY_AND_CHECKED:
                view.setChecked(true);
                view.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void displayCheckBoxes() {
        if (displayCheck) {
            return;
        } else {
            displayCheck = true;
        }

        int amount = this.checkboxListener.checkboxState.size();
        for (int i = 0; i < amount; i++) {
            this.checkboxListener.checkboxState.set(i, DISPLAY_NOT_CHECKED);
        }

        for (CustomViewHolder viewHolder : this.holderList) {
            this.setCheckbox(viewHolder.cbChoice, DISPLAY_NOT_CHECKED);
        }
    }

    public void hideCheckBoxes() {
        displayCheck = false;

        int amount = this.checkboxListener.checkboxState.size();
        for (int i = 0; i < amount; i++) {
            this.checkboxListener.checkboxState.set(0, NOT_DISPLAY);
        }

        for (CustomViewHolder viewHolder : this.holderList) {
            this.setCheckbox(viewHolder.cbChoice, NOT_DISPLAY);
        }
    }

//    public void checkCheckBox(CustomViewHolder viewHolder) {
//        if (viewHolder.cbChoice.getVisibility() == View.VISIBLE) {
//            if (viewHolder.cbChoice.isChecked()) {
//                this.checkboxState.set(viewHolder.dataID, DISPLAY_AND_CHECKED);
//            } else {
//                this.checkboxState.set(viewHolder.dataID, DISPLAY_NOT_CHECKED);
//            }
//        } else {
//            this.checkboxState.set(viewHolder.dataID, NOT_DISPLAY);
//        }
//        Log.d(String.valueOf(viewHolder.dataID), String.valueOf(this.checkboxState.get(viewHolder.dataID).shortValue()));
//    }

    public void addItem(String name, String contactInfo) {
        Person person = new Person(name, contactInfo);

        this.personList.add(person);
        this.checkboxListener.checkboxState.add(NOT_DISPLAY);

        this.notifyItemInserted(this.personList.size() - 1);
    }

    public void updateItem(int position, String name, String contactInfo) {
        Person person = personList.get(position);

        person.setName(name);
        person.setContactInfo(contactInfo);
        this.notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        this.personList.remove(position);

        this.checkboxListener.checkboxState.remove(position);

        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, this.personList.size());
    }

    public LinkedList<Integer> getChosenList() {
        LinkedList<Integer> result = new LinkedList<Integer>();
        int index = 0;

        for (Short value : this.checkboxListener.checkboxState) {
            if (value.shortValue() == DISPLAY_AND_CHECKED) {
                result.add(new Integer(index));
            }
            index++;
        }

        return result;
    }
}
