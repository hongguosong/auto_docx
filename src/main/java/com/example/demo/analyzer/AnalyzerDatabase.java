package com.example.demo.analyzer;

import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerDatabase {
	private int startPos;
	private LexicalElement root;
	private List<LexicalElement> withPackage;
	private List<LexicalElement> usePackage;

	public AnalyzerDatabase(){
		startPos = 0;
		root = new LexicalElement("", LexicalElement.TYPE_DEFAULT);
		withPackage = new ArrayList<LexicalElement>();
		usePackage = new ArrayList<LexicalElement>();
	}

	public void finishWordAnalyzer(WordAnalyzer wordAnalyzer){
		startPos += wordAnalyzer.getTotalSize();
	}

	public LexicalElement getRoot(){
		return root;
	}

	public void addWith(String name){
		LexicalElement with = root.findChild(name.toLowerCase());
		if(with != null && with.getType() == LexicalElement.TYPE_PACKAGE){
			int k=0;
			for(;k<withPackage.size();k++){
				if(withPackage.get(k).getKey().equals(with.getKey())){
					break;
				}
			}
			if(k==withPackage.size()){
				withPackage.add(with);
			}
		}
	}

	public void addUse(String name){
		LexicalElement use = root.findChild(name.toLowerCase());
		if(use != null && use.getType() == LexicalElement.TYPE_PACKAGE){
			int k=0;
			for(;k<usePackage.size();k++){
				if(usePackage.get(k).getKey().equals(use.getKey())){
					break;
				}
			}
			if(k==usePackage.size()){
				usePackage.add(use);
			}
		}
	}

	public List<LexicalElement> findProc(String name){
		List<LexicalElement> itemList = new ArrayList<LexicalElement>();
		if(name.contains(".")){
			for(LexicalElement pkg:withPackage){
				if(name.toLowerCase().contains(pkg.getKey())){
					String procName = name.toLowerCase().replace(pkg.getKey()+".", "");
					if(procName.contains(".")){
						itemList.addAll(pkg.findProtectedByName(procName));
					}else{
						itemList.addAll(pkg.findChildListByName(procName));
					}
				}else{
					itemList.addAll(pkg.findProtectedByName(name.toLowerCase()));
				}
			}
			return itemList;
		}
		else{
			for(LexicalElement pkg:withPackage){
				itemList.addAll(pkg.findChildListByName(name.toLowerCase()));
			}
			for(LexicalElement ele:root.getChildren()){
				if(ele.getType() != LexicalElement.TYPE_PACKAGE){
					if(ele.getKeyName().equals(name.toLowerCase())){
						itemList.add(ele);
					}
				}
			}
			return itemList;
		}
	}

	public VarItem findVar(String name){
		if(name.contains(".")){
			for(LexicalElement pkg:withPackage){
				if(name.toLowerCase().contains(pkg.getKey())){
					String varName = name.toLowerCase().replace(pkg.getKey()+".", "");
					for(VarItem var:pkg.getVars()){
						if(var.getName().toLowerCase().equals(varName.toLowerCase())){
							return var;
						}
					}
				}
			}
			return null;
		}
		else{
			for(LexicalElement pkg:withPackage){
				for(VarItem var:pkg.getVars()){
					if(var.getName().toLowerCase().equals(name.toLowerCase())){
						return var;
					}
				}
			}
			for(VarItem var:root.getVars()){
				if(var.getName().toLowerCase().equals(name.toLowerCase())){
					return var;
				}
			}
			return null;
		}
	}

	public void updateCalls(String procKey, LexicalElement calledItem){
		LexicalElement updateProc = root.findChild(procKey);
		if(updateProc != null){
			updateProc.addCall(calledItem);
		}
	}

	public void updateVarCalls(String procKey, VarItem calledItem){
		LexicalElement updateProc = root.findChild(procKey);
		if(updateProc != null){
			updateProc.addVarCall(calledItem);
		}
	}

	public void addPackage(String name, int start){
		LexicalElement item = new LexicalElement(name, LexicalElement.TYPE_PACKAGE);
		item.setStart(startPos + start);
		root.addChildren(item);
	}

	public void addFunction(String name, int start, String paramStr, String returnType){
		LexicalElement item = new LexicalElement(name, LexicalElement.TYPE_FUNC, paramStr, returnType);
		item.setStart(startPos + start);
		root.addChildren(item);
	}

	public void addProcedure(String name, int start, String paramStr){
		LexicalElement item = new LexicalElement(name, LexicalElement.TYPE_PROC, paramStr, null);
		item.setStart(startPos + start);
		root.addChildren(item);
	}

	public void addTask(String name, int start){
		LexicalElement item = new LexicalElement(name, LexicalElement.TYPE_TASK, null, null);
		item.setStart(startPos + start);
		root.addChildren(item);
	}

	public void addProtected(String name, int start){
		LexicalElement item = new LexicalElement(name, LexicalElement.TYPE_PROT, null, null);
		item.setStart(startPos + start);
		root.addChildren(item);
	}

	public void addComment(String comment, int start){
		LexicalElement item = root.findChild(start);
		if(item != null){
			item.setComment(comment);
		}
	}

	public void addProcLines(int lines, int start){
		LexicalElement item = root.findChild(start);
		if(item != null && item.getStart() < start && item.getEnd() > start){
			int count = item.getLineCount() + lines;
			item.setLineCount(count);
		}
	}

	public void updateEnd(String procKey, int end, WordAnalyzer wordAnalyzer, int type){
		LexicalElement item = root.findNotEndChild(procKey.toLowerCase(), startPos, type);
		if((item != null) && (item.getEnd() == -1)){
			item.setEnd(startPos + end);
			String procContent  = wordAnalyzer.getSubString(item.getStart() - startPos, (item.getEnd() + 1) - startPos);
			String procCheckCode = DigestUtils.md5DigestAsHex(procContent.getBytes());
			item.setHashValue(procCheckCode);
		}
	}

	public void updateProcVar(String procKey, String paramStr){
		if(procKey == null){
			root.updateProcVar(paramStr);
		}
		else{
			LexicalElement updateProc = root.findChild(procKey.toLowerCase());
			if(updateProc != null && (updateProc.getType() == LexicalElement.TYPE_PACKAGE)){
				updateProc.updateProcVar(paramStr);
			}
		}
	}

	public String toResultString(){
		return root.toResultString();
	}

}
