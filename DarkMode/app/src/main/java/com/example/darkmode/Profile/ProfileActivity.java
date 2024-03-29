package com.example.darkmode.Profile;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darkmode.R;
import com.example.darkmode.ThemeSettings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView user_name,user_email,user_profession,user_blood_group,user_contact;
    private CircularImageView circularImageView;

    ProgressBar progressBar;
    ThemeSettings themeSettings;
    private FirebaseAuth mAuth5;
    private String userID;

    private FirebaseFirestore db5;
    private DocumentReference document_reference5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme Settings
        themeSettings = new ThemeSettings(this);
        if(themeSettings.loadNightModeState()==false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Profile");

        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_profession = findViewById(R.id.profession);
        user_blood_group = findViewById(R.id.blood_group);
        user_contact = findViewById(R.id.contact);
        progressBar = findViewById(R.id.progress_loading);
        circularImageView = findViewById(R.id.user_image);

        progressBar = findViewById(R.id.progress_loading);
        progressBar.setVisibility(View.GONE);

        mAuth5 = FirebaseAuth.getInstance();
        userID = mAuth5.getUid();

        db5 = FirebaseFirestore.getInstance();
        document_reference5 = db5.collection("UserDetails").document(userID);

        loadData();
    }

    public void loadData(){

        document_reference5.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    progressBar.setVisibility(View.VISIBLE);

                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String profession = documentSnapshot.getString("profession");
                    String bloodGroup = documentSnapshot.getString("b_group");
                    String contact = documentSnapshot.getString("contact");
                    String url = documentSnapshot.getString("imageUrl");

                    Picasso.get().load(url).error(R.drawable.user_default).into(circularImageView);
                    user_name.setText(name);
                    user_email.setText(email);
                    user_profession.setText(profession);
                    user_blood_group.setText(bloodGroup);
                    user_contact.setText(contact);

                } else {
                    Toast.makeText(ProfileActivity.this, "Information does not exist!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Picasso.get().load(R.drawable.user_default).into(circularImageView);
            }
        });

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent editProfile = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(editProfile);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.delete_profile:
                openDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Are you sure?")
                .setMessage("If you delete this profile, it  will no longer be available in CR's list!")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        document_reference5.delete();
                        Picasso.get().load(R.drawable.user_default).into(circularImageView);
                        user_name.setText("");
                        user_email.setText("");
                        user_profession.setText("");
                        user_blood_group.setText("");
                        user_contact.setText("");

                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
