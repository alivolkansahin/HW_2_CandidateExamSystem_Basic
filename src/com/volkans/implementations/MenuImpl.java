package com.volkans.implementations;


import java.time.Duration;
import java.time.LocalTime;
import java.util.ListIterator;

import com.volkans.implementations.repository.IMenu;
import com.volkans.implementations.repository.entities.Question;
import com.volkans.implementations.repository.utilities.Utils;


public class MenuImpl implements IMenu{
	private static DatabaseImpl database = new DatabaseImpl();
	private static ExamManagerImpl manager = new ExamManagerImpl();
	
	private int key;
	private boolean whileCheckMainPage = true;
	private boolean whileCheckCandidatePage = true;
	private boolean whileCheckExamPage = true;

	@Override
	public void runMainPage() {
		System.out.println(Utils.GREEN_BOLD_BRIGHT + "\nWELCOME TO JAVA-12 PROGRAMMER TEST" + Utils.RESET);
		while(isWhileCheckMainPage()==true) {
			setWhileCheckCandidatePage(true);
			applyMainPageChoice(getMainPageChoice());
		}	
	}

	@Override
	public int getMainPageChoice() {
		System.out.println("1- Candidate Register");
		System.out.println("2- Candidate Login");
		System.out.println("5- Show Exam Questions (Cheat)");
		System.out.println("6- Show Registered Candidates (Cheat)");
		System.out.println("0- Exit Program");
		setKey(Utils.getIntegerValue("Input the number of the operation you want to execute: "));
		return getKey();
	}

	@Override
	public void applyMainPageChoice(int key) {
		switch (key) {
		case 1:
			getManager().register();
			break;
		case 2:
			if(!getManager().login()) {
				break;
			}
			cyclePage(key);
			runCandidatePage();
			break;
		case 5:
			getManager().getExam().forEach(System.out::println);
			break;
		case 6:
			getManager().getCandidates().forEach(System.out::println);
			break;
		case 0:
			System.out.println(Utils.RED + "Program Terminated..." + Utils.RESET);
			System.exit(0);	
		default:
			System.out.println(Utils.RED + "Wrong input!" + Utils.RESET);
			break;
		}
		cyclePage(key); 
		
	}

	@Override
	public void runCandidatePage() {
		while(isWhileCheckCandidatePage()==true) {
			applyCandidatePageChoice(getCandidatePageChoice());
		}	
	}

	@Override
	public int getCandidatePageChoice() {
		System.out.println(Utils.GREEN_BOLD_BRIGHT + "\nCandidate " + getManager().getCandidate().getCandidateName() + " " + getManager().getCandidate().getCandidateSurname() + Utils.RESET);
		System.out.println("1- Show Exam Info");
		System.out.println("2- Start Exam");
		System.out.println("5- Show My Exam Result");
		System.out.println("9- Logout");
		System.out.println("0- Exit Program");
		setKey(Utils.getIntegerValue("Input the number of the operation you want to execute: "));
		return getKey();
	}

	@Override
	public void applyCandidatePageChoice(int key) {
		switch (key) {
		case 1:
			getManager().informCandidateForExam();;
			break;
		case 2:
			runExamPage();
			break;
		case 5:
			getManager().showExamResult();
			break;
		case 9:
			setWhileCheckCandidatePage(false);
			break;	
		case 0:
			System.out.println(Utils.RED + "Program Terminated..." + Utils.RESET);
			System.exit(0);	
		default:
			System.out.println(Utils.RED + "Wrong input!" + Utils.RESET);
			break;
		}
		cyclePage(key); 
		
	}

	@Override
	public void runExamPage() {
		if(getManager().startExam()==false)
			return;
		ListIterator<Question> listIterator= getManager().getExam().listIterator();
		LocalTime finishTime = LocalTime.now().plusMinutes(30);
		Duration remainingTime;
		while(listIterator.hasNext()==true) {
			Question currentQuestion = listIterator.next();
			remainingTime = Duration.between(LocalTime.now(), finishTime).withNanos(0);
			if(remainingTime.getSeconds()<0) {
				System.out.println(Utils.RED_BOLD_BRIGHT + "Exam Time is up!" + Utils.RESET);
				break;
			}
			getManager().showCurrentQuestion(remainingTime,currentQuestion);
			getManager().checkCandidateAnswer(currentQuestion, getExamPageChoice());
		}	
		getManager().finishExam();
	}

	@Override
	public String getExamPageChoice() {
		return Utils.getStringValueForExam("Input your answer: ");
	}
	
	/**
	 * Kullanıcıdan alınan seçime göre geriye bir bilgilendirme mesajı döner. Aynı zamanda kullanıcı yaptığı seçimleri
	 * console üzerinden daha iyi okuyabilmesi için ENTER ile devam et bilgilendirmesi de yapar ve kullanıcıdan bir
	 * console girişi yapmasını bekler.
	 */
	@Override
	public void cyclePage(int key) {
		if (key == 9) {
			System.out.println(Utils.YELLOW + "Returned Previous Page..." + Utils.RESET);
		} else {
			Utils.getStringValue(Utils.GREEN + "Press ENTER to continue..." + Utils.RESET);
		}
		
	}
	// only getters	
	public static ExamManagerImpl getManager() {
		return manager;
	}

	public static DatabaseImpl getDatabase() {
		return database;
	}

	// getters-setters
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isWhileCheckMainPage() {
		return whileCheckMainPage;
	}

	public void setWhileCheckMainPage(boolean whileCheckMainPage) {
		this.whileCheckMainPage = whileCheckMainPage;
	}

	public boolean isWhileCheckCandidatePage() {
		return whileCheckCandidatePage;
	}

	public void setWhileCheckCandidatePage(boolean whileCheckCandidatePage) {
		this.whileCheckCandidatePage = whileCheckCandidatePage;
	}

	public boolean isWhileCheckExamPage() {
		return whileCheckExamPage;
	}

	public void setWhileCheckExamPage(boolean whileCheckExamPage) {
		this.whileCheckExamPage = whileCheckExamPage;
	}	
		
}
