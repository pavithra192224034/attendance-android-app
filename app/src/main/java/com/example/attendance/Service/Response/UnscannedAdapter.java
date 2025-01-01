package com.example.attendance.Service.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.R;

import java.util.ArrayList;
import java.util.List;

public class UnscannedAdapter extends RecyclerView.Adapter<UnscannedAdapter.ViewHolder> {

    private List<AbsentListResponse.Stdent> studentList;

    private FragmentActivity activity;

    public UnscannedAdapter(List<AbsentListResponse.Stdent> studentList, FragmentActivity activity) {
        this.studentList = studentList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AbsentListResponse.Stdent student = studentList.get(position);
        holder.nameTextView.setText(student.getStudent_name());
    }

    @Override
    public int getItemCount() {
        return studentList==null?0:studentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.course);
        }
    }
}