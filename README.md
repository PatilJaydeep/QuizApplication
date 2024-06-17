Online Quiz Application Documentation


  Project Overview
      The Online Quiz Application is a Java-based software designed to facilitate the creation,
      management, and participation in quizzes on various topics. It supports user authentication,
      multiple-choice questions, progress tracking, and a leaderboard to rank top performers.

      
  Table of Contents
      1. Setup Instructions
      2. User Guide
      3. Code Documentation
      4. Assumptions and Limitations

      
  Setup Instructions
    Prerequisites
      • Java Development Kit (JDK): Ensure JDK 11 or higher is installed. Download from
    Oracle or OpenJDK.
      • JavaFX SDK: Download from Gluon.
      • MySQL Database: Set up MySQL. Download from MySQL.

      
  Step-by-Step Installation
    1. Download and Setup JavaFX:
      o Extract the JavaFX SDK and note the lib directory path.
    2. Download and Setup MySQL:
      o Install MySQL and create a database named quizapp.
      o Create tables by executing the SQL scripts in db_scripts.sql (you may need to
      create this script containing the necessary CREATE TABLE statements).
    3. Add Libraries to Project:
      o Place the JavaFX and MySQL connector JAR files in the lib directory.
    4. Set Up Environment in VS Code:
      o Open VS Code and create a new Java project.
      o Add the libraries to your project's classpath.
      o Update launch.json with JavaFX VM arguments.
    5. Compile and Run:
      o Use the provided scripts or IDE configurations to compile and run the project.

      
    Directory Structure
    
    OnlineQuizApplication/
    ├── src/
    │ ├── main/
    │ │ ├── java/
    │ │ │ ├── com/
    │ │ │ │ ├── quizapp/
    │ │ │ │ │ ├── App.java
    │ │ │ │ │ ├── model/
    │ │ │ │ │ │ ├── User.java
    │ │ │ │ │ │ ├── Quiz.java
    │ │ │ │ │ │ ├── Question.java
    │ │ │ │ │ ├── dao/
    │ │ │ │ │ │ ├── UserDao.java
    │ │ │ │ │ │ ├── QuizDao.java
    │ │ │ │ │ │ ├── QuestionDao.java
    │ │ │ │ │ ├── service/
    │ │ │ │ │ │ ├── UserService.java
    │ │ │ │ │ │ ├── QuizService.java
    │ │ │ │ │ │ ├── QuestionService.java
    │ │ │ │ │ ├── ui/
    │ │ │ │ │ │ ├── LoginScreen.java
    │ │ │ │ │ │ ├── QuizScreen.java
    │ │ │ │ │ │ ├── AdminScreen.java
    │ │ │ │ │ │ ├── LeaderboardScreen.java
    └── lib/
     ├── javafx-base-17.0.1.jar
     ├── javafx-controls-17.0.1.jar
     ├── javafx-fxml-17.0.1.jar
     ├── mysql-connector-java-8.0.29.jar

 
  Running the Application
    javac -cp "lib/*:." -d out src/main/java/com/quizapp/ui/*.java
    java -cp "out:lib/*:." --module-path /path/to/javafx-sdk/lib --add-modules
    javafx.controls,javafx.fxml com.quizapp.ui.LoginScreen
    Replace /path/to/javafx-sdk/lib with your JavaFX SDK path.

    
  User Guide
  Getting Started
    1. Launch the Application:
      o Run the LoginScreen to start the application.
    2. User Login and Registration:
      o Existing users can log in with their credentials.
      o New users can register by creating an account.
    3. Taking a Quiz:
      o Select a quiz topic from the available list.
      o Answer multiple-choice questions, receiving immediate feedback on each.
    4. Viewing Scores and Leaderboard:
      o View your scores and past quiz attempts.
      o Check the leaderboard for top scorers.
      
  Administrative Functions
    1. Create and Manage Quizzes:
      o Administrators can add, edit, and delete quizzes.
      o Define questions, options, and correct answers for each quiz.
    2. Track User Progress:
      o View and manage user quiz attempts and scores.

      
  Navigating the User Interface
    • LoginScreen: For user authentication.
    • QuizScreen: For taking quizzes.
    • AdminScreen: For quiz management.
    • LeaderboardScreen: For viewing top scorers.

    
  Code Documentation
  Package Structure
    • com.quizapp.model: Contains data models (User, Quiz, Question).
    • com.quizapp.dao: Data access objects for interacting with the database.
    • com.quizapp.service: Service layer for business logic.
    • com.quizapp.ui: User interface components.

      
  Key Classes and Methods
    User.java
      • Represents a user in the system.
      • Key properties: username, password, totalScore, quizCount.
    Quiz.java
      • Represents a quiz with a list of questions.
      • Key properties: quizId, title.
    Question.java
      • Represents a quiz question.
      • Key properties: questionId, text, options, correctAnswer.
    UserDao.java
      • Handles database operations related to users.
      • Methods: saveUser, getUserByUsername, getTopScorers.
    QuizDao.java
      • Handles database operations related to quizzes.
      • Methods: saveQuiz, getAllQuizzes.
    QuestionDao.java
      • Handles database operations related to questions.
      • Methods: saveQuestion, getQuestionsByQuizId.
    UserService.java
      • Contains business logic for user operations.
      • Methods: registerUser, authenticateUser, getTopScorers.
    QuizService.java
      • Contains business logic for quiz operations.
      • Methods: createQuiz, getAllQuizzes.
    QuestionService.java
      • Contains business logic for question operations.
      • Methods: addQuestion, getQuestionsForQuiz.
    LoginScreen.java
      • Handles user login and registration UI.
      • Methods: initialize, handleLogin, handleRegister.
    QuizScreen.java
      • Manages quiz-taking UI.
      • Methods: initialize, loadQuiz, submitAnswer.
    AdminScreen.java
      • Provides UI for quiz management.
      • Methods: initialize, createQuiz, editQuiz.
    LeaderboardScreen.java
      • Displays top scorers UI.
      • Methods: initialize, loadLeaderboardData.

      
  Comments and Annotations
    • Use /** ... */ for class and method documentation.
    • Use // for inline comments explaining logic.
    • Document all public methods and important private methods.

      
  Assumptions and Limitations
    Assumptions
      1. Single-User Admin:
        o Only one user acts as the administrator for managing quizzes.
      2. Simplicity in UI:
        o The application is designed with a basic user interface for ease of use and
          understanding.
      3. Local Database:
        o The database is assumed to be local. Adaptations are needed for remote or cloud
      databases.
      
  Limitations
    1. Limited Question Types:
      o Supports only multiple-choice questions. Other types (e.g., essay, fill-in-theblank) are not supported.
    2. Basic Security:
      o Uses simple password hashing. Does not include advanced security measures like
        OAuth or 2FA.
    3. No Real-Time Updates:
      o The leaderboard and scores do not update in real-time.
    4. Single Language:
      o The application is not internationalized and supports only one language (English).
    5. No Mobile Support:
      o Designed for desktop environments. No mobile optimization is included.

      
  Conclusion
    The Online Quiz Application provides a robust foundation for creating and managing quizzes in
    a Java-based environment. By following the setup instructions and using the provided
    documentation, users can easily set up, extend, and use the application. The documentation
    includes comprehensive setup details, a user guide, and detailed code annotations to help with
    further development and customization.
    For any queries or issues, consult the comments within the code and ensure all dependencies and
    configurations are correctly set up as per the instructions.
