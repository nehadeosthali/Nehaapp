package com.example.nehaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NehaappLoginActivity" ;
    private Button loginbutton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private GoogleSignInAccount account;
    private static final int RC_SIGN_IN = 0;
    private String notificationToken="dummyToken";
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    //private Button next;
    //private Button guest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        loginbutton = findViewById(R.id.loginbutton);

        loginbutton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();





        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);


        //ADDED!!!
        Button next = (Button) findViewById(R.id.Guest);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myIntent);
            }

        });

    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");



                                Intent intent  = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {
        //if (v.getId() == R.id.loginbutton) {
            switch (v.getId()) {
                case R.id.loginbutton:
                    signIn();

                    break;
                // ...
            }
//            Log.d(TAG,"Login ");
//            Intent intent  = new Intent(this,MainActivity.class);
//            startActivity(intent);
           /* // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);*/
        }

    private void signIn() {
        Log.i(TAG, "onCreate()");
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
       /* Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);*/
    }

 /*   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
        }
*/
 @RequiresApi(api = Build.VERSION_CODES.KITKAT)
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     Log.i(TAG,"onActivityResult()");
     if (requestCode == RC_SIGN_IN) {
         IdpResponse response = IdpResponse.fromResultIntent(data);

         if (resultCode == RESULT_OK) {
             // Successfully signed in
             final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

             FirebaseInstanceId.getInstance().getInstanceId()
                     .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                         @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                         @Override
                         public void onComplete(@NonNull Task<InstanceIdResult> task) {
                             if (!task.isSuccessful()) {
                                 Log.w(TAG, "getInstanceId failed", task.getException());
                                 return;
                             }

                             // Get new Instance ID token
                             notificationToken = Objects.requireNonNull(task.getResult()).getToken();


//                             String email = user.getEmail().replace(".", ",");
//                             FirebaseDatabase database = FirebaseDatabase.getInstance();
//                             DatabaseReference myRef = database.getReference();
//                             myRef.child("users").child("neha").addListenerForSingleValueEvent(new ValueEventListener() {
//                                 @Override
//                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                 }
//
//                                 @Override
//                                 public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                 }
//                             });
                             addUsertoDatabase(firebaseUser);

                             Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                             startActivity(intent);

                         }
                     });




             // ...
         } else {
             Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();

         }
     }
 }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void addUsertoDatabase(final FirebaseUser firebaseUser) {
             final String email;
             email = firebaseUser.getEmail().replace(".",",");

             final DatabaseUser databaseUser = new DatabaseUser(firebaseUser.getEmail());
             databaseUser.setName(firebaseUser.getDisplayName());


        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference("users");
        databaseReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    databaseReference.child(email).setValue(databaseUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    }
