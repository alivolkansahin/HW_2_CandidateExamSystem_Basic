package com.volkans.implementations;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.volkans.implementations.repository.IExamManager;
import com.volkans.implementations.repository.entities.Candidate;
import com.volkans.implementations.repository.entities.Question;
import com.volkans.implementations.repository.enums.EOptions;
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
		setCandidates(getCandidatesFromDatabase().get());
		prepareExam();
	}
	
	@Override
	public List<Question> prepareExam() {
		boolean check = false;
		while(!check) {
			setExam(readExamFromTextFile().get());
			if(getExam().isEmpty()) {
				System.out.println(Utils.YELLOW_BOLD_BRIGHT+"Therefore questions will be created by using self-written method inside the code..."+Utils.RESET);
				prepareQuestionsFromCodingPage();
				setExam(readExamFromBinFile().get());
			}
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Exam is prepared!" + Utils.RESET);
			check = true;	
		}
		return getExam();	
	}
	
	@Override
	public void prepareQuestionsFromCodingPage() {
		
		Question question1 = new Question("1",10, "Türkiyenin başkenti neresidir?",
										 "İstanbul", "İzmir", "Ankara", "Antalya", EOptions.C);
		Question question2 = new Question("2",10, "(7x9)/3 kaçtır?",
				"24", "21", "22", "23", EOptions.B);	
		Question question3 = new Question("3",11, "İstanbul'un fethi kaç yılında olmuştur?",
				"1437", "1443", "1467", "1453", EOptions.D);	
		Question question4 = new Question("4",9, "Hangisi bir kıta değildir?",
				"Güneydoğu Anadolu", "İç Anadolu", "Kuzeydoğu Anadolu", "Akdeniz Bölgesi", EOptions.C);	
		Question question5 = new Question("5",10, "Suyun kaynama noktası kaçtır?",
				"100", "95", "90", "85", EOptions.A);	
		Question question6 = new Question("6",10, "Hangisi bir ana renk değildir?",
				"Yeşil", "Kırmızı", "Mavi", "Sarı", EOptions.A);	
		Question question7 = new Question("7",10, "Aşağıdakilerden hangisi bir artık yıldır?",
				"2066", "2088", "2042", "2055", EOptions.B);	
		Question question8 = new Question("8",10, "Bilgeadam Boost Java-12 kursunda kaç öğrenci vardır?",
				"13", "14", "15", "16", EOptions.C);	
		Question question9 = new Question("9",10, "Hangisi dünya üzerinde yer alan bir kıta değildir?",
				"Afrika", "Hindistan", "Avrupa", "Asya", EOptions.B);	
		Question question10 = new Question("10",10, "Türkiye Eurovisionda Sertab Erener ile kaç yılında birincilik kazanmıştır?",
				"2000", "2001", "2002", "2003", EOptions.D);
		addQuestionsToList(question1,question2,question3,question4,question5,question6,question7,question8,question9,question10);
		
	}

	@Override
	public void addQuestionsToList(Question... question) {
		List<Question> exam = new LinkedList<>();
		for (Question eachQuestion : question) {
			exam.add(eachQuestion);
		}	
		saveExamToBinFile(exam);
	}
	
	@Override
	public void saveExamToBinFile(List<Question> questionList) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("exam.bin"))){
			oos.writeObject(questionList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public Optional<List<Question>> readExamFromBinFile() {	
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("exam.bin"))){	
			List<Question> exam = (LinkedList<Question>) ois.readObject();
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Questions have been read from 'exam.bin' file!" + Utils.RESET);
			return Optional.of(exam);
		} catch (FileNotFoundException e) {
			System.out.println(Utils.YELLOW_BOLD_BRIGHT + "'exam.bin' is not found!" + Utils.RESET);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Optional.of(Collections.emptyList());
	}
	
	@Override
	public Optional<List<Question>> readExamFromTextFile() {	
		List <String> lines;
		try {
			lines = Files.readAllLines(Path.of("questions.txt"));
			lines.removeIf(line-> line.trim().isEmpty());	 // boş satır var ise onları sil.
		} catch (FileNotFoundException e) {
			System.out.println(Utils.YELLOW_BOLD_BRIGHT + "'questions.txt' is not found!"+ Utils.RESET);
			return Optional.of(Collections.emptyList());
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.of(Collections.emptyList());
		}
		return prepareQuestionsFromReadingTxtFile(lines);
	}
	
	@Override
	public Optional<List<Question>> prepareQuestionsFromReadingTxtFile(List<String> lines) {
		List<Question> exam = new LinkedList<>();
		for(int i=0 ; i<lines.size() ; i+=8) {
			String questionNumber = lines.get(i).substring(5,lines.get(i).length());
			int questionPoint = Integer.parseInt(lines.get(i+1).substring(5,lines.get(i+1).length()));
			String questionContext = lines.get(i+2);
			String questionChoiceA = lines.get(i+3);
			String questionChoiceB = lines.get(i+4);
			String questionChoiceC = lines.get(i+5);
			String questionChoiceD = lines.get(i+6);
			EOptions correctAnswer = null;
			for (EOptions siklar : EOptions.values()) {
				if(siklar.getKey().equals(lines.get(i+7).substring(6,lines.get(i+7).length()))) {
					correctAnswer = siklar;
					break;
				}
			}
			exam.add(new Question(questionNumber, questionPoint, questionContext, questionChoiceA, questionChoiceB, questionChoiceC, questionChoiceD, correctAnswer));	
		}
		System.out.println(Utils.YELLOW_BOLD_BRIGHT + "Questions have been read from 'questions.txt' file!"+ Utils.RESET);
		return Optional.of(exam);
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
				saveCandidatesToDatabase(getCandidates());
				System.out.println(Utils.GREEN_BOLD_BRIGHT +  "Registration successful!"+ Utils.RESET);	
				return;		
			}
			
		}
		
	}

	@Override
	public boolean login() {
		String candidateName = Utils.getStringValue("Name: ");
		String candidateSurname = Utils.getStringValue("Surname: ");
		String password = Utils.getStringValue("Password: ");
		Optional<Candidate> candidate = findCandidateByNameSurnamePassword(candidateName, candidateSurname, password);
		if(candidate.isPresent()) {
			setCandidate(candidate.get());
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Login Success!"+ Utils.RESET);
			return true;
		} else {
			System.out.println(Utils.RED_BOLD_BRIGHT + "User not found!"+ Utils.RESET);
			return false;
		}
	}
	
	private Optional<Candidate> findCandidateByNameSurnamePassword(String candidateName, String candidateSurname, String password) {
		for (Candidate candidate : getCandidates()) {
			if (candidate.getCandidateName().equals(candidateName) && candidate.getCandidateSurname().equals(candidateSurname) 
					&& candidate.getPassword().equals(password)) {
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
			System.out.println(" " + k + "\t " + v.get(0) + "\t     " + v.get(1)+ "\t     \t     " + v.get(2) + "\t\t   " + v.get(3));
		});
		
	}

	@Override
	public void finishExam() {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("candidateResults.txt"))){
			saveCandidatesToDatabase(getCandidates());
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
		System.out.println("\nQuestion Number: " + question.getQuestionNumber() + "\t Points: " +
				question.getQuestionPoint() + "\t Remaining Time: " + showRemainingTime(remainingTime) 
				+ "\n" + question.getQuestionContext());
		question.getQuestionChoices().forEach((k,v)->System.out.println(k+") "+v));		
	}
	
	@Override
	public void saveCandidatesToDatabase(List<Candidate> candidates) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("registeredCandidates.bin"))){
			oos.writeObject(candidates);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public Optional<List<Candidate>> getCandidatesFromDatabase() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("registeredCandidates.bin"))){	
			List<Candidate> candidates = (LinkedList<Candidate>) ois.readObject();
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Candidates have been read from 'registeredCandidates.bin' file!" + Utils.RESET);
			return Optional.of(candidates);
		} catch (FileNotFoundException e) {
			System.out.println(Utils.YELLOW_BOLD_BRIGHT + "There are no registered candidates!" + Utils.RESET);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Optional.of(Collections.emptyList());
		
	}
	
	//getters-setters
	private Candidate getCandidate() {
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
