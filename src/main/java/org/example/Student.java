package org.example;

public class Student {

    private String name;
    private String surname;

    public Student(String name, String surname){
        this.name=name;
        this.surname=surname;
    }

    public String getName(){
        return name;
    }
    public String getSurname(){
        return surname;
    }

    @Override
    public String toString() {
        return "name: "+name+"\n"+
               "surname: "+surname+"\n";
    }
}
