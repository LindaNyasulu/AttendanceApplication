package java.app.attendanceapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAddapter classAddapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(v-> showDialog());

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAddapter = new ClassAddapter(this,classItems);
        recyclerView.setAdapter(classAddapter);
        classAddapter.setOnItemClickListener(position -> gotItemActivity(position));
        setToolbar();
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
        classItems.add(new ClassItem(className,courseName));
        classAddapter.notifyDataSetChanged();
    }
}