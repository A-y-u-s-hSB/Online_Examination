import java.util.*;

class User {
    private String username;
    private String password;
    private String name;
    private String email;

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
        System.out.println("Profile updated successfully.");
    }

    public void displayProfile() {
        System.out.println("Profile Information:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}

class Exam {
    private List<Question> questions;
    private Map<Integer, Character> answers; // To store user-selected answers
    private int score;
    private int timeLimit; // Time limit in seconds
    private Timer timer;
    private boolean isSubmitted;

    public Exam() {
        this.questions = new ArrayList<>();
        this.answers = new HashMap<>();
        this.score = 0;
        this.timeLimit = 60; // Example: 60 seconds for the whole exam
        this.isSubmitted = false;
        loadQuestions();
    }

    // Load sample MCQ questions
    private void loadQuestions() {
        questions.add(new Question(1, "What is the capital of France?", 'A', new String[]{"A. Paris", "B. London", "C. Rome", "D. Berlin"}));
        questions.add(new Question(2, "What is the highest mountain?", 'B', new String[]{"A. K2", "B. Mount Everest", "C. Kilimanjaro", "D. Denali"}));
        questions.add(new Question(3, "Who invented Java?", 'C', new String[]{"A. Elon Musk", "B. Bill Gates", "C. James Gosling", "D. Larry Page"}));
    }

    // Start the exam with a timer
    public void startExam() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting the exam. You have " + timeLimit + " seconds to complete it.");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSubmit();
            }
        }, timeLimit * 1000); // Auto-submit after time limit

        for (Question q : questions) {
            System.out.println(q);
            System.out.print("Enter your answer (A/B/C/D): ");
            char answer = scanner.next().toUpperCase().charAt(0);
            answers.put(q.getId(), answer);
        }

        submitExam();
    }

    // Submit the exam manually or automatically
    public void submitExam() {
        if (isSubmitted) return;
        timer.cancel();
        isSubmitted = true;
        calculateScore();
        System.out.println("Exam submitted successfully. Your score is: " + score + "/" + questions.size());
    }

    // Auto-submit when time is up
    private void autoSubmit() {
        if (!isSubmitted) {
            System.out.println("\nTime's up! Auto-submitting the exam.");
            submitExam();
        }
    }

    // Calculate the score
    private void calculateScore() {
        for (Question q : questions) {
            if (answers.getOrDefault(q.getId(), ' ') == q.getCorrectAnswer()) {
                score++;
            }
        }
    }
}

// Class to represent an individual MCQ question
class Question {
    private int id;
    private String questionText;
    private char correctAnswer;
    private String[] options;

    public Question(int id, String questionText, char correctAnswer, String[] options) {
        this.id = id;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(". ").append(questionText).append("\n");
        for (String option : options) {
            sb.append(option).append("\n");
        }
        return sb.toString();
    }
}

// Main class to run the Online Examination System
public class OnlineExaminationSystem {
    private static User currentUser;
    private static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        // Adding a test user
        users.put("user1", new User("user1", "password", "John Doe", "john@example.com"));

        // Display login menu
        login();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Update Profile");
            System.out.println("2. Change Password");
            System.out.println("3. Start Exam");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    updateProfile();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    startExam();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 4);
    }

    // User login function
    private static void login() {
        Scanner scanner = new Scanner(System.in);
        boolean loginSuccessful = false;

        while (!loginSuccessful) {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            User user = users.get(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                loginSuccessful = true;
                System.out.println("Login successful! Welcome " + currentUser.getUsername() + ".");
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    // Update profile function
    private static void updateProfile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        currentUser.updateProfile(newName, newEmail);
        currentUser.displayProfile();
    }

    // Change password function
    private static void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (currentUser.getPassword().equals(currentPassword)) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            currentUser.setPassword(newPassword);
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Incorrect current password.");
        }
    }

    // Start the exam
    private static void startExam() {
        Exam exam = new Exam();
        exam.startExam();
    }
}
