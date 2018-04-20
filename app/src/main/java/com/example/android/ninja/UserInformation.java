package com.example.android.ninja;


    public class UserInformation {
        private String Name;
        private String Number;


        public void setName(String name) {
            Name = name;
        }

        public void setNumber(String number) {
            Number = number;
        }

        public UserInformation(String Name, String Number){
            this.Name = Name;
            this.Number=Number;

        }
        UserInformation(){

        }

        public String getName() {
            return Name;
        }

        public String getNumber() {
            return Number;
        }
    }

