package com.example.administrator.contentresolvertest.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.contentresolvertest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ContactActivity extends AppCompatActivity {
    private ContentResolver contentResolver;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> contactsList = new ArrayList<>();


    private List<HashMap<String, Object>> contactMapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // 检查权限
        if (checkAndRequestPermission()) {//检查和申请权限
            init();
        }


    }

    private void init() {
        // 初始化listView控件
        initListView();

        getContentForList();
    }

    /**
     * 初始化listView控件
     */
    private void initListView() {

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, contactsList);

        listView.setAdapter(adapter);
    }


    /**
     * 获取联系人内容
     */
    private void getContentForList() {
        contentResolver = this.getContentResolver();//获得内容解析对象

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        StringBuilder builder = new StringBuilder();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                builder.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                builder.append("\n");
                builder.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contactsList.add(builder.toString());
                builder.setLength(0);
            }
            cursor.close();

            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 检查权限和申请权限
     *
     * @return
     */
    private boolean checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
            return true;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 申请权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        } else if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "程序即将退出", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);

        }
    }
}
