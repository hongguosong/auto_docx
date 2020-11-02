package com.example.demo.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LexicalElement {
	public static final int TYPE_DEFAULT = 0;//默认类型
	public static final int TYPE_PACKAGE = 1;//包
	public static final int TYPE_PROC = 2;//过程
	public static final int TYPE_FUNC = 3;//函数
	public static final int TYPE_TASK = 4;//任务
	public static final int TYPE_PROT = 5;//保护对象

	private String key;
	private String name;
	private String paramStr;
	private int start;
	private int end;
	private int type;
	private String hashValue;
	private String returnType;
	private String comment;
	private int lineCount;
	private List<VarItem> params;
	private List<VarItem> vars;
	private List<LexicalElement> children;
	private LexicalElement parent;
	private List<LexicalElement> calls;
	private List<VarItem> varCalls;

	public LexicalElement(String name, int type){
		this(name, type, null, null);
	}

	public LexicalElement(String name, int type, String paramStr, String returnType){
		this.name = name;
		this.type = type;
		this.paramStr = paramStr;
		this.key = generateKey(name, paramStr);
		this.start = -1;
		this.end = -1;
		this.hashValue = null;
		this.returnType = returnType;
		this.comment = null;
		this.lineCount = 1;
		this.params = new ArrayList<VarItem>();
		this.vars = new ArrayList<VarItem>();
		this.children = new ArrayList<LexicalElement>();
		this.calls = new ArrayList<LexicalElement>();
		this.varCalls = new ArrayList<VarItem>();

		updateProcParam();
	}

	public boolean isProcType() {
		return getType() == LexicalElement.TYPE_PACKAGE ||
				getType() == LexicalElement.TYPE_PROC ||
				getType() == LexicalElement.TYPE_FUNC ||
				getType() == LexicalElement.TYPE_TASK ||
				getType() == LexicalElement.TYPE_PROT;
	}

	public static String generateKey(String pName, String pParam){
		if(pName == null){
			return null;
		}
		else {
			if (pParam == null) {
				return pName.toLowerCase();
			} else {
				List<VarItem> paramList = generateProcParam(pParam);
				StringBuilder paramBuilder = new StringBuilder();
				if(paramList.size() > 0){
					paramBuilder.append(paramList.get(0).getCls());
					for(int i = 1; i < paramList.size(); i++){
						paramBuilder.append(',');
						paramBuilder.append(paramList.get(i).getCls());
					}
				}
				return (pName + "(" + paramBuilder.toString().trim() + ")").toLowerCase();
			}
		}
	}

	public static String generateName(String pKey){
		if(pKey == null){
			return null;
		}
		else {
			if (pKey.contains("(")) {
				int end = pKey.indexOf("(");
				return pKey.substring(0, end).toLowerCase();
			} else {
				return pKey.toLowerCase();
			}
		}
	}

	private static VarItem getVarItem(String varString){
		String[] arr1 = varString.split(":=");
		VarItem item = new VarItem();
		if(arr1.length >= 2){
			varString = arr1[0].trim();
			item.setVal(arr1[1].trim());
		}

		String[] arr2 = varString.split(":");
		if(arr2.length >= 2){
			item.setName(arr2[0].trim());
			varString = arr2[1].trim();
		}

		String[] arr3 = varString.split(" ");
		if(arr3.length >= 2){
			item.setCls(arr3[arr3.length - 1].trim());
			varString = varString.replace(item.getCls(), "");
			if(!varString.trim().isEmpty()){
				item.setType(varString.trim());
			}
		}
		else{
			item.setCls(varString.trim());
		}

		if(item.getName() != null){
			return item;
		}

		return null;
	}

	private static List<VarItem> generateProcParam(String paramString){
		List<VarItem> paramList = new ArrayList<VarItem>();
		if(paramString != null){
			String[] paramArray = paramString.split(";");
			for(String param: paramArray){
				if(!param.isEmpty()){
					VarItem item = getVarItem(param);
					if(item != null){
						if(item.getName().contains(",")){
							String[] nameArray = item.getName().split(",");
							for(String name: nameArray){
								if(!name.trim().isEmpty()){
									VarItem tmp = new VarItem();
									tmp.setName(name.trim());
									tmp.setCls(item.getCls());
									tmp.setType(item.getType());
									tmp.setVal(item.getVal());
									paramList.add(tmp);
								}
							}
						}
						else{
							paramList.add(item);
						}
					}
				}
			}
		}
		return paramList;
	}

	private void updateProcParam(){
		List<VarItem> paramList = generateProcParam(paramStr);
		for(VarItem param:paramList){
			this.addParam(param);
		}
	}

	public void updateProcVar(String varStr){
		if(varStr != null){
			String[] paramArray = varStr.split(";");
			for(String param: paramArray){
				if(!param.isEmpty()){
					VarItem item = getVarItem(param);
					if(item != null){
						if(item.getName().contains(",")){
							String[] nameArray = item.getName().split(",");
							for(String name: nameArray){
								if(!name.trim().isEmpty()){
									VarItem tmp = new VarItem();
									tmp.setName(name.trim());
									tmp.setCls(item.getCls());
									tmp.setType(item.getType());
									tmp.setVal(item.getVal());
									if(isValidName(tmp.getName())) {
										this.addVar(tmp);
									}
								}
							}
						}
						else{
							if(isValidName(item.getName())) {
								this.addVar(item);
							}
						}
					}
				}
			}
		}
	}

	private String[] delimiterOrBlank = {" ", ";", "(", ")", ",", "[", "]", "{", "}"};
	private boolean isValidName(String name) {
		if(name == null){
			return false;
		}
		else {
			for (String ch : delimiterOrBlank) {
				if (name.contains(ch)) {
					return false;
				}
			}
		}
		return true;
	}

	public String getKey() {
		return key;
	}

	public String getKeyName(){
		return generateName(key);
	}

	public String getName() {
		return name;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getType() {
		return type;
	}

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public List<VarItem> getParams() {
		return params;
	}

	public void addParam(VarItem item){
		boolean hasKey = false;
		for(VarItem param : params){
			if(param.getName().toLowerCase().equals(item.getName().toLowerCase())){
				hasKey = true;
				break;
			}
		}
		if(!hasKey){
			params.add(item);
			item.setParent(this);
		}
	}

	public List<VarItem> getVars() {
		return vars;
	}

	public void addVar(VarItem item){
		boolean hasKey = false;
		for(VarItem param : params){
			if(param.getName().toLowerCase().equals(item.getName().toLowerCase())){
				hasKey = true;
				break;
			}
		}
		if(!hasKey){
			vars.add(item);
			item.setParent(this);
		}
	}

	public List<LexicalElement> getChildren() {
		return children;
	}

	public List<LexicalElement> findChildListByName(String name) {
		List<LexicalElement> childList = new ArrayList<LexicalElement>();
		LexicalElement child;
		for(int i=0;i<children.size();i++){
			child = children.get(i);
			if(child.getKeyName().equals(name)){
				childList.add(child);
			}
			else{
				List<LexicalElement> itemList = child.findChildListByName(name);
				if(itemList.size() > 0){
					childList.addAll(itemList);
				}
			}
		}
		return childList;
	}

	public List<LexicalElement> findProtectedByName(String name) {
		List<LexicalElement> childList = new ArrayList<LexicalElement>();
		LexicalElement child;
		for(int i=0;i<children.size();i++){
			child = children.get(i);
			if(child.getParent().getType() == TYPE_PROT){
				String temp = child.getParent().getName() + "." + child.getName();
				if(temp.toLowerCase().equals(name.toLowerCase())){
					childList.add(child);
				}
			}
			else {
				List<LexicalElement> itemList = child.findProtectedByName(name);
				if(itemList.size() > 0){
					childList.addAll(itemList);
				}
			}
		}
		return childList;
	}

	public LexicalElement findChild(String key) {
		LexicalElement child;
		for(int i=0;i<children.size();i++){
			child = children.get(i);
			if(child.getFullName().equals(key)){
				return child;
			}
			else{
				LexicalElement item = child.findChild(key);
				if(item != null){
					return item;
				}
			}
		}
		return null;
	}

	public LexicalElement findChild(String key, int startPos, int type) {
		LexicalElement child;
		for(int i=0;i<children.size();i++){
			child = children.get(i);
			if(child.getStart() >= startPos){
				if(child.getKey().equals(key) && child.getType() == type && child.getEnd() < 0){
					return child;
				}
				else{
					LexicalElement item = child.findChild(key, startPos, type);
					if(item != null){
						return item;
					}
				}
			}
		}
		return null;
	}

	public LexicalElement findChild(int pos) {
		LexicalElement child;
		for(int i=0;i<children.size();i++){
			child = children.get(i);
			if(child.getStart() <= pos && child.getEnd() >= pos){
				if(child.hasChildren()){
					LexicalElement item = child.findChild(pos);
					if(item != null){
						return item;
					}
					else{
						return child;
					}
				}
				else{
					return child;
				}
			}
		}
		return null;
	}

	public LexicalElement findNotEndChild(String key, int startPos, int type) {
		LexicalElement item = null;
		int startPosition = startPos;
		do{
			item = findChild(key, startPosition, type);
			if(item != null) {
				startPosition = item.getEnd();
			}
			else {
				startPosition = -1;
			}
		} while (startPosition >= 0);
		return item;
	}

	public boolean hasChildren(){
		return children.size() > 0;
	}

	public void addChildren(LexicalElement item){
		LexicalElement child;
		boolean isAdded = false;
		for(int i=0;i<children.size();i++){
			child = children.get(i);
			int childStart = child.getStart() < 0 ? Integer.MIN_VALUE: child.getStart();
			int childEnd = child.getEnd() < 0 ? Integer.MAX_VALUE: child.getEnd();
			if(item.getStart() > childStart && item.getStart() <= childEnd){
				child.addChildren(item);
				isAdded = true;
				break;
			}
		}
		if(!isAdded){
			children.add(item);
			item.setParent(this);
		}
	}

	public void addCall(LexicalElement item){
		boolean hasKey = false;
		for(LexicalElement call : calls){
			if(call.getFullName().equals(item.getFullName())){
				hasKey = true;
				break;
			}
		}
		if(!hasKey){
			calls.add(item);
		}
	}

	public LexicalElement getParent() {
		return parent;
	}

	public void setParent(LexicalElement parent) {
		this.parent = parent;
	}

	public String getFullName(){
		String prefix = "";
		if(parent != null){
			prefix = parent.getFullName();
			if(prefix != null && !prefix.trim().isEmpty()){
				prefix += ".";
			}
		}
		return prefix + key;
	}

	public String getLongName(){
		String prefix = "";
		if(parent != null){
			prefix = parent.getLongName();
			if(prefix != null && !prefix.trim().isEmpty()){
				prefix += ".";
			}
		}
		return prefix + name.toLowerCase();
	}

	public List<LexicalElement> getCalls() {
		return calls;
	}

	public void addVarCall(VarItem item){
		boolean hasKey = false;
		for(VarItem call : varCalls){
			if(call.getName().toLowerCase().equals(item.getName().toLowerCase())){
				hasKey = true;
				break;
			}
		}
		if(!hasKey){
			varCalls.add(item);
		}
	}

	public List<VarItem> getVarCalls() {
		return varCalls;
	}

	private String getTypeName(){
		String typeName = null;
		switch(type){
			case TYPE_PACKAGE:
				typeName = "package";
				break;
			case TYPE_PROC:
				typeName = "procedure";
				break;
			case TYPE_FUNC:
				typeName = "function";
				break;
			case TYPE_TASK:
				typeName = "task";
				break;
			case TYPE_PROT:
				typeName = "protected";
				break;
			default:
				typeName = "default";
				break;
		}

		return typeName;
	}

	public String toResultString(){
		StringBuilder sb = new StringBuilder();
		if(comment != null){
			sb.append(comment);
		}
		sb.append(getTypeName());
		sb.append(":");
		sb.append(name);
		sb.append("[start:");
		sb.append(start);
		sb.append(",end:");
		sb.append(end);
		sb.append(",hash:");
		sb.append(hashValue);
		sb.append(",lines:");
		sb.append(lineCount);
		sb.append("]");
		if(params.size() > 0){
			sb.append("(");
			for(int i=0;i<params.size();i++){
				sb.append(params.get(i).getName());
				sb.append(":");
				if(params.get(i).getType() != null){
					sb.append(params.get(i).getType());
					sb.append(" ");
				}
				sb.append(params.get(i).getCls());
				if(params.get(i).getVal() != null){
					sb.append(":=");
					sb.append(params.get(i).getVal());
				}
				sb.append(";");
			}
			sb.append(")");
		}
		if(returnType != null){
			sb.append(" return ");
			sb.append(returnType);
		}
		if(vars.size() > 0){
			sb.append(" vars:");
			for(int i=0;i<vars.size();i++){
				sb.append(vars.get(i).getName());
				sb.append(":");
				if(vars.get(i).getType() != null){
					sb.append(vars.get(i).getType());
					sb.append(" ");
				}
				sb.append(vars.get(i).getCls());
				if(vars.get(i).getVal() != null){
					sb.append(":=");
					sb.append(vars.get(i).getVal());
				}
				sb.append(";");
			}
		}
		if(calls.size() > 0){
			sb.append(" calls:");
			for(int i=0;i<calls.size();i++){
				sb.append(" ");
				sb.append(calls.get(i).getName());
			}
		}
		if(varCalls.size() > 0){
			sb.append(" var_calls:");
			for(int i=0;i<varCalls.size();i++){
				sb.append(" ");
				sb.append(varCalls.get(i).getName());
			}
		}
		sb.append("\n");

		for(int i=0;i<children.size();i++){
			sb.append(children.get(i).toResultString());
		}

		return sb.toString();
	}
}