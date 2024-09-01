import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentManagementGUI extends JFrame {
    private StudentManagementSystem system;
    
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField gradeField;
    private JTextArea displayArea;

    public StudentManagementGUI(StudentManagementSystem system) {
        this.system = system;
        setTitle("Student Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        nameField = new JTextField();
        rollNumberField = new JTextField();
        gradeField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollNumberField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new AddButtonListener());
        JButton displayButton = new JButton("Display All Students");
        displayButton.addActionListener(new DisplayButtonListener());
        JButton searchButton = new JButton("Search Student");
        searchButton.addActionListener(new SearchButtonListener());
        JButton removeButton = new JButton("Remove Student");
        removeButton.addActionListener(new RemoveButtonListener());
        JButton editButton = new JButton("Edit Student");
        editButton.addActionListener(new EditButtonListener());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        inputPanel.add(addButton);
        inputPanel.add(displayButton);

        add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.add(searchButton);
        actionPanel.add(removeButton);
        actionPanel.add(editButton);
        actionPanel.add(exitButton);

        add(actionPanel, BorderLayout.SOUTH);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String rollNumberStr = rollNumberField.getText();
            String grade = gradeField.getText();

            if (name.isEmpty() || rollNumberStr.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                Student student = new Student(name, rollNumber, grade);
                system.addStudent(student);
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "Student added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "Roll number must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DisplayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayArea.setText("");
            for (Student student : system.getStudents()) {
                displayArea.append(student.toString() + "\n");
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rollNumberStr = rollNumberField.getText();
            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                Student student = system.searchStudent(rollNumber);
                if (student != null) {
                    displayArea.setText(student.toString());
                } else {
                    JOptionPane.showMessageDialog(StudentManagementGUI.this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "Roll number must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rollNumberStr = rollNumberField.getText();
            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                if (system.removeStudent(rollNumber)) {
                    JOptionPane.showMessageDialog(StudentManagementGUI.this, "Student removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(StudentManagementGUI.this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "Roll number must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rollNumberStr = rollNumberField.getText();
            String newName = nameField.getText();
            String newGrade = gradeField.getText();

            if (rollNumberStr.isEmpty() || newName.isEmpty() || newGrade.isEmpty()) {
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "All fields are required for editing!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                if (system.editStudent(rollNumber, newName, newGrade)) {
                    JOptionPane.showMessageDialog(StudentManagementGUI.this, "Student details updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(StudentManagementGUI.this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudentManagementGUI.this, "Roll number must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem system = new StudentManagementSystem("students.dat");
            StudentManagementGUI gui = new StudentManagementGUI(system);
            gui.setVisible(true);
        });
    }
}
