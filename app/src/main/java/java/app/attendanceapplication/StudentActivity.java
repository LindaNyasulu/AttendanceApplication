package java.app.attendanceapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String courseName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAddapter addapter;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems  =  new ArrayList<>();
    private DbHelper dbHelper;
    private int cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        dbHelper = new DbHelper(this);
        Intent intent = getIntent();
        className =  intent.getStringExtra("className");
        courseName = intent.getStringExtra("courseName");
        position = intent.getIntExtra("position",-1);
        cid = intent.getIntExtra("cid",-1);

        setToolbar();
        loadData();
        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        addapter=new StudentAddapter(this,studentItems);
        recyclerView.setAdapter(addapter);
        addapter.setOnItemClickListener(position1 ->changeStatus(position));
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,roll,name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        addapter.notifyItemChanged(position);
    }

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView coursetitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText(className);
        coursetitle.setText(courseName);

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemCick(menuItem));

    }

    private boolean onMenuItemCick(MenuItem menuItem) {
        if (menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        return true;
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid,roll,name);
        StudentItem studentItem = new StudentItem(sid,roll,name);
        studentItems.add(studentItem);
        addapter.notifyDataSetChanged();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
            return super.onContextItemSelected(item);

    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        addapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        addapter.notifyItemRemoved(position);
    }

}