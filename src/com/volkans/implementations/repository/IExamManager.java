package com.volkans.implementations.repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.volkans.implementations.repository.entities.Candidate;
import com.volkans.implementations.repository.entities.Question;

public interface IExamManager {
	
	List<Question> prepareExam(); // constructorda çağrılıyor ve aşağıdaki // konulmuş methodları uyguluyor.
	
	// eğer question.txt bulunamadıysa yada okunamadıysa kendi içinde oluşturuyor,bine atıyor ve hep ordan çekiyor.
	void prepareQuestionsFromCodingPage();
	void addQuestionsToList(Question... question);
	void saveExamToBinFile(List<Question> questionList);
	Optional<List<Question>> readExamFromBinFile();
	
	// eğer question.txt bulunup okunabildiyse hep bu 2 method çalışıyor.
	Optional<List<Question>> readExamFromTextFile();
	Optional<List<Question>> prepareQuestionsFromReadingTxtFile(List<String> lines);
	
	void register();
	boolean login();
	void informCandidateForExam();
	boolean startExam();
	void showCurrentQuestion(Duration remainingTime, Question question);
	String showRemainingTime(Duration remainingTime);
	boolean checkCandidateAnswer(Question question, String candidateAnswer);
	void finishExam();
	void showExamResult();
	
	void saveCandidatesToDatabase(List<Candidate> candidates);
	Optional<List<Candidate>> getCandidatesFromDatabase();

}
