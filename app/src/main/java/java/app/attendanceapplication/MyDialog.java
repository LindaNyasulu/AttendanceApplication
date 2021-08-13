package java.app.attendanceapplication;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG="addClass";

    private OnClickListener listener;
    public interface OnClickListener{
        void onClick(String text1,String text2);
    }
    public void setListener(OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=null;
        if (getTag().equals(CLASS_ADD_DIALOG))dialog=getAddClassDialog();
        return dialog;
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Class");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText course_edt = view.findViewById(R.id.edt02);

        class_edt.setHint("Class Name");
        course_edt.setHint("Course Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v-> {
            String className = class_edt.getText().toString();
            String courseName  = course_edt.getText().toString();
            listener.onClick(className,courseName);
            dismiss();
        });
        return builder.create();
    }
}
