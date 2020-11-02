package com.example.demo.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

public class WordAnalyzer {

	private static Logger LOGGER = LoggerFactory.getLogger(WordAnalyzer.class);

	private String fileName;
	private String input;
	private int cIndex;

	private WordAnalyzer(String fileName, String input){
		this.fileName = fileName;
		this.input = input;
		cIndex = 0;
	}

	public void reset(){
		cIndex = 0;
	}

	public String getFileName(){
		return fileName;
	}

	public int getCIndex(){
		return cIndex;
	}

	public int getTotalSize(){
		return input.length();
	}

	public String getNextWord(){
		StringBuilder word = new StringBuilder();
		boolean doubleQuotFlag = false;
		while(cIndex < input.length()){
			int current = input.charAt(cIndex);
			int next = 0;
			if(cIndex + 1 < input.length()){
				next = input.charAt(cIndex + 1);
			}

			if((!doubleQuotFlag) && (current == '"')){
				doubleQuotFlag = true;
			}
			else if((doubleQuotFlag) && (current == '"')){
				doubleQuotFlag = false;
			}

			if(doubleQuotFlag){
				word.append((char)current);
				cIndex++;
			}
			else{
				if(current != ' '){
					word.append((char)current);
				}
				cIndex++;

				if(AdaAnalyzer.isDelimiter(current)){
					break;
				}
				if((next == ' ') || AdaAnalyzer.isDelimiter(next)){
					break;
				}
				if(AdaAnalyzer.isOperatorStart(next)){
					if(AdaAnalyzer.isOperatorEnd(current, next)){
						word.append((char)next);
						cIndex++;
					}
					break;
				}
				if(AdaAnalyzer.isOperatorStart(current)){
					if(AdaAnalyzer.isOperatorEnd(current, next)){
						word.append((char)next);
						cIndex++;
						break;
					}else{
						break;
					}
				}
//				if((current == '=') && next == '>'){
//					word.append((char)next);
//					cIndex++;
//					break;
//				}
			}
		}
		if(word.length() > 0){
			return word.toString();
		}
		else{
			if(cIndex >= input.length()){
				return null;
			}
			else{
				return getNextWord();
			}
		}
	}

	public String getSubString(int start, int end){
		String body = input.substring(start, end);
		return body;
	}

	public static WordAnalyzer create(String fileName){
		try {
			WordAnalyzer wordAnalyzer = null;
			FileInputStream fis = new FileInputStream(fileName);
			StringBuilder sb = new StringBuilder();
			int current = 0;
			while ((current = fis.read()) != -1) {
				sb.append((char) current);
			}
			fis.close();
			wordAnalyzer = new WordAnalyzer(fileName, sb.toString());
			return wordAnalyzer;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
}