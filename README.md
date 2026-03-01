# Student Grade Calculator (Java)

A Java console application to manage and analyze student academic performance across multiple subjects.

## Features
- Add multiple students and subjects
- Auto-calculate grade (A+, A, B+, B, C, D, F) and GPA (10-point scale)
- Generate detailed report cards per student
- Identify best and weakest subjects
- Compare performance between two students
- Pass/Fail result based on average

## Tech Stack
`Java` `OOP` `Collections Framework` `Scanner` `HashMap` `ArrayList`

## How to Run

```bash
# Compile
javac GradeCalculator.java

# Run
java GradeCalculator
```

## OOP Concepts Used
- **Classes & Objects**: `Subject` class as a data model
- **Encapsulation**: Subject fields grouped logically
- **Collections**: `HashMap` for student DB, `ArrayList` for subjects
- **Streams**: Used for average calculation with `mapToDouble().average()`

## Sample Output
```
╔══════════════════════════════════╗
║   Student Grade Calculator v1.0  ║
╚══════════════════════════════════╝

  Report Card: Atharv Pandey
══════════════════════════════════════
  Subject              Marks  Grade
  ──────────────────────────────────
  Mathematics           88.0      A
  Physics               75.0     B+
  Digital Electronics   92.0     A+
  ──────────────────────────────────
  AVERAGE               85.0      A
  GPA (10-point scale):   9.00
  Best Subject:           Digital Electronics (92.0)
  Needs Improvement:      Physics (75.0)
  Result:                 PASS ✓
══════════════════════════════════════
```
