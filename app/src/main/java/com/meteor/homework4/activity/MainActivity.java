package com.meteor.homework4.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meteor.homework4.adapter.CustomRvAdapter;
import com.meteor.homework4.fragment.InputDialog1;
import com.meteor.homework4.fragment.InputDialog2;
import com.meteor.homework4.other.DataHolder;

public class MainActivity extends AppCompatActivity implements InputDialog1.ClickHandler, InputDialog2.ClickHandler{
    private Button btn_add, btn_linear, btn_grid;

    private RecyclerView rv_info;
    private RecyclerView.LayoutManager rv_infoLiLayoutManager, rv_infoGrLayoutManager;
    private CustomRvAdapter rv_infoAdapter;

    private InputDialog1 inputDialog1;
    private InputDialog2 inputDialog2;

    private int clickedChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildUIViews();
        buildUIListeners();
    }

    private void buildUIViews() {
        btn_add=findViewById(R.id.btn_add);
        btn_grid=findViewById(R.id.btn_grid);
        btn_linear=findViewById(R.id.btn_linear);

        inputDialog1=new InputDialog1();
        inputDialog2=new InputDialog2();

        //build RecyclerView
        rv_info=findViewById(R.id.rv_info);

        rv_infoGrLayoutManager=new GridLayoutManager(this, getResources().getInteger(R.integer.GridLayout_ColumnAmount));
        rv_infoLiLayoutManager=new LinearLayoutManager(this);
        rv_info.setLayoutManager(rv_infoLiLayoutManager);

        rv_infoAdapter=new CustomRvAdapter();
        rv_info.setAdapter(rv_infoAdapter);
    }

    private void buildUIListeners() {
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_add:
                        getSupportFragmentManager().popBackStack();
                        inputDialog1.show(getSupportFragmentManager().beginTransaction(), getString(R.string.dg_input1tag));
                        break;
                    case R.id.btn_grid: {
                        rv_info.setLayoutManager(rv_infoGrLayoutManager);
                        break;
                    }
                    case R.id.btn_linear: {
                        rv_info.setLayoutManager(rv_infoLiLayoutManager);
                        break;
                    }
                }
            }
        };

        btn_add.setOnClickListener(clickListener);
        btn_linear.setOnClickListener(clickListener);
        btn_grid.setOnClickListener(clickListener);

        rv_info.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child=recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if (child != null) {
                    clickedChild=recyclerView.getChildAdapterPosition(child);

                    String viewContent = ((TextView) child).getText().toString();
                    String[] info=viewContent.split("\n");

                    DataHolder.getInstance().setName(info[0]);
                    DataHolder.getInstance().setContactInfo(info[1]);

                    if (inputDialog2.isAdded()) {
                        return false;
                    }

                    getSupportFragmentManager().popBackStack();
                    inputDialog2.show(getSupportFragmentManager().beginTransaction(),getString(R.string.dg_input2tag));
                }
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    @Override
    public void handleSave1(String name, String contactInfo) {
        rv_infoAdapter.addItem(name, contactInfo);
    }

    @Override
    public void handleSave2(String name, String contactInfo) {
        rv_infoAdapter.updateItem(clickedChild, name, contactInfo);
    }

    @Override
    public void handleDelete2() {
        rv_infoAdapter.deleteItem(clickedChild);
    }
}
