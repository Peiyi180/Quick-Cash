package com.csci3130g13.g13quickcash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**

 * Use the {@link ShowEmployerInfoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowEmployerInfoDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_1 = "param1";


    // TODO: Rename and change types of parameters
    private String employerID;


    public ShowEmployerInfoDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShowEmployerInfoDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowEmployerInfoDialogFragment newInstance(String param1) {
        ShowEmployerInfoDialogFragment fragment = new ShowEmployerInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employerID = getArguments().getString(PARAM_1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_employer_info_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView empID = (TextView) view.findViewById(R.id.empID);
        TextView empEmail = (TextView) view.findViewById(R.id.empEmail);
        TextView empName = (TextView) view.findViewById(R.id.empName);
        Button closeBtn = (Button) view.findViewById(R.id.returnBtn);

        closeBtn.setOnClickListener(btnView -> {
            dismiss();
        });

        DatabaseReference employerDB = DBConst.dbRef.child("users").child("employer").child(employerID);

        employerDB.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Employer storedEmp = snapshot.getValue(Employer.class);

                empID.setText(employerID);
                empName.setText(storedEmp.getName());
                empEmail.setText(storedEmp.getEmail());

                employerDB.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }
}