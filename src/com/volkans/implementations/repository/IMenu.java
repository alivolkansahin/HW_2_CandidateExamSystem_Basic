package com.volkans.implementations.repository;

public interface IMenu {
	void runMainPage();
	int getMainPageChoice();
	void applyMainPageChoice(int key);
	void runCandidatePage();
	int getCandidatePageChoice();
	void applyCandidatePageChoice(int key);	
	void runExamPage();
	String getExamPageChoice();
	void cyclePage(int key);	
}
