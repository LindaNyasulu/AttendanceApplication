package java.app.attendanceapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        className =  intent.getStringExtra("className");
        courseName = intent.getStringExtra("courseName");
        position = intent.getIntExtra("position",-1);

        setToolbar();
        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        addapter=new StudentAddapter(this,studentItems);
        recyclerView.setAdapter(addapter);
        addapter.setOnItemClickListener(position1 ->changeStatus(position));
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

    private void addStudent(String roll, String name) {
        studentItems.add(new StudentItem(roll, name) );
        addapter.notifyItemChanged(studentItems.size()-1);
    }

}