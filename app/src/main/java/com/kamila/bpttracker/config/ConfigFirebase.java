package com.kamila.bpttracker.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth authentication;
    private static DatabaseReference firebase;

    //this method return the instance of firebaseDatabase

    public static DatabaseReference getFirebaseDataBase(){
        if( firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;

    }

    //this method returns the instance of FirebaseAuth

    public static FirebaseAuth getFirebaseAuthentication() {
        if(authentication == null){
            authentication = FirebaseAuth.getInstance();
        }
        return authentication;
    }
}
