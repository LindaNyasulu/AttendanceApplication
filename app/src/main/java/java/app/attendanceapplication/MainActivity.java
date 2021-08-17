package java.app.attendanceapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAddapter classAddapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(v-> showDialog());
        loadData();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAddapter = new ClassAddapter(this,classItems);
        recyclerView.setAdapter(classAddapter);
        classAddapter.setOnItemClickListener(position -> gotItemActivity(position));
        setToolbar();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getClassTable();
        classItems.clear();
        while (cursor.moveToNext()){
         int id = cursor.getInt(cursor.getColumnIndex(DbHelper.C_ID));
         String className = cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
         String courseName = cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_NAME_KEY));

         classItems.add(new ClassItem(id,className,courseName));
        }

    }

    //        @SuppressLint("SetTextI18n")
    private void setToolbar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView coursetitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Attendance Application");
        coursetitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);

        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("courseName",classItems.get(position).getCourseName());
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private void showDialog(){
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className,courseName)->addClass(className,courseName));


    }

    private void addClass(String className,String courseName) {
        long cid = dbHelper.addClass(className,courseName);
        ClassItem classItem = new ClassItem(cid,className,courseName);
        classItems.add(classItem);
        classAddapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case  1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((className,courseName)->updateClass(position,className,courseName));
    }

    private void updateClass(int position, String className, String courseName) {
        dbHelper.updateClass(classItems.get(position).getCid(),className,courseName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setCourseName(courseName);
        classAddapter.notifyItemChanged(position);
    }

    private void deleteClass(int position){
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAddapter.notifyItemRemoved(position);


    }
}