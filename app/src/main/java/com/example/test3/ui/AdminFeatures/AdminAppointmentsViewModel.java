package com.example.test3.ui.AdminFeatures;

import androidx.lifecycle.ViewModel;

import com.example.test3.DatabaseHandler.User;

public class AdminAppointmentsViewModel extends ViewModel {
        private User currentUser = new User();

        public void setCurrentUser(User user){
            currentUser = user;
        }

        public User getCurrentUser(){
            return currentUser;
        }

}
