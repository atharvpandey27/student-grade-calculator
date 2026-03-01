import java.util.*;

/**
 * Student Grade Calculator
 * A Java console application to manage student grades,
 * compute GPA, and generate subject-wise reports.
 *
 * @author Atharv Pandey
 */
public class GradeCalculator {

    // Grade boundaries
    static final int A_PLUS  = 90;
    static final int A       = 80;
    static final int B_PLUS  = 70;
    static final int B       = 60;
    static final int C       = 50;
    static final int D       = 40;

    // Map of student records: name -> list of Subject objects
    static Map<String, List<Subject>> studentDB = new LinkedHashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   Student Grade Calculator v1.0  ║");
        System.out.println("╚══════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrades();
                case 3 -> viewReport();
                case 4 -> viewAllStudents();
                case 5 -> compareStudents();
                case 6 -> { running = false; System.out.println("\nGoodbye!"); }
                default -> System.out.println("Invalid option. Try again.\n");
            }
        }
    }

    static void printMenu() {
        System.out.println("┌──────────────────────────┐");
        System.out.println("│  1. Add Student           │");
        System.out.println("│  2. Add/Update Grades     │");
        System.out.println("│  3. View Student Report   │");
        System.out.println("│  4. View All Students     │");
        System.out.println("│  5. Compare Two Students  │");
        System.out.println("│  6. Exit                  │");
        System.out.println("└──────────────────────────┘");
    }

    // ─── ADD STUDENT ────────────────────────────────────────────
    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty.\n"); return; }
        if (studentDB.containsKey(name)) {
            System.out.println("Student '" + name + "' already exists.\n");
            return;
        }
        studentDB.put(name, new ArrayList<>());
        System.out.println("✓ Student '" + name + "' added.\n");
    }

    // ─── ADD GRADES ─────────────────────────────────────────────
    static void addGrades() {
        String name = pickStudent(); if (name == null) return;
        System.out.print("Enter subject name: ");
        String subject = sc.nextLine().trim();
        double marks = readDouble("Enter marks (0-100): ");
        if (marks < 0 || marks > 100) { System.out.println("Marks must be between 0 and 100.\n"); return; }

        List<Subject> subjects = studentDB.get(name);
        // Update if subject already exists
        for (Subject s : subjects) {
            if (s.name.equalsIgnoreCase(subject)) {
                s.marks = marks;
                System.out.printf("✓ Updated %s for %s: %.1f (%s)%n%n", subject, name, marks, getGrade(marks));
                return;
            }
        }
        subjects.add(new Subject(subject, marks));
        System.out.printf("✓ Added %s for %s: %.1f (%s)%n%n", subject, name, marks, getGrade(marks));
    }

    // ─── VIEW REPORT ────────────────────────────────────────────
    static void viewReport() {
        String name = pickStudent(); if (name == null) return;
        List<Subject> subjects = studentDB.get(name);
        if (subjects.isEmpty()) { System.out.println("No grades recorded yet.\n"); return; }

        System.out.println("\n══════════════════════════════════════");
        System.out.printf("  Report Card: %-24s%n", name);
        System.out.println("══════════════════════════════════════");
        System.out.printf("  %-20s %6s  %5s%n", "Subject", "Marks", "Grade");
        System.out.println("  ──────────────────────────────────");

        double total = 0;
        Subject highest = subjects.get(0), lowest = subjects.get(0);

        for (Subject s : subjects) {
            System.out.printf("  %-20s %6.1f  %5s%n", s.name, s.marks, getGrade(s.marks));
            total += s.marks;
            if (s.marks > highest.marks) highest = s;
            if (s.marks < lowest.marks) lowest = s;
        }

        double avg = total / subjects.size();
        System.out.println("  ──────────────────────────────────");
        System.out.printf("  %-20s %6.1f  %5s%n", "AVERAGE", avg, getGrade(avg));
        System.out.printf("  GPA (10-point scale):   %.2f%n", toGPA(avg));
        System.out.printf("  Best Subject:           %s (%.1f)%n", highest.name, highest.marks);
        System.out.printf("  Needs Improvement:      %s (%.1f)%n", lowest.name, lowest.marks);
        System.out.printf("  Result:                 %s%n", avg >= D ? "PASS ✓" : "FAIL ✗");
        System.out.println("══════════════════════════════════════\n");
    }

    // ─── VIEW ALL ───────────────────────────────────────────────
    static void viewAllStudents() {
        if (studentDB.isEmpty()) { System.out.println("No students added yet.\n"); return; }
        System.out.println("\n  Registered Students:");
        System.out.println("  ────────────────────────────────────");
        System.out.printf("  %-20s %8s  %6s%n", "Name", "Subjects", "Avg");
        for (Map.Entry<String, List<Subject>> e : studentDB.entrySet()) {
            List<Subject> subs = e.getValue();
            double avg = subs.isEmpty() ? 0 : subs.stream().mapToDouble(s -> s.marks).average().orElse(0);
            System.out.printf("  %-20s %8d  %6.1f (%s)%n", e.getKey(), subs.size(), avg, subs.isEmpty() ? "-" : getGrade(avg));
        }
        System.out.println();
    }

    // ─── COMPARE ────────────────────────────────────────────────
    static void compareStudents() {
        if (studentDB.size() < 2) { System.out.println("Need at least 2 students to compare.\n"); return; }
        System.out.println("Select first student:"); String s1 = pickStudent(); if (s1 == null) return;
        System.out.println("Select second student:"); String s2 = pickStudent(); if (s2 == null) return;
        if (s1.equals(s2)) { System.out.println("Please choose two different students.\n"); return; }

        double avg1 = getAvg(s1), avg2 = getAvg(s2);
        System.out.println("\n  ── Comparison ──");
        System.out.printf("  %-20s  Avg: %.1f (%s)%n", s1, avg1, getGrade(avg1));
        System.out.printf("  %-20s  Avg: %.1f (%s)%n", s2, avg2, getGrade(avg2));
        System.out.printf("  Winner: %s (by %.1f marks)%n%n",
            avg1 >= avg2 ? s1 : s2, Math.abs(avg1 - avg2));
    }

    // ─── HELPERS ────────────────────────────────────────────────
    static String pickStudent() {
        if (studentDB.isEmpty()) { System.out.println("No students added yet.\n"); return null; }
        List<String> names = new ArrayList<>(studentDB.keySet());
        System.out.println("Select student:");
        for (int i = 0; i < names.size(); i++)
            System.out.printf("  %d. %s%n", i + 1, names.get(i));
        int idx = readInt("Enter number: ") - 1;
        if (idx < 0 || idx >= names.size()) { System.out.println("Invalid selection.\n"); return null; }
        return names.get(idx);
    }

    static String getGrade(double marks) {
        if (marks >= A_PLUS) return "A+";
        if (marks >= A)      return "A";
        if (marks >= B_PLUS) return "B+";
        if (marks >= B)      return "B";
        if (marks >= C)      return "C";
        if (marks >= D)      return "D";
        return "F";
    }

    static double toGPA(double marks) {
        if (marks >= A_PLUS) return 10.0;
        if (marks >= A)      return 9.0;
        if (marks >= B_PLUS) return 8.0;
        if (marks >= B)      return 7.0;
        if (marks >= C)      return 6.0;
        if (marks >= D)      return 5.0;
        return 0.0;
    }

    static double getAvg(String name) {
        List<Subject> subs = studentDB.get(name);
        return subs.isEmpty() ? 0 : subs.stream().mapToDouble(s -> s.marks).average().orElse(0);
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        try { int v = Integer.parseInt(sc.nextLine().trim()); return v; }
        catch (NumberFormatException e) { return -1; }
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}

// ─── Subject Model ───────────────────────────────────────────────
class Subject {
    String name;
    double marks;
    Subject(String name, double marks) { this.name = name; this.marks = marks; }
}
