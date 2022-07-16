package com.csci3130g13.g13quickcash;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**  Helper class for Search operations.  */
public class SearchHelper {

    protected Query query;
    protected Context context;
    protected Bundle searchCriteriaData;
    protected HashMap<String, String> filterCriteria;
    protected final DatabaseReference jobRef = DBConst.dbRef.child("jobs");
    protected final DatabaseReference employeeRef = DBConst.dbRef.child("users").child("employee");

    /** @param searchCriteriaData the search criteria, as returned by SearchJobActivity
     *  @param context context from which the class is instantiated.
     */
    public SearchHelper(Context context, Bundle searchCriteriaData){

        this.context = context;
        this.searchCriteriaData = searchCriteriaData;

    }

    /** @param filterCriteria the search criteria, as returned by HireActivity
     *  @param context context from which the class is instantiated.
     */
    public SearchHelper(Context context, HashMap<String, String> filterCriteria){

        this.context = context;
        this.filterCriteria = filterCriteria;

    }

    /** Parses Spinner position into an RTDB child key of a Job.
     *  @param pos position number of the category in the sort category Spinner.
     *  @return  RTDB child key corresponding to the category.
     */
    public static String parseSortCategory(int pos){

        switch(pos){

            case 1:
                return "businessName";

            case 2:
                return "businessType";

            case 3:
                return "city";

            case 4:
                return "startDate_sec";

            case 5:
                return "endDate_sec";

            case 6:
                return "prefEmployeeType";

            case 7:
                return "salary";

            case 8:
                return "workHours";

            case 9:
                return "prefSkill";

            default:
                return "default";

        }

    }


    /** Produces a filter of jobs that match the search criteria.
     *  @return  a Set of Strings that represent the keys of the Jobs that match the search criteria.
     */
    public Set<String> buildSearchFilter(){





        Double minSalary = searchCriteriaData.getDouble("minSalary");
        Long startDate_sec_start = searchCriteriaData.getLong("startDate_sec_start");
        Long startDate_sec_end = searchCriteriaData.getLong("startDate_sec_end");
        Long endDate_sec_start = searchCriteriaData.getLong("endDate_sec_start");
        Long endDate_sec_end = searchCriteriaData.getLong("endDate_sec_end");
        String hireType = searchCriteriaData.getString("hireType");
        String reqSkill = searchCriteriaData.getString("reqSkill");
        String city = searchCriteriaData.getString("city");
        Double workHours_start = searchCriteriaData.getDouble("workHours_start");
        Double workHours_end = searchCriteriaData.getDouble("workHours_end");


        Set<String> filter = new HashSet<>();

        //populate the filter with all Jobs or Jobs with matching cities, depending on whether city name was indicated in search criteria.

        if(!city.equals("Not specified")){

            query = jobRef.orderByChild("city").equalTo(city);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        filter.add(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            jobRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.add(child.getKey());
                    }
                    jobRef.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


        if(minSalary != -1){
            query = jobRef.orderByChild("salary").endBefore(minSalary);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        if(startDate_sec_start != -1){

            query = jobRef.orderByChild("startDate_sec").endBefore(startDate_sec_start);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            query = jobRef.orderByChild("startDate_sec").startAfter(startDate_sec_end);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        if(endDate_sec_start != -1){

            query = jobRef.orderByChild("endDate_sec").endBefore(endDate_sec_start);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            query = jobRef.orderByChild("endDate_sec").startAfter(endDate_sec_end);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        if(workHours_start != -1){

            query = jobRef.orderByChild("workHours").endBefore(workHours_start);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            query = jobRef.orderByChild("workHours").startAfter(workHours_end);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        if(!hireType.equals("Not specified")){

            String antitype;

            if(hireType.equals("Part-Time")){
                antitype = "Full-Time";
            } else {
                antitype = "Part-Time";
            }

            query = jobRef.orderByChild("prefEmployeeType").equalTo(antitype);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if(!reqSkill.equals("Not specified")){

            CharSequence[] array = context.getResources().getTextArray(R.array.business_type_array);
            List<CharSequence> antitype = new ArrayList<>(Arrays.asList(array));
            antitype.remove(reqSkill);

            for(CharSequence index : antitype) {

                query = jobRef.orderByChild("prefSkill").equalTo(index.toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            filter.remove(child.getKey());
                        }
                        query.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        }


        return filter;

    }

    /** Produces a filter of employees that match the job criteria.
     *  @return  a Set of Strings that represent the keys of the Employees that match the job criteria.
     */
    public Set<String> buildEmployeeSearchFilter(){

        String reqSkill = filterCriteria.get("reqSkill");
        String hireType = filterCriteria.get("hireType");
        String city = filterCriteria.get("city");

        Set<String> filter = new HashSet<>();

        query = employeeRef.orderByChild("city").equalTo(city);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    filter.add(child.getKey());
                }
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(!reqSkill.equals("Not specified")){

            CharSequence[] array = context.getResources().getTextArray(R.array.business_type_array);
            List<CharSequence> antitype = new ArrayList<>(Arrays.asList(array));
            antitype.remove(reqSkill);

            for(CharSequence index : antitype) {

                query = employeeRef.orderByChild("profession").equalTo(index.toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            filter.remove(child.getKey());
                        }
                        query.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        }

        if(!hireType.equals("Not specified")){

            String antitype;

            if(hireType.equals("Part-Time")){
                antitype = "Full-Time";
            } else {
                antitype = "Part-Time";
            }

            query = employeeRef.orderByChild("jobPreference1").equalTo(antitype);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren() ) {
                        filter.remove(child.getKey());
                    }
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        return filter;

    }
}
