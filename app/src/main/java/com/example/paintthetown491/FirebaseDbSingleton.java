package com.example.paintthetown491;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//this class is needed to prevent having to create multiple instance of the same reference to firebase
public class FirebaseDbSingleton
{
    private static FirebaseDbSingleton instance=null;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDb;
    public DatabaseReference dbRef;
    public FirebaseUser user;

    private FirebaseDbSingleton()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDb=FirebaseDatabase.getInstance();
        dbRef=firebaseDb.getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
    }

    public static FirebaseDbSingleton getInstance()
    {
        if(instance==null)
            instance= new FirebaseDbSingleton();
        return instance;
    }


}
