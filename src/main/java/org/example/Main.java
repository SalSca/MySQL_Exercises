package org.example;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            /*
            connessione al database, connessione allo statement
            utilizzato per eseguire query e creazione di resultSet
            per ritornare valori su java
             */
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newdb","developer", "developer");
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            int verify;
            /*
            creazione della tabella se esiste (IF NOT EXISTS) 'students'
            con specifici valori
             */
            String sql = "CREATE TABLE IF NOT EXISTS students " +
                    "(student_id INT(10) NOT NULL AUTO_INCREMENT, " +
                    " last_name VARCHAR(30) NOT NULL, " +
                    " first_name VARCHAR(30) NOT NULL, " +
                    " PRIMARY KEY (student_id))";
            statement.executeUpdate(sql);

            /*
            Creazione delle liste con valori
            casuali e utilizzando ciclo for riempimento
            della tabella verificando prima se quei valori
            se sono già presenti
             */
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
            /*
            Stampa di tutti i valori contenuti nella tabella students
            utilizzando next() ovvero finché trova valore
             */
            resultSet = statement.executeQuery("Select * from students;");
            System.out.println("\nThe students in the database are:");
            while(resultSet.next()){
                System.out.println("-"+resultSet.getString("first_name")+" "+resultSet.getString("last_name"));
            }
            /*
            Verifica se già esiste una colonna 'country' nella tabella 'students'
            se esiste tramite i metodi:
            next()->sposta il cursore alla prima riga del risultato
            getInt()->restituisce il valore della prima colonna della prima riga del risultato
            se restituisce 1 allora esiste se restituisce 0 allora non esiste
             */
            sql= "SELECT COUNT(*) "+
                    "FROM INFORMATION_SCHEMA.COLUMNS "+
                    "WHERE table_name = 'students' AND column_name = 'country';";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            verify = resultSet.getInt(1);
            if(verify==0){
                sql= "ALTER TABLE students "+
                        "ADD country VARCHAR(30);";
                statement.executeUpdate(sql);
            }

            /*
            Assegno i valori Italy e Germany agli studenti
            sfrutto l'indice del ciclo for per prendere gli
            studenti in base al loro id (pari o dispari)
            se pari assegna Italy se dispari Germany
             */
           for (int i=1; i <= firstNames.size(); i++) {
                    if(i%2==0) {
                        sql = "UPDATE students "+
                              "SET country = 'Italy' "+
                              "WHERE student_id= "+i;
                        statement.executeUpdate(sql);
                    }else {
                        sql = "UPDATE students "+
                              "SET country = 'Germany' "+
                              "WHERE student_id= "+i;
                        statement.executeUpdate(sql);
                    }
            }

           /*
           creazione delle viste per la visualizzazione
           facilitata degli studenti italiani e tedeschi
            */

                sql="CREATE OR REPLACE VIEW italian_students AS "+
                        "SELECT last_name,first_name "+
                        "FROM students "+
                        "WHERE country = 'Italy'";
                statement.executeUpdate(sql);

                sql="CREATE OR REPLACE VIEW german_students AS "+
                        "SELECT last_name,first_name "+
                        "FROM students "+
                        "WHERE country = 'Germany'";
                statement.executeUpdate(sql);

          /*
          Creazione e riempimento delle liste 'italianStudents' e
          'germanStudents'
          */
           List<Student> italianStudents = new ArrayList<>();
           resultSet = statement.executeQuery("SELECT last_name, first_name FROM italian_students");
           while(resultSet.next()){
               String first_name= resultSet.getString("first_name");
               String last_name= resultSet.getString("last_name");
               italianStudents.add(new Student(first_name,last_name));
           }

            System.out.println("\nItalian students:");
            for (Student student : italianStudents) {
                System.out.println("-"+student.getName() + " " + student.getSurname());
            }

            List<Student> germanStudents = new ArrayList<>();
            resultSet = statement.executeQuery("SELECT last_name, first_name FROM german_students");
            while(resultSet.next()){
                String first_name= resultSet.getString("first_name");
                String last_name= resultSet.getString("last_name");
                germanStudents.add(new Student(first_name,last_name));
            }

            System.out.println("\nGerman students:");
            for (Student student : germanStudents) {
                System.out.println("-"+student.getName() + " " + student.getSurname());
            }

            /*
            chiusura di tutte le connessioni
             */
            connection.close();
            statement.close();
            resultSet.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}