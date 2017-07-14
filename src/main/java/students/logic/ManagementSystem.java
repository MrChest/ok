package students.logic;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ManagementSystem {

    private static Connection conn;

    // Для шаблона Singletone статическая переменная
    private static ManagementSystem instance;

    // закрытый конструктор
    private ManagementSystem() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/students?autoReconnect=true&useSSL=false";
            conn = DriverManager.getConnection(url, "root", "admin");
            System.out.println("1");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    // метод getInstance - проверяtт, инициализирована ли статическая
    // переменная (в случае надобности делает это) и возвращает ее
    public static synchronized ManagementSystem getInstance() {
        if (instance == null) {
            instance = new ManagementSystem();
        }
        return instance;
    }

    // Метод создает две группы и помещает их в коллекцию для групп
    public Vector<Group> getGroups() throws SQLException{
        Vector<Group> groups = new Vector<>();

        String query = "SELECT group_id, groupName, curator, speciality FROM groups";

        try(Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()){
                Group group = new Group();
                group.setGroupId(rs.getInt(1));
                group.setNameGroup(rs.getString(2));
                group.setCurator(rs.getString(3));
                group.setSpeciality(rs.getString(4));

                groups.add(group);
            }
        }
        return groups;
    }

    // Получить список всех студентов
    public Collection<Student> getAllStudents() throws SQLException{
        Vector<Student> students = new Vector<>();

        String query = "SELECT student_id, firstName, patronymic, surName, " +
                "sex, dateOfBirth, group_id, educationYear FROM students " +
                "ORDER BY surName, firstName, patronymic";

        try(Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()){
                Student student = new Student(rs);
                students.add(student);
            }
        }
        return students;
    }

    // Получить список студентов для определенной группы
    public Collection<Student> getStudentsFromGroup(Group group, int year) throws SQLException{
        List<Student> students = new ArrayList<Student>();

        String query = "SELECT student_id, firstName, patronymic, surName, " +
                "sex, dateOfBirth, group_id, educationYear FROM students " +
                "WHERE group_id=? AND educationYear=? " +
                "ORDER BY surName, firstName, patronymic";

        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, group.getGroupId());
            st.setInt(2,group.getGroupId());
            try (ResultSet rs = st.executeQuery(query)){
                 while (rs.next()){
                     Student student = new Student(rs);
                     students.add(student);
                 }
             }
        }
        return students;
    }

    // Перевести студентов из одной группы с одним годом обучения в другую группу с другим годом обучения
    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException{
        String query = "UPDATE students SET group_id=?, educationYear=?" +
                                    "WHERE group_id=? AND educationYear=?";
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, newGroup.getGroupId());
            st.setInt(2, oldYear);
            st.setInt(3, oldGroup.getGroupId());
            st.setInt(4, newYear);
            st.execute();
        }
    }

    // Удалить всех студентов из определенной группы
    public void removeStudentsFromGroup(Group group, int year) throws SQLException{
        String query = "DELETE FROM students WHERE group_id=? AND  educationYear=?";

        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, group.getGroupId());
            st.setInt(2, year);
            st.execute();
        }
    }

    // Добавить студента
    public void insertStudent(Student student) throws SQLException{
        String query = "INSERT INTO students " +
                "(firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear) "+
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setString(1, student.getFirstName());
            st.setString(2, student.getPatronymic());
            st.setString(3, student.getSurName());
            st.setString(4, new String(new char[]{student.getSex()}));
            st.setDate(5, new Date(student.getDateOfBirth().getTime()));
            st.setInt(6, student.getGroupId());
            st.setInt(7, student.getEducationYear());
            st.execute();
        }
    }

    // Обновить данные о студенте
    public void updateStudent(Student student) throws SQLException{
        String query = "UPDATE students SET " +
                "firstName=?, patronymic=?, surName=?, sex=?, dateOfBirth=?, group_id=?, educationYear=? " +
                "WHERE students_id=?";

        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setString(1, student.getFirstName());
            st.setString(2, student.getPatronymic());
            st.setString(3, student.getSurName());
            st.setString(4, new String(new char[]{student.getSex()}));
            st.setDate(5, new Date(student.getDateOfBirth().getTime()));
            st.setInt(6, student.getGroupId());
            st.setInt(7, student.getEducationYear());
            st.setInt(8, student.getStudentId());
            st.execute();
        }
    }

    // Удалить студента
    public void deleteStudent(Student student) throws SQLException{
        String query = "DELETE FROM students WHERE students_id=?";

        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, student.getGroupId());
            st.execute();
        }
    }

    // Этот код позволяет нам изменить кодировку
    // Такое может произойти если используется IDE - например NetBeans.
    // Тогда вы получаете просто одни вопросы, что крайне неудобно читать
    public static void printString(Object s) {
        try {
            System.out.println(new String(s.toString().getBytes("windows-1251"), "windows-1252"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

    }

    public static void printString() {
        System.out.println();
    }
}
