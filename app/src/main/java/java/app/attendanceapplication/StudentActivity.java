package java.app.attendanceapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String courseName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        className =  intent.getStringExtra("className");
        courseName = intent.getStringExtra("courseName");
        position = intent.getIntExtra("position",-1);

        setToolbar();
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

    }

}