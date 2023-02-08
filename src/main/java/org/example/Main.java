package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newdb","developer", "developer");
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            String sql = "CREATE TABLE IF NOT EXISTS students " +
                    "(student_id INT(10) NOT NULL AUTO_INCREMENT, " +
                    " last_name VARCHAR(30) NOT NULL, " +
                    " first_name VARCHAR(30) NOT NULL, " +
                    " PRIMARY KEY (student_id))";
            statement.executeUpdate(sql);

            List<String> lastNames = new ArrayList<>();
            lastNames.add("Rossi");
            lastNames.add("La manna");
            lastNames.add("Gatto");
            lastNames.add("Sassari");
            List<String> firstNames = new ArrayList<>();
            firstNames.add("Mario");
            firstNames.add("Giovanni");
            firstNames.add("Flavia");
            firstNames.add("Giulia");
            for (int i=0; i < firstNames.size(); i++) {
                    sql = "SELECT * FROM students WHERE last_name='" + lastNames.get(i) + "' AND first_name='" + firstNames.get(i) + "'";
                    resultSet=statement.executeQuery(sql);
                    if(!resultSet.next()){
                        sql = "INSERT INTO students (last_name, first_name) VALUES('" + lastNames.get(i) + "','" + firstNames.get(i) + "')";
                        statement.executeUpdate(sql);
                    }
                }
            resultSet = statement.executeQuery("Select * from students;");
            while(resultSet.next()){
                System.out.println(resultSet.getString("first_name")+" "+resultSet.getString("last_name"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}