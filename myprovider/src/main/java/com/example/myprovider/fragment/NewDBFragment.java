package com.example.myprovider.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprovider.ApplicationUtils;
import com.example.myprovider.R;
import com.example.myprovider.sql.MyProvider;


public class NewDBFragment extends Fragment implements View.OnClickListener {
    private MyProvider mProvider = (MyProvider) ApplicationUtils.mContentProvider;
    private TextView title;
    private EditText editName;
    private EditText editVersion;


    public OnOkListener okListener;

    public NewDBFragment setOkListener(OnOkListener okListener) {
        this.okListener = okListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_db, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        title = (TextView) view.findViewById(R.id.title);
        title.setText("设置数据库");

        editName = (EditText) view.findViewById(R.id.editName);
        editVersion = (EditText) view.findViewById(R.id.editVersion);

        Button button = (Button) view.findViewById(R.id.buttonOK);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.buttonCancel);
        button.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonOK:
                try{
                    mProvider.newDatabase(editName.getText().toString(), Integer.parseInt(editVersion.getText().toString()));
                    if (okListener != null) {
                        okListener.onOk();
                    }
                    getActivity().onBackPressed();
                }catch(NumberFormatException e) {
                    toast(e.getMessage());
                }


                break;

            case R.id.buttonCancel:
                getActivity().onBackPressed();
                break;

        }
    }


    public interface OnOkListener{
        void onOk();
    }

    private void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


}
