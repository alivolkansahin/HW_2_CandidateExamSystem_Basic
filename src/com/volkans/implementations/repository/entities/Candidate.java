package com.volkans.implementations.repository.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.volkans.implementations.repository.enums.EUserExamStatus;
import com.volkans.implementations.repository.utilities.Utils;

public class Candidate implements Serializable {
	private static Random rnd = new Random();
	private static long idGenerate = 12345;
	private String candidateName;
	private String candidateSurname;
	private String candidateID;
	private String password;
	private EUserExamStatus examStatus;
	private Map<String,List<String>> examEvaluation; // 1.soru - doğru , 2.soru - yanlış gibi.
	private int examScore;					         // total sonuç
	
	public Candidate(String candidateName, String candidateSurname, String password) {
		super();
		idGenerate += rnd.nextInt(1, 51);
		setCandidateID(String.valueOf(idGenerate));
		setCandidateName(candidateName);
		setCandidateSurname(candidateSurname);
		setPassword(password);
		setExamStatus(EUserExamStatus.DIDNTTAKE);
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getCandidateSurname() {
		return candidateSurname;
	}

	public void setCandidateSurname(String candidateSurname) {
		this.candidateSurname = candidateSurname;
	}

	public String getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(String candidateID) {
		this.candidateID = candidateID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public EUserExamStatus getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(EUserExamStatus examStatus) {
		this.examStatus = examStatus;
	}
	
	public int getExamScore() {
		return examScore;
	}

	public void setExamScore(int examScore) {
		this.examScore = examScore;
	}
	

	public Map<String, List<String>> getExamEvaluation() {
		return examEvaluation;
	}

	public void setExamEvaluation(Map<String, List<String>> examEvaluation) {
		this.examEvaluation = examEvaluation;
	}

	@Override
	public String toString() {
		if(getExamStatus().equals(EUserExamStatus.TOOK)) {
			return "Candidate: " + getCandidateName() + " " + getCandidateSurname() + " ID: " 
					+ getCandidateID() + " Exam Status: " + Utils.GREEN_BOLD_BRIGHT + getExamStatus() + Utils.RESET + " Score: " + getExamScore();
		} else {
			return "Candidate: " + getCandidateName() + " " + getCandidateSurname() + " ID: " 
					+ getCandidateID() + " Exam Status: " + Utils.YELLOW_BOLD_BRIGHT + getExamStatus() + Utils.RESET;
		}
		
	}
	
}
