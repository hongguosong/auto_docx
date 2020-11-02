package com.example.demo.analyzer;

public class VarItem {
	private String name;
	private String cls;
	private String type;
	private String val;
	private LexicalElement parent;
	
	public VarItem(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public LexicalElement getParent() {
		return parent;
	}
	public void setParent(LexicalElement parent) {
		this.parent = parent;
	}
}
