package com.example.myprovider;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myprovider.fragment.NewDBFragment;
import com.example.myprovider.sql.MyProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NewDBFragment.OnOkListener, MyProvider.OnUpdateListener{
    private MyProvider mProvider;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private List<String> dbNames = new ArrayList<>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProvider = (MyProvider) ApplicationUtils.mContentProvider;
        mProvider.setUpdateListener(this);

        init();
    }

    private void init() {

        initListView();


    }

    private List convertDatabase(HashMap<String, SQLiteDatabase> databases) {
        dbNames.clear();
        for (Map.Entry<String, SQLiteDatabase> entry : databases.entrySet()) {

            dbNames.add(entry.getKey());
        }
        return dbNames;

    }

    private void updateListView() {
        convertDatabase(mProvider.getDatabases());
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }


    /**
     * 初始化recycleView
     */
    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dbNames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectdbName = dbNames.get(position);
                mProvider.deleteDatabase(selectdbName);
//                mProvider.queryTable(selectdbName, new MyProvider.QueryTableListener() {
//                    @Override
//                    public void onQuery(SQLiteDatabase database) {
//
//                    }
//                });
            }
        });

        updateListView();
    }


    /***********************************************************************************************************/
    /**
     * 初始化菜单并加载
     * 设置监听事件
     */
    /***********************************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_set_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /******************新建数据库******************/
            case R.id.newDatabase:
                newDatabaseItemProcess();
                break;
        }
        return true;
    }

    private void newDatabaseItemProcess() {
        repalceFragment(R.id.frameDialog, new NewDBFragment().setOkListener(this));
    }

    /**
     * 替换碎片
     */
    private void repalceFragment(int id, Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        transaction.addToBackStack(null);

        transaction.replace(id, fragment);

        transaction.commit();


    }

    @Override
    public void onOk() {
        updateListView();
    }

    @Override
    public void onUpdate() {
        updateListView();
    }
}
