package students.frame;

import students.logic.Student;

import javax.swing.table.AbstractTableModel;
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


    public Object getValueAt(int column) {
        String[] colNames = {"Фамилия", "Имя", "Отчество", "Дата"};
        return colNames[column];
    }
}
