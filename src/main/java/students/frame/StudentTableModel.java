package students.frame;

import students.logic.Student;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.Vector;

/**
 * Created by chestnov.v on 14.07.2017.
 */
public class StudentTableModel extends AbstractTableModel {
    private Vector<Student> students;

    public StudentTableModel(Vector<Student> students) {
        this.students = students;
    }

    @Override
    public int getRowCount() {
        if (students != null){
            return students.size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    // Вернем наименование колонки
    public String getColumnName(int column) {
        String[] colNames = {"Фамилия", "Имя", "Отчество", "Дата"};
        return colNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (students != null) {
            // Получаем из вектора студента
            Student st = (Student) students.get(rowIndex);
            // В зависимости от колонки возвращаем имя, фамилия и т.д.
            switch (columnIndex) {
                case 0:
                    return st.getSurName();
                case 1:
                    return st.getFirstName();
                case 2:
                    return st.getPatronymic();
                case 3:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(
                            st.getDateOfBirth());
            }
        }
        return null;
    }

    // Добавим метод, который возвращает студента по номеру строки
    // Это нам пригодится чуть позже
    public Student getStudent(int rowIndex) {
        if (students != null) {
            if (rowIndex < students.size() && rowIndex >= 0) {
                return (Student) students.get(rowIndex);
            }
        }
        return null;
    }
}
