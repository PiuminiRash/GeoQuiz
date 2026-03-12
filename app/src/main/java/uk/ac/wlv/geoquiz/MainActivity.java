package uk.ac.wlv.geoquiz;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Language Buttons
    private Button btnEnglish;
    private Button btnSinhala;
    private Button btnTamil;

    // Answer Buttons
    private Button trueButton;
    private Button falseButton;

    private ImageButton mNextButton;
    private ImageButton mPreviousButton;

    private Button mCallButton;

    private Button hintButton;

    // Progress
    private ProgressBar progressBar;
    private TextView progressRight;
    private TextView progressLeft;

    // Question View
    private TextView questionTitle;
    private TextView questionText;

    // Accuracy
    private TextView mAccuracyTextView;
    private int correctAnswers = 0;
    private int answeredQuestions = 0;

    private int currentQuestionIndex = 0;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, true),
            new Question(R.string.question4, false),
            new Question(R.string.question5, true),
            new Question(R.string.question6, false),
            new Question(R.string.question7, true),
            new Question(R.string.question8, true),
            new Question(R.string.question9, true),
            new Question(R.string.question10, false)
    };

    private int mCurrentIndex = 0;
    private int mCorrectCount = 0;
    private int mAnsweredCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if(savedInstanceState != null){
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
            correctAnswers = savedInstanceState.getInt("correctAnswers");
            answeredQuestions = savedInstanceState.getInt("answeredQuestions");
        }

        // Language buttons
        btnEnglish = findViewById(R.id.btnEnglish);
        btnSinhala = findViewById(R.id.btnSinhala);
        btnTamil = findViewById(R.id.btnTamil);

        btnEnglish.setOnClickListener(v -> setLocale("en"));
        btnSinhala.setOnClickListener(v -> setLocale("si"));
        btnTamil.setOnClickListener(v -> setLocale("ta"));

        // Progress views
        progressBar = findViewById(R.id.quizProgress);
        progressRight = findViewById(R.id.progressRight);
        progressLeft = findViewById(R.id.progressLeft);

        mCallButton = findViewById(R.id.call_button);

        // Question views
        questionTitle = findViewById(R.id.question_title);
        questionText = findViewById(R.id.question_text_view);

        // Buttons
        mNextButton = findViewById(R.id.next_button);
        mPreviousButton = findViewById(R.id.previous_button);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);

        hintButton = findViewById(R.id.hintButton);

        // Accuracy view
        mAccuracyTextView = findViewById(R.id.accuracyText);

        loadQuestion();
        updateProgress();
        updateAccuracy();

        trueButton.setOnClickListener(v -> checkAnswer(true));
        falseButton.setOnClickListener(v -> checkAnswer(false));
        hintButton.setOnClickListener(v -> showHint());

        mNextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < mQuestionBank.length - 1) {
                currentQuestionIndex++;
                loadQuestion();
                updateProgress();
                resetButtonColors();
            }
        });

        mPreviousButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadQuestion();
                updateProgress();
                resetButtonColors();
            }
        });

        // Call button
        mCallButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0771234567"));
            startActivity(intent);
        });

    }

    private void loadQuestion() {
        int questionNumber = currentQuestionIndex + 1;
        questionTitle.setText("Question " + questionNumber);
        questionText.setText(mQuestionBank[currentQuestionIndex].getTextResId());
    }

    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer = mQuestionBank[currentQuestionIndex].isAnswerTrue();

        answeredQuestions++;

        if (userAnswer == correctAnswer) {
            correctAnswers++;
        }

        updateAccuracy();
    }

    private void showHint() {
        boolean correctAnswer = mQuestionBank[currentQuestionIndex].isAnswerTrue();

        if (correctAnswer) {
            trueButton.setBackgroundResource(R.drawable.answer_correct_bg);
        } else {
            falseButton.setBackgroundResource(R.drawable.answer_correct_bg);
        }
    }

    private void resetButtonColors() {
        trueButton.setBackgroundResource(R.drawable.answer_default_bg);
        falseButton.setBackgroundResource(R.drawable.answer_default_bg);
    }

    private void updateAccuracy() {
        if (answeredQuestions == 0) {
            mAccuracyTextView.setText("0%");
        } else {
            int accuracy = (correctAnswers * 100) / answeredQuestions;
            mAccuracyTextView.setText(accuracy + "%");
        }
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    private void updateProgress() {
        int totalQuestions = mQuestionBank.length;
        int currentQuestion = currentQuestionIndex + 1;

        int percentage = (currentQuestion * 100) / totalQuestions;
        progressBar.setProgress(percentage);

        progressRight.setText(currentQuestion + "-" + totalQuestions);

        int remaining = totalQuestions - currentQuestion;
        progressLeft.setText(remaining + "-" + totalQuestions);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("currentQuestionIndex", currentQuestionIndex);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putInt("answeredQuestions", answeredQuestions);
    }
}