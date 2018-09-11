package com.leslie.codebase.litepal;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.leslie.codebase.litepal.adapter.ListAdapter;
import com.leslie.codebase.litepal.sql.SqlControlor;
import com.leslie.codebase.litepal.sql.SqlUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //    private TextView showInfo;
    private EditText editText;
    private ListView listView;

    private ArrayAdapter<String> tableAdapter;
    private ListAdapter fieldAdapter;

//    private SQLiteDatabase database = Connector.getDatabase();
    private SqlControlor sqlControlor = new SqlControlor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();

        startAnimation();
        // 设置标题为数据库的名称
        setTitle(sqlControlor.databaseName());


//        inserDataToTable(database);
//        inserDataToTable(database);
//        inserDataToTable(database);
//        inserDataToTable(database);
//        inserDataToTable(database);

//
//        watchDataShowText(database);

//        database.execSQL(SqlUtil.createTable("a", new String[]{"id interger"}));
//        database.execSQL(SqlUtil.createTable("b", new String[]{"id interger"}));
//        database.execSQL(SqlUtil.createTable("c", new String[]{"id interger"}));
//        database.execSQL(SqlUtil.createTable("d", new String[]{"id interger"}));


    }

    /**
     * 绑定控件
     */
    private void bindViews() {
//        showInfo = findViewById(R.id.textView);

        editText = findViewById(R.id.edit_sql);

        findViewById(R.id.btn_exc).setOnClickListener(this);

        listView = findViewById(R.id.listView);


        tableAdapter = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                sqlControlor.updateTables()
        );

        listView.setAdapter(tableAdapter);

        updateTableList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;

                final String tableName = textView.getText().toString();

                showTableDir(tableName);

            }
        });


    }

    private void updateTableList() {

        sqlControlor.updateTables();
        updateListView(listView);

    }
    
    private void updateListView(ListView listView) {
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }


    /**
     * 设置监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exc:
                try {
                    sqlControlor.execute(editText.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    /***********************************************************************************************************/
    /**
     * 初始化菜单并加载
     * 设置监听事件
     */
    /***********************************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /******************新建表格******************/
            case R.id.newTable:
                showPopupWindow(R.layout.activity_main, R.layout.option_new_table, createPopupBinder());
                break;
        }
        return true;
    }


    /**
     * 显示数据到文本试图
     *
     * @param database
     */
//    private void watchDataShowText(SQLiteDatabase database) {
//        Cursor cursor = database.query("Person", null, null, null, null, null, null);
//        if (cursor != null) {
//            StringBuilder builder = new StringBuilder();
//            while (cursor.moveToNext()) {
//                builder.append("name = " + cursor.getString(cursor.getColumnIndex("name")));
//                builder.append("\t");
//                builder.append("age = " + cursor.getInt(cursor.getColumnIndex("age")));
//                builder.append("\n");
//
//            }
//            cursor.close();
//            showInfo.setText(builder.toString());
//
//        }
//    }

    /************************操作数据库中的数据************************/
    private void inserDataToTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("name", "李玮斌");
        values.put("age", 20);
        database.insert("person", null, values);
    }

//    private void updateDataToTable(SQLiteDatabase database, String tableName) {
//        ContentValues values = new ContentValues();
//        values.put("sex", "male");
//        int row = database.update(tableName, values, "name = ?", new String[]{"陈春贝"});
//        Toast.makeText(this, database.getPath() + "数据库更新了" + row + "行数据!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void deleteDataToTable(SQLiteDatabase database, String tableName) {
//        int row = database.delete(tableName, "name = ?", new String[]{"陈春贝"});
//        Toast.makeText(this, database.getPath() + "数据库删除了" + row + "行数据!", Toast.LENGTH_SHORT).show();
//    }

    /***************************** END *****************************/

    /************************显示popupWindow************************/
    public void showPopupWindow(int root, int content, PopupBinder binder) {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(content, null);

        View rootView = LayoutInflater.from(MainActivity.this).inflate(root, null);

        PopupWindow popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1300, true);

        binder.OnBind(content, contentView, popupWindow);

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        openingAni(contentView);


    }

    private interface PopupBinder {
        void OnBind(int content, View contentView, PopupWindow window);
    }

    // popupWindow装订器对象
    PopupBinder binder;

    public PopupBinder createPopupBinder() {
        if (binder == null) {
            binder = new PopupBinder() {
                @Override
                public void OnBind(int content, View contentView, PopupWindow window) {
                    switch (content) {
                        case R.layout.option_new_table:
                            bindAddOption(contentView, window);
                            break;
                        case R.layout.option_list:
                            bindItemOption(contentView, window);
                            break;
                    }
                }
            };
        }
        return binder;

    }

    private void dismissWindowAni(PopupWindow window, final View content) {

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                goneViewAnimation(content);
            }
        });
        window.dismiss();
    }

    /**
     * 弹出选择类型窗口
     * @param view
     * @param window
     */
    private void bindItemOption(View view, final PopupWindow window) {
        TextView textView = view.findViewById(R.id.title);
        textView.setText("请输入字段类型");
        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, SqlUtil.sql_type));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editType.setText(SqlUtil.sql_type[position]);
                dismissWindowAni(window, view);
            }
        });
    }

    TextView editType;

    private void bindAddOption(final View view, final PopupWindow window) {
        TextView textView = view.findViewById(R.id.title);
        textView.setText("请输入表格信息");

        final List<String> fields = new ArrayList<>();
        final ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, fields));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fields.remove(position);
                return true;
            }
        });

        final EditText editTbale = view.findViewById(R.id.edit_table);

        final EditText editField = view.findViewById(R.id.edit_field);


        class Listener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.editType:
                        showPopupWindow(R.layout.activity_main, R.layout.option_list, createPopupBinder());

                        break;
                    case R.id.buttonAdd:
                        fields.add(editField.getText().toString() + " " + editType.getText().toString());
                        updateListView(listView);
                        break;


                    case R.id.buttonOK:
                        dismissWindowAni(window, view);
                        if (sqlControlor.createTable(editTbale.getText().toString(), fields.toArray(new String[fields.size()]))) {
                            Toast.makeText(MainActivity.this, "成功创建\""+ editTbale.getText().toString() +"\"表格", Toast.LENGTH_SHORT).show();
                            updateTableList();
                        } else {
                            Toast.makeText(MainActivity.this, "创建表格失败", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.buttonCancel:
                        dismissWindowAni(window, view);
                        break;
                }
            }
        }
        Listener listener = new Listener();
        editType = view.findViewById(R.id.editType);
        editType.setOnClickListener(listener);
        Button btn = view.findViewById(R.id.buttonAdd);
        btn.setOnClickListener(listener);
        btn = view.findViewById(R.id.buttonCancel);
        btn.setOnClickListener(listener);
        btn = view.findViewById(R.id.buttonOK);
        btn.setOnClickListener(listener);
    }

    /***************************** END *****************************/


    /************************ 显示动画方法 ************************/
    private void startAnimation() {
        openingAniById(R.id.layout);
        startViewAnimation(listView, false);

    }

    private <V extends View> void startViewAnimation(V view, boolean right) {


        if (right) {

            int width = getDisplayMetrics()[0];
//        int height = getDisplayMetrics()[1];
            ViewPropertyAnimator animator = view.animate();

            animator.translationXBy(width);

            animator.setDuration(300l);

            animator.start();
        }
        Animation alphaAni = new AlphaAnimation(0, 1);
        alphaAni.setDuration(1000);
        view.startAnimation(alphaAni);


    }

    private <V extends View> void goneViewAnimation(V view) {
        int width = getDisplayMetrics()[0];
//        int height = getDisplayMetrics()[1];
        ViewPropertyAnimator animator = view.animate();

        animator.translationXBy(-width);

        animator.setDuration(300l);

        animator.start();
    }

    private void openingAniById(int layoutId) {
        View view = findViewById(layoutId);
        openingAni(view);
    }

    private void openingAni(View view) {
        view.setVisibility(View.INVISIBLE);
        /**
         * 透明度渐变动画
         */
        Animation alphaAni = new AlphaAnimation(0, 1);
        alphaAni.setDuration(300);

//        /**
//         * 缩放度渐变动画
//         */
//        int width = getDisplayMetrics()[0];
//        int height = getDisplayMetrics()[1];
//        Animation scaleAni = new ScaleAnimation(0.01f, 1, 0.01f, 1, width / 2, height / 2);
//        scaleAni.setDuration(500);

        /**
         * 平移渐变动画
         */
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        Animation transAni = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        transAni.setDuration(500); // 设置动画的过渡时间


        AnimationSet aniSet = new AnimationSet(true);
        aniSet.addAnimation(alphaAni);
//        aniSet.addAnimation(scaleAni);
        aniSet.addAnimation(transAni);

        /**
         * 延时动画
         */
//        layout.postDelayed(new Runnable() {
//            @Override
//            public void run() {


        view.startAnimation(aniSet);
        view.setVisibility(View.VISIBLE);

//            }
//        }, 500);
    }

    private void gonegAniById(int layoutId) {
        View view = findViewById(layoutId);
        goneAni(view);
    }

    private void goneAni(View view) {

        view.setVisibility(View.VISIBLE);
        /**
         * 透明度渐变动画
         */
        Animation alphaAni = new AlphaAnimation(1, 0);
        alphaAni.setDuration(1000);


        /**
         * 平移渐变动画
         */
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        Animation transAni = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1);
        transAni.setDuration(500); // 设置动画的过渡时间


        AnimationSet aniSet = new AnimationSet(true);
        aniSet.addAnimation(alphaAni);
        aniSet.addAnimation(transAni);


        view.startAnimation(aniSet);

        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (baseDir()) {
            gonegAniById(R.id.layout);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.super.onBackPressed();
                        }
                    });

                }
            }, 500);
        } else {
            showDatabaseDir();
        }


    }

    private boolean baseDir() {
        if (getTitle().toString().endsWith(".db")) {
            return true;
        } else {
            return false;
        }

    }

    private void showDatabaseDir() {
        goneViewAnimation(listView);

        setTitle(sqlControlor.databaseName());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(tableAdapter);

                        updateListView(listView);

                        startViewAnimation(listView, true);
                    }
                });

            }
        }, 400);


    }

    private void showTableDir(final String tableName) {
        goneViewAnimation(listView);

        setTitle(tableName);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        fieldAdapter = new ListAdapter(MainActivity.this, R.layout.recycle_item_view, sqlControlor.fetchValues(tableName))
                                .setColumns(sqlControlor.columns(tableName));

                        listView.setAdapter(fieldAdapter);

                        updateListView(listView);

                        startViewAnimation(listView, true);

                    }
                });

            }
        }, 400);

    }

    /***************************** END *****************************/

    private int[] getDisplayMetrics() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return new int[]{
                width, height
        };
    }


}