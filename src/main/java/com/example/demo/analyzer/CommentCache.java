package com.example.demo.analyzer;

import java.util.HashMap;
import java.util.Map;

public class CommentCache {
	private int startPos;
	private Map<Integer, StringBuilder> commentMap;

	public CommentCache(){
		startPos = 0;
		commentMap = new HashMap<Integer, StringBuilder>();
	}

	public void reset(){
		startPos = 0;
		commentMap.clear();
	}

	public void addFileLength(int length){
		startPos += length;
	}

	public void addComment(int pos, String comment){
		Integer commentPos = startPos + pos;
		if(!commentMap.containsKey(commentPos)){
			commentMap.put(commentPos, new StringBuilder());
		}
		commentMap.get(commentPos).append(comment.toString());
	}

	public Map<Integer, StringBuilder> getCommentMap(){
		return commentMap;
	}
}
