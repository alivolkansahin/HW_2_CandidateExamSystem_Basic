package com.volkans.implementations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.volkans.implementations.repository.IExamManager;
import com.volkans.implementations.repository.entities.Candidate;
import com.volkans.implementations.repository.entities.Question;
import com.volkans.implementations.repository.enums.EUserExamStatus;
import com.volkans.implementations.repository.utilities.Utils;

public class ExamManagerImpl implements IExamManager {
	
	private static final String VALID_CHARACTERS_FOR_NAME_SURNAME_REGEX = "^[a-zA-ZğĞüÜşŞıİöÖçÇ\\s]*$";
	private static final String SUITABLE_CHARACTERS_FOR_PASSWORD_REGEX = "^(?=.*[A-ZA-Z])(?=.*\\d)(?=.*[@$.!%*#?&])[A-Za-z\\d@$.!%*#?&]{3,10}";
	
	private Candidate candidate;
	private List<Question> exam;
	private List<Candidate> candidates;
		
	public ExamManagerImpl() {
		super();
		setCandidates(MenuImpl.getDatabase().readCandidatesDeSerialization().get());
		prepareExam();
	}
	
	@Override
	public List<Question> prepareExam() {
		boolean check = false;
		while(!check) {
			setExam(MenuImpl.getDatabase().readQuestionFromTextFile().get());
			if(getExam().isEmpty()) {
				System.out.println(Utils.YELLOW_BOLD_BRIGHT + "Therefore questions will be created by using self-written method inside the code..." + Utils.RESET);
				MenuImpl.getDatabase().prepareQuestionsFromCodingPage();
				setExam(MenuImpl.getDatabase().readQuestionsDeSerialization().get());
			}
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Exam has been prepared!" + Utils.RESET);
			check = true;	
		}
		return getExam();	
	}	
	
	@Override
	public void register() {
		while (true) {			
			String name = Utils.getStringValue("Name: ");
			String surname = Utils.getStringValue("Surname: ");
			if(!name.matches(VALID_CHARACTERS_FOR_NAME_SURNAME_REGEX) || !surname.matches(VALID_CHARACTERS_FOR_NAME_SURNAME_REGEX)) {
				System.out.println(Utils.RED_BOLD_BRIGHT + "Name or Surname contains invalid characters!"+ Utils.RESET);
				continue;
			}
			while(true) {
				String password = Utils.getStringValue("Enter password: ");
				String rePassword = Utils.getStringValue("Enter repassword: ");
				if (!password.matches(SUITABLE_CHARACTERS_FOR_PASSWORD_REGEX)) {
					System.out.println(Utils.RED_BOLD_BRIGHT +  "Invalid combination for password! Valid password conditions are as follows:\n" + Utils.YELLOW_BOLD_BRIGHT
							+ "1)At least one uppercase letter (A-Z or a-z)\n"
							+ "2)At least one digit (0-9)\n"
							+ "3)At least one special character (@, $, !, %, *, #, ?, or &)\n"
							+ "4)The total character length should be between 3 and 10 characters\n" + Utils.GREEN_BOLD_BRIGHT
							+ "(Example: P@ssword1)"  + Utils.RESET);
					continue;
				}
				if(!password.equals(rePassword)) {
					System.out.println(Utils.RED_BOLD_BRIGHT +  "Password and repassword does not match!"+ Utils.RESET);
					continue;
				}
				if(getCandidates().isEmpty()) {
					setCandidates(new LinkedList<>());
				}
				getCandidates().add(new Candidate(name, surname, password));
				MenuImpl.getDatabase().writeCandidatesSerialization(getCandidates());
//				saveCandidateToDatabase(getCandidate()); // bakılacak...
				System.out.println(Utils.GREEN_BOLD_BRIGHT +  "Registration successful! Your ID is "+ getCandidates().get(getCandidates().size()-1).getCandidateID() + Utils.RESET);	
				return;		
			}
			
		}
		
	}

	@Override
	public boolean login() {
		String candidateID = Utils.getStringValue("ID: ");
		String password = Utils.getStringValue("Password: ");
		Optional<Candidate> candidate = findCandidateByNameSurnamePassword(candidateID, password);
		if(candidate.isPresent()) {
			setCandidate(candidate.get());
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Login Success!"+ Utils.RESET);
			return true;
		} else {
			System.out.println(Utils.RED_BOLD_BRIGHT + "User not found! Check ID and/or Password"+ Utils.RESET);
			return false;
		}
	}
	
	private Optional<Candidate> findCandidateByNameSurnamePassword(String candidateID, String password) {
		for (Candidate candidate : getCandidates()) {
			if (candidate.getCandidateID().equals(candidateID) && candidate.getPassword().equals(password)) {
				return Optional.of(candidate);
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean startExam() {
		if(getCandidate().getExamStatus().equals(EUserExamStatus.TOOK)) {
			System.out.println(Utils.RED + "You had already taken the exam!" + Utils.RESET);
			return false;
		}
		getCandidate().setExamStatus(EUserExamStatus.TOOK);
		getCandidate().setExamScore(0);
		getCandidate().setExamEvaluation(new LinkedHashMap<>());
		for (Question question : getExam()) {
			getCandidate().getExamEvaluation().put("Q" + question.getQuestionNumber(), new ArrayList<>());
			getCandidate().getExamEvaluation().get("Q" + question.getQuestionNumber()).add(String.valueOf(question.getQuestionPoint()));
			getCandidate().getExamEvaluation().get("Q" + question.getQuestionNumber()).add(question.getCorrectAnswer().getKey());
		}
		return true;
		
	}

	@Override
	public boolean checkCandidateAnswer(Question question, String candidateAnswer) {
		getCandidate().getExamEvaluation().get("Q" + question.getQuestionNumber()).add(candidateAnswer.toUpperCase());
		if(candidateAnswer.equalsIgnoreCase(question.getCorrectAnswer().getKey())) {
			getCandidate().setExamScore(getCandidate().getExamScore() + question.getQuestionPoint());
			getCandidate().getExamEvaluation().get("Q" + question.getQuestionNumber()).add("CORRECT");
			return true;
		} else {
			getCandidate().getExamEvaluation().get("Q" + question.getQuestionNumber()).add("WRONG");
			return true;
		}
		
	}
	
	@Override
	public void showExamResult() {
		if(getCandidate().getExamStatus().equals(EUserExamStatus.DIDNTTAKE)) {
			System.out.println(Utils.RED_BOLD_BRIGHT + "You haven't take the exam!" + Utils.RESET);
			return;
		}
		System.out.println("Q.No\tPoint\tCorrectAnswer\tYourAnswer\tEvaluation");
		System.out.println("----\t-----\t-------------\t----------\t----------");
		getCandidate().getExamEvaluation().forEach((k,v) -> {
			System.out.println(" " + k + "\t " + v.get(0) + "\t     " + v.get(1)+ "\t     \t     " + v.get(2) + "\t\t   " 
					+ (v.get(3).startsWith("C") ? Utils.GREEN_BOLD_BRIGHT + v.get(3) : Utils.RED_BOLD_BRIGHT + v.get(3)) + Utils.RESET);
		});
		
	}

	@Override
	public void finishExam() {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("candidateResults.txt"))){
			MenuImpl.getDatabase().writeCandidatesSerialization(getCandidates());
			for (Candidate candidate : getCandidates()) {
				bw.write(candidate.toString()+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Utils.GREEN_BOLD_BRIGHT + "Exam is finished!");
		System.out.println("Results have been written to 'candidateResults.txt' file" + Utils.RESET);
	}


	@Override
	public String showRemainingTime(Duration remainingTime) {
		return String.format("%02d:%02d%n", remainingTime.toMinutes(), remainingTime.minusMinutes(remainingTime.toMinutes()).getSeconds());	
	}

	@Override
	public void informCandidateForExam() {
		System.out.println(Utils.YELLOW_BOLD_BRIGHT + "There are 10 questions.\n"
				+ "Each questions have different points associated to them \n"
				+ "Exam time is 30 minutes. Timer starts as soon as you start your exam \n"
				+ "You can see your evaluation result after you took the exam\n" + Utils.GREEN_BOLD_BRIGHT
				+ "Good luck! :)" + Utils.RESET);	
	}

	@Override
	public void showCurrentQuestion(Duration remainingTime, Question question) {
		System.out.println("\nQuestion Number: " + question.getQuestionNumber() + "     Points: " +
				question.getQuestionPoint() + Utils.YELLOW_BOLD_BRIGHT + "     Remaining Time: " + showRemainingTime(remainingTime) 
				+ Utils.RESET + question.getQuestionContext());
		question.getQuestionChoices().forEach((k,v)->System.out.println(k+") "+v));		
	}
	
		
//	public void saveCandidateToDatabase(Candidate candidate) {
//		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("registeredCandidates.bin",true))){
//			oos.writeObject(candidate);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
//	}
	
	//getters-setters
	public Candidate getCandidate() {
		return candidate;
	}

	private void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public List<Question> getExam() {
		return exam;
	}

	private void setExam(List<Question> exam) {
		this.exam = exam;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}

}
