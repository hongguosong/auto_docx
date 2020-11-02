package com.example.demo.analyzer;

import java.util.HashMap;
import java.util.Map;

public class LineFeedCache {
	private int startPos;
	private Map<Integer, Integer> lineFeedMap;

	public LineFeedCache(){
		startPos = 0;
		lineFeedMap = new HashMap<Integer, Integer>();
	}

	public void reset(){
		startPos = 0;
		lineFeedMap.clear();
	}

	public void addFileLength(int length){
		startPos += length;
	}

	public void addLineFeed(int pos){
		Integer linePos = startPos + pos;
		if(!lineFeedMap.containsKey(linePos)){
			lineFeedMap.put(linePos, 0);
		}
		lineFeedMap.put(linePos, lineFeedMap.get(linePos) + 1);
	}

	public Map<Integer, Integer> getLineFeedMap(){
		return lineFeedMap;
	}
}
