package com.volkans.implementations.repository;

import java.time.Duration;
import java.util.List;
import com.volkans.implementations.repository.entities.Question;

public interface IExamManager {	
	List<Question> prepareExam();
	void register();
	boolean login();
	void informCandidateForExam();
	boolean startExam();
	void showCurrentQuestion(Duration remainingTime, Question question);
	String showRemainingTime(Duration remainingTime);
	boolean checkCandidateAnswer(Question question, String candidateAnswer);
	void finishExam();
	void showExamResult();
}
