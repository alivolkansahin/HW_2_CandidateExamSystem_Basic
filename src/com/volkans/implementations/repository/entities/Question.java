package com.volkans.implementations.repository.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.volkans.implementations.repository.enums.EOptions;

public class Question implements Serializable{
	private static int whichQuestion;
	private String questionNumber;
	private int questionPoint;
	private String questionContext;
	private Map<EOptions, String> questionChoices; // EOptions, ChoiceText
	private EOptions correctAnswer;
	
	public Question(String questionNumber, int questionPoint, String questionContext, String questionChoiceA, String questionChoiceB,
			String questionChoiceC,String questionChoiceD,EOptions correctAnswer) {
		super();
		setQuestionNumber(questionNumber);
		setQuestionPoint(questionPoint);
		setQuestionContext(questionContext);
		createQuestionChoices(questionChoiceA, questionChoiceB, questionChoiceC, questionChoiceD);
		setCorrectAnswer(correctAnswer);
	}
	
	public void createQuestionChoices(String questionChoiceA, String questionChoiceB,
			String questionChoiceC,String questionChoiceD) {
		setQuestionChoices(new LinkedHashMap<>());
		getQuestionChoices().put(EOptions.A,questionChoiceA);
		getQuestionChoices().put(EOptions.B,questionChoiceB);
		getQuestionChoices().put(EOptions.C,questionChoiceC);
		getQuestionChoices().put(EOptions.D,questionChoiceD);
		
	}

	public String getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	public int getQuestionPoint() {
		return questionPoint;
	}

	public void setQuestionPoint(int questionPoint) {
		this.questionPoint = questionPoint;
	}

	public String getQuestionContext() {
		return questionContext;
	}

	public void setQuestionContext(String questionContext) {
		this.questionContext = questionContext;
	}

	public Map<EOptions, String> getQuestionChoices() {
		return questionChoices;
	}

	public void setQuestionChoices(Map<EOptions, String> questionChoices) {
		this.questionChoices = questionChoices;
	}

	public EOptions getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(EOptions correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	@Override
	public String toString() {
		return "\nQuestion Number: " + getQuestionNumber() + "\t Points: " + getQuestionPoint() + "\n" 
				+ getQuestionContext() + "\n" + getQuestionChoices() + "\nCorrect Answer: " + getCorrectAnswer();
	}
	
}


