package com.StudyMathsSmarter.StudyMathsSmarter.Quiz;

import com.StudyMathsSmarter.StudyMathsSmarter.Question.Question;
import com.StudyMathsSmarter.StudyMathsSmarter.Question.QuestionRepositoryPostgres;
import com.StudyMathsSmarter.StudyMathsSmarter.Topics;
import com.StudyMathsSmarter.StudyMathsSmarter.User.UserRepositoryPostgres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizService {
    private final QuizRepositoryPostgres quizRepositoryPostgres;
    private final UserRepositoryPostgres userRepositoryPostgres;
    private final QuestionRepositoryPostgres questionRepositoryPostgres;
    private List<Question> quiz;

    @Autowired
    public QuizService(QuizRepositoryPostgres quizRepositoryPostgres, UserRepositoryPostgres userRepositoryPostgres, QuestionRepositoryPostgres questionRepositoryPostgres) {
        this.quizRepositoryPostgres = quizRepositoryPostgres;
        this.userRepositoryPostgres = userRepositoryPostgres;
        this.questionRepositoryPostgres = questionRepositoryPostgres;
    }

    //GET REQUEST
    public List<Quiz> getAllQuiz(){
        return quizRepositoryPostgres.findAll();
    }

    //POST REQUEST
    public void addNewQuiz(Quiz newQuiz){
        quizRepositoryPostgres.save(newQuiz);
    }

    public List<Question> getNewQuiz(){
        List<Question> questionOnes = questionRepositoryPostgres.findAllTopics(Topics.SOLVING_EQUATION);
        List<Question> questionTwos = questionRepositoryPostgres.findAllTopics(Topics.GEOMETRY);
        List<Question> questionThrees = questionRepositoryPostgres.findAllTopics(Topics.TRIGONOMETRY);

        Random rand = new Random();

        quiz = new ArrayList<>(List.of(
                questionOnes.get(rand.nextInt(questionOnes.size())),
                questionOnes.get(rand.nextInt(questionOnes.size())),
                questionTwos.get(rand.nextInt(questionTwos.size())),
                questionTwos.get(rand.nextInt(questionTwos.size())),
                questionThrees.get(rand.nextInt(questionThrees.size())),
                questionThrees.get(rand.nextInt(questionThrees.size()))
        ));

        return quiz;
    }

    //Show result for unregistered user
    public Quiz getQuizResult(List<String> answers){
        int score = 0;

        int[] scores = new int[6];
        for (int i = 0; i < answers.size(); i++){
            if (answers.get(i).equals(quiz.get(i).getAnswer())){
                scores[i] = quiz.get(i).getLevel();
                score = score + quiz.get(i).getLevel();
            }
        }

        double finalScore = score/18.0*100;

        Quiz quiz = new Quiz(0, scores[0], scores[1], scores[2], scores[3], scores[4], scores[5], LocalDate.now(), finalScore);
        return quiz;
    }

    //Show result for registered user and save to database
    public Quiz getQuizResultForUser(int userId, List<String> answers){
        int score = 0;

        int[] scores = new int[6];
        for (int i = 0; i < answers.size(); i++){
            if (answers.get(i).equals(quiz.get(i).getAnswer())){
                scores[i] = quiz.get(i).getLevel();
                score = score + quiz.get(i).getLevel();
            }
        }

        double finalScore = score/18.0*100;

        Quiz quiz = new Quiz(userId, scores[0], scores[1], scores[2], scores[3], scores[4], scores[5], LocalDate.now(), finalScore);

        double result = score/18.0*100;
        quizRepositoryPostgres.save(quiz);

        return quiz;
    }

    public List<Quiz> selectAllQuizForUser(int userId){
        return quizRepositoryPostgres.selectAllQuizForUser(userId);
    }
}
