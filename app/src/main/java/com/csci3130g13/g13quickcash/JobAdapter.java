package com.csci3130g13.g13quickcash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobAdapter extends FirebaseRecyclerAdapter<Job, JobAdapter.JobViewHolder> {

    private Context parentContext;
    private User viewer;
    private Set<String> filter = null;
    private int marginDp = 30;
    private Job jobToHire;

    public JobAdapter(@NonNull FirebaseRecyclerOptions<Job> options, Context parentContext, User viewer) {
        super(options);
        this.parentContext = parentContext;
        this.viewer = viewer;
    }

    /** Set the filter of Job keys to display in the recycler */

    public void setFilter(Set<String> filter){
        this.filter = filter;
    }

    public void clearFilter(){
        this.filter = null;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.job_item, parent, false);

        // extract the default margin from the xml layout
        marginDp = ( (ViewGroup.MarginLayoutParams) view.getLayoutParams() ).getMarginStart();

        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position, Job job) {

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;

        // if the ViewHolder holds a job that doesn't belong in the filter, hide it programmatically.

        if(filter != null && !filter.contains(getRef(position).getKey())){
            holder.itemView.setVisibility(View.GONE);
            marginParams.setMargins(0,0,0,0);
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            marginParams.setMargins(marginDp, marginDp, marginDp, marginDp);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(params);

            //Log.d("marginparams", Integer.toString(marginDp));
        }

        holder.businessType.setText(job.getBusinessType());
        holder.businessName.setText(job.getBusinessName());
        holder.city.setText(job.getCity());
        holder.positionName.setText(job.getPositionName());
        holder.salary.setText(job.getSalary() + "$/day");
        holder.jobDesc.setText(job.getJobDesc());
        holder.prefEmployeeType.setText(job.getPrefEmployeeType());
        holder.prefSkill.setText(job.getPrefSkill());
        holder.startDate.setText(job.getStartDate());
        holder.endDate.setText(job.getEndDate());
        holder.workHours.setText(job.getWorkHours() + "hrs/day");

        if(job.isHired()){
            holder.jobStatus.setText("Position Unavailable (hired)");
            holder.itemView.findViewById(R.id.mainLayout_jobItem).setBackgroundColor(Color.CYAN);
        } else {
            holder.jobStatus.setText("Hiring");
            holder.itemView.findViewById(R.id.mainLayout_jobItem).setBackgroundColor(Color.parseColor("#ff99cc00")); //holo_green_light
        }

        holder.contactBtn.setOnClickListener(view -> {
            // launch Employer info Fragment.
            ShowEmployerInfoDialogFragment frag = ShowEmployerInfoDialogFragment.newInstance(job.getEmployerID());
            FragmentActivity fa = (FragmentActivity) parentContext;
            frag.show(fa.getSupportFragmentManager(), "Dialog Fragment");

        });

        // hide delete button if the author isn't the same as the viewer.

        if(! job.getEmployerID().equals(viewer.getId()) ) {
            holder.delBtn.setVisibility(View.GONE);
            holder.delBtn.setOnClickListener(null);
            holder.hireBtn.setVisibility(View.GONE);
            holder.hireBtn.setOnClickListener(null);
        } else {
            holder.delBtn.setVisibility(View.VISIBLE);
            holder.delBtn.setOnClickListener(view -> {

                DatabaseReference jobToDelete = getRef(position);
                String jobToDeleteKey = jobToDelete.getKey();

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("/jobs/" + jobToDeleteKey, null );
                updateMap.put("/users/employer/" + job.getEmployerID() + "/jobs/" + jobToDeleteKey, null);

                if(!job.getEmployeeID().equals("N/A")) {
                    updateMap.put("/users/employee/" + job.getEmployeeID() + "/jobs/" + jobToDeleteKey, null);
                }

                DBConst.dbRef.updateChildren(updateMap);
                notifyDataSetChanged();

            });

            if(!job.isHired()) {
                holder.hireBtn.setVisibility(View.VISIBLE);

                holder.hireBtn.setOnClickListener(view -> {

                    DatabaseReference jobToHireRef = getRef(position);

                    Intent hireIntent = new Intent(parentContext, HireActivity.class);
                    hireIntent.putExtra("JOB_TO_HIRE_KEY", jobToHireRef.getKey());
                    hireIntent.putExtra("EMPLOYER_TO_HIRE", (Employer) viewer);
                    parentContext.startActivity(hireIntent);

                    notifyDataSetChanged();

                });
            } else {

                holder.hireBtn.setVisibility(View.GONE);
                holder.hireBtn.setOnClickListener(null);

            }


        }

    }

    public class JobViewHolder extends RecyclerView.ViewHolder{

        protected TextView businessType ;
        protected TextView businessName;
        protected TextView city ;
        protected TextView positionName ;
        protected TextView salary ;
        protected TextView jobDesc ;
        protected TextView prefEmployeeType ;
        protected TextView prefSkill ;
        protected TextView startDate ;
        protected TextView endDate ;
        protected TextView workHours ;
        protected Button contactBtn;
        protected Button delBtn;
        protected Button hireBtn;
        protected TextView jobStatus;

        public JobViewHolder(@NonNull View itemView) {

            super(itemView);
            businessType = itemView.findViewById(R.id.businessType_card);
            businessName = itemView.findViewById(R.id.businessName_card);
            city = itemView.findViewById(R.id.jobCity_card);
            positionName = itemView.findViewById(R.id.positionName_card);
            salary = itemView.findViewById(R.id.jobSalary);
            jobDesc = itemView.findViewById(R.id.jobDesc_card);
            prefEmployeeType = itemView.findViewById(R.id.workFormat);
            prefSkill = itemView.findViewById(R.id.reqSkill);
            startDate = itemView.findViewById(R.id.jobStartDate_card);
            endDate = itemView.findViewById(R.id.jobEndDate_card);
            workHours = itemView.findViewById(R.id.jobHours_card);
            contactBtn = itemView.findViewById(R.id.contactButton);
            delBtn = itemView.findViewById(R.id.delButton);
            hireBtn = itemView.findViewById(R.id.hireButton);
            jobStatus = itemView.findViewById(R.id.jobStatus_TV);

        }

    }

}
