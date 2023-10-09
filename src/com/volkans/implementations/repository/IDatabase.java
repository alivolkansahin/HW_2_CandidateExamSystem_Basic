package com.volkans.implementations.repository;

import java.util.List;
import java.util.Optional;

import com.volkans.implementations.repository.entities.Candidate;
import com.volkans.implementations.repository.entities.Question;

public interface IDatabase {
	// normal senaryo: elimizde txt dosyası var ve içinde sorular hazır. Okunması ve nesne oluşturulması lazım.
	Optional<List<Question>> readQuestionFromTextFile();
	Optional<List<Question>> prepareQuestions(List<String> lines);	
	// txt dosyası yoksa, kod içinde sorular oluşturulup serialization ile kaydedilip bundan sonra deserialization olarak devam ediyor program...
	void prepareQuestionsFromCodingPage();
	void addQuestionsToList(Question... question);
	void writeQuestionsSerialization(List<Question> questionList);
	Optional<List<Question>> readQuestionsDeSerialization();	
	void writeCandidatesSerialization(List<Candidate> candidates);
	Optional<List<Candidate>> readCandidatesDeSerialization();
}
