package com.example.and_project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.and_project.data.FirebaseRepository;
import com.example.and_project.data.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class UserViewModel extends AndroidViewModel {
    private final FirebaseRepository firebaseRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        firebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<User> getCurrentUser() {
        return firebaseRepository.getUserLiveData();
    }

    public Task<AuthResult> signIn(String email, String password) {
        return firebaseRepository.signIn(email, password);
    }

    public Task<AuthResult> signUp(String email, String password) {
        return firebaseRepository.signUp(email, password);
    }

    public void signOut() {
        firebaseRepository.signOut();
    }
}
