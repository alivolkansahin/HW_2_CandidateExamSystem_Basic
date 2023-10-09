package com.volkans.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.volkans.implementations.repository.IDatabase;
import com.volkans.implementations.repository.entities.Candidate;
import com.volkans.implementations.repository.entities.Question;
import com.volkans.implementations.repository.enums.EOptions;
import com.volkans.implementations.repository.utilities.Utils;

public class DatabaseImpl implements IDatabase{

	@Override
	public Optional<List<Question>> readQuestionFromTextFile() {	
		List <String> lines;
		try {
			lines = Files.readAllLines(Path.of("questions.txt"));
			lines.removeIf(line-> line.trim().isEmpty());	 // boş satır var ise onları sil.
			return prepareQuestions(lines);
		} catch (FileNotFoundException e) {
			System.out.println(Utils.YELLOW_BOLD_BRIGHT + "'questions.txt' is not found!"+ Utils.RESET);
			return Optional.of(Collections.emptyList());
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.of(Collections.emptyList());
		}		
	}

	@Override
	public Optional<List<Question>> prepareQuestions(List<String> lines) {
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
			for (EOptions correctKey : EOptions.values()) {
				if(correctKey.getKey().equals(lines.get(i+7).substring(6,lines.get(i+7).length()))) {
					correctAnswer = correctKey;
					break;
				}
			}
			exam.add(new Question(questionNumber, questionPoint, questionContext, questionChoiceA, questionChoiceB, questionChoiceC, questionChoiceD, correctAnswer));	
		}
		System.out.println(Utils.GREEN_BOLD_BRIGHT + "Questions have been read from 'questions.txt' file!"+ Utils.RESET);
		return Optional.of(exam);
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
		writeQuestionsSerialization(exam);
	}

	@Override
	public void writeQuestionsSerialization(List<Question> questionList) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("questions.bin"))){
			oos.writeObject(questionList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public Optional<List<Question>> readQuestionsDeSerialization() {	
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("questions.bin"))){	
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
	public void writeCandidatesSerialization(List<Candidate> candidates) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("registeredCandidates.txt"))){
			oos.writeObject(candidates);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public Optional<List<Candidate>> readCandidatesDeSerialization() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("registeredCandidates.txt"))){	
			List<Candidate> candidates = (LinkedList<Candidate>) ois.readObject();
			System.out.println(Utils.GREEN_BOLD_BRIGHT + "Candidates have been read from 'registeredCandidates.txt' file!" + Utils.RESET);
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

}
