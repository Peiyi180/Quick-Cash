package com.csci3130g13.g13quickcash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EmployeeAdapter extends FirebaseRecyclerAdapter<Employee, EmployeeAdapter.EmployeeViewHolder> {

    private Context parentContext;
    private Employer employer;
    private Set<String> filter = null;
    private int marginDp = 30;
    private String jobToHireKey;
    protected final DatabaseReference jobRef = DBConst.dbRef.child("jobs");

    public EmployeeAdapter(@NonNull FirebaseRecyclerOptions<Employee> options, Context parentContext, Employer employer, String jobToHireKey) {
        super(options);
        this.parentContext = parentContext;
        this.employer = employer;
        this.jobToHireKey = jobToHireKey;
    }

    /** Set the filter of Employee keys to display in the recycler */

    public void setFilter(Set<String> filter){
        this.filter = filter;
    }

    public void clearFilter(){
        this.filter = null;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.employee_item, parent, false);

        // extract the default margin from the xml layout
        marginDp = ( (ViewGroup.MarginLayoutParams) view.getLayoutParams() ).getMarginStart();

        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position, Employee employee) {

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;

        // if the ViewHolder holds a employee that doesn't belong in the filter, hide it programmatically.

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

        }

        holder.empID.setText(employee.getId());
        holder.empCity.setText(employee.getCity());
        holder.empName.setText(employee.getName());
        holder.empSkill.setText(employee.getProfession());
        holder.empRep.setText(Integer.toString(employee.getReputation()));
        holder.empEmail.setText(employee.getEmail());

        holder.hireEmpBtn.setOnClickListener(view -> {

            employee.addJob(jobToHireKey, employer.getId());
            employer.addJob(jobToHireKey, employee.getId());
            employee.setReputation(employee.getReputation()+1);
            employer.setReputation(employer.getReputation()+1);


            DatabaseReference jobDBRef = jobRef.child(jobToHireKey);

            jobDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int jobSalary = dataSnapshot.child("salary").getValue(Integer.class);
                    employer.setTotalExpenditure(employer.getTotalExpenditure()+jobSalary);
                    employee.setTotalIncome(employee.getTotalIncome()+jobSalary);

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("/users/employee/" + employee.getId() + "/totalIncome", employee.getTotalIncome());
                    updateMap.put("/users/employer/" + employer.getId() + "/totalExpenditure", employer.getTotalExpenditure());
                    DBConst.dbRef.updateChildren(updateMap);

                    jobDBRef.removeEventListener(this);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("/jobs/" + jobToHireKey + "/employeeID", employee.getId()  );
            updateMap.put("/jobs/" + jobToHireKey + "/hired", true);
            updateMap.put("/users/employer/" + employer.getId() + "/jobs", employer.getJobs());
            updateMap.put("/users/employee/" + employee.getId() + "/jobs", employee.getJobs());
            updateMap.put("/users/employee/" + employee.getId() + "/reputation", employee.getReputation());
            updateMap.put("/users/employer/" + employer.getId() + "/reputation", employer.getReputation());


            DBConst.dbRef.updateChildren(updateMap).addOnSuccessListener(unused -> {
                Toast.makeText(parentContext, "Employee successfully hired.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(parentContext, "Hire operation failed! Please try again.", Toast.LENGTH_SHORT).show();
            });

            parentContext.startActivity(new Intent(parentContext, LandingPageEmployerActivity.class).putExtra("EmployerTag", employer));
            ((AppCompatActivity) parentContext).finish();
        });

    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{

        protected TextView empID ;
        protected TextView empCity;
        protected TextView empName ;
        protected TextView empSkill ;
        protected TextView empRep ;
        protected Button hireEmpBtn;
        protected TextView empEmail;

        public EmployeeViewHolder(@NonNull View itemView) {

            super(itemView);
            empID = itemView.findViewById(R.id.employeeID_TV);
            empCity = itemView.findViewById(R.id.employeeCity_TV);
            empName = itemView.findViewById(R.id.employeeName_TV);
            empSkill = itemView.findViewById(R.id.employeeSkill_TV);
            empRep = itemView.findViewById(R.id.employeeRep_TV);
            hireEmpBtn = itemView.findViewById(R.id.hireEmployeeBtn);
            empEmail = itemView.findViewById(R.id.email_TV);

        }

    }

}
