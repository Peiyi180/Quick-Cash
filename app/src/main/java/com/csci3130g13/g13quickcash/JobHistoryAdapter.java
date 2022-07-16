package com.csci3130g13.g13quickcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class JobHistoryAdapter extends FirebaseRecyclerAdapter<Job, JobHistoryAdapter.JobHistoryViewHolder> {

    private Context parentContext;
    private User viewer;
    private int marginDp = 30;

    public JobHistoryAdapter(@NonNull FirebaseRecyclerOptions<Job> options, Context parentContext, User viewer) {
        super(options);
        this.parentContext = parentContext;
        this.viewer = viewer;
    }

    @NonNull
    @Override
    public JobHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_history_item, parent, false);
        return new JobHistoryViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobHistoryViewHolder holder, int position, @NonNull Job job) {

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;

        if( job.getEmployerID().equals(viewer.getId()) || job.getEmployeeID().equals(viewer.getId())) {
            holder.itemView.setVisibility(View.VISIBLE);
            marginParams.setMargins(marginDp, marginDp, marginDp, marginDp);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            holder.itemView.setVisibility(View.GONE);
            marginParams.setMargins(0,0,0,0);
            params.height = 0;
        }

        holder.itemView.setLayoutParams(params);

        holder.positionName.setText(job.getPositionName());
        holder.businessName.setText(job.getBusinessName());
        holder.businessType.setText(job.getBusinessType());
        holder.city.setText(job.getCity());
        holder.salary.setText(job.getSalary() + "$/day");
        holder.prefEmployeeType.setText(job.getPrefEmployeeType());
        holder.workHours.setText(job.getWorkHours() + "hrs/day");
        holder.prefSkill.setText(job.getPrefSkill());
        holder.startDate.setText(job.getStartDate());
        holder.endDate.setText(job.getEndDate());
        holder.jobDesc.setText(job.getJobDesc());
    }

    public class JobHistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView positionName ;
        private TextView businessName;
        private TextView businessType;
        private TextView city ;
        private TextView salary ;
        private TextView prefEmployeeType ;
        private TextView workHours ;
        private TextView prefSkill ;
        private TextView startDate ;
        private TextView endDate ;
        private TextView jobDesc ;

        public JobHistoryViewHolder(@NonNull View itemView) {

            super(itemView);
            positionName = itemView.findViewById(R.id.positionName_card2);
            businessName = itemView.findViewById(R.id.businessName_card2);
            businessType = itemView.findViewById(R.id.businessType_card2);
            city = itemView.findViewById(R.id.jobCity_card2);
            salary = itemView.findViewById(R.id.jobSalary2);
            prefEmployeeType = itemView.findViewById(R.id.workFormat2);
            workHours = itemView.findViewById(R.id.jobHours_card2);
            prefSkill = itemView.findViewById(R.id.reqSkill2);
            startDate = itemView.findViewById(R.id.jobStartDate_card2);
            endDate = itemView.findViewById(R.id.jobEndDate_card2);
            jobDesc = itemView.findViewById(R.id.jobDesc_card2);

        }

    }
}
