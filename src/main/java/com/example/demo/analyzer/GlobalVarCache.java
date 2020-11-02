package com.example.demo.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalVarCache {
	private Map<String, List<String>> varMap;

	public GlobalVarCache(){
		varMap = new HashMap<String, List<String>>();
	}

	public void reset(){
		varMap.clear();
	}
	public void addGlobalVar(String pkgName, String varString){
		if(!varMap.containsKey(pkgName.toLowerCase())){
			varMap.put(pkgName.toLowerCase(), new ArrayList<String>());
		}
		varMap.get(pkgName.toLowerCase()).add(varString);
	}

	public Map<String, List<String>> getVarMap(){
		return varMap;
	}
}
