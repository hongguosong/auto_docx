package com.example.demo.word;

public enum CoverageType {
    TESTCASE("测试路径概要", 1),
    STATEMENT("语句覆盖率", 2),
    BRANCH("分支覆盖率", 3);

    private String name;
    private int type;

    private CoverageType(String name, int type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
