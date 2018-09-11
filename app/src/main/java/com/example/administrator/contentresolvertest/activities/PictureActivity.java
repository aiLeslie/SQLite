package com.example.administrator.contentresolvertest.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.administrator.contentresolvertest.R;
import com.example.administrator.contentresolvertest.recycle.IAdapter;
import com.example.administrator.contentresolvertest.recycle.adapter.PicAdapter;
import com.example.administrator.contentresolvertest.recycle.holder.binder.PictureBinder;
import com.example.administrator.contentresolvertest.recycle.holder.PictureViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PictureActivity extends AppCompatActivity {
    private static final String TAG = "PictureActivity";
    private RecyclerView recyclerView;
    private ContentResolver contentResolver;
    private List<HashMap<String, Object>> contactMapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // 检查权限
        if (checkAndRequestPermission()) {//检查和申请权限
            init();
        }


    }

    private void init() {

        initRecycleView();

        getContentForRecycler();

        getContentFromMyProvider();
    }

    /**
     * 初始化recycleView
     */
    private void initRecycleView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        IAdapter<PictureViewHolder> iAdapter = new PicAdapter(this, R.layout.pic_item, contactMapList);

        iAdapter.setBinder(new PictureBinder());

        recyclerView.setAdapter(iAdapter);

        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager); // 设置管理器

        // 瀑布流布局管理器
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.HORIZONTAL)); // 设置管理器


    }

    /**
     * 初始化数据
     */
    private void getContentForRecycler() {

        if (contactMapList == null) { // 如果contactMapList为空
            contactMapList = new ArrayList<>(); // 实例化对象
        } else { // 否则
            contactMapList.clear(); // 清空列表
        }

        contentResolver = this.getContentResolver();//获得内容溶剂对象

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        HashMap<String, Object> itemMap;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                itemMap = new HashMap<>();
                itemMap.put(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                contactMapList.add(itemMap);

            }
            cursor.close();
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 更新recycleView
     */
    private void updataRecycleView() {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 从contentProvider读取数据
     */
    private void getContentFromMyProvider() {
        contentResolver = this.getContentResolver();//获得内容溶剂对象

        // 实例化URI对象(前缀 + 包名 + 表名)
        Uri uri = Uri.parse("content://com.example.myprovider.sql/leslie");


        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()) {
                    builder.append("\n");
                    builder.append(cursor.getString(cursor.getColumnIndex("name")));
                    builder.append(" ");
                    builder.append(cursor.getInt(cursor.getColumnIndex("age")));
                    builder.append("\n");

                }
                Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
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
            return false;
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
