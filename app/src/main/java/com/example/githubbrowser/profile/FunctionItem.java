package com.example.githubbrowser.profile;

public class FunctionItem {
    private int funcImg;
    private String leadingText;
    private int trailingNumber;

    public FunctionItem(int funcImg, String leadingText, int trailingNumber) {
        this.funcImg = funcImg;
        this.leadingText = leadingText;
        this.trailingNumber = trailingNumber;
    }

    public int getFuncImg() {
        return funcImg;
    }

    public void setFuncImg(int funcImg) {
        this.funcImg = funcImg;
    }

    public String getLeadingText() {
        return leadingText;
    }

    public void setLeadingText(String leadingText) {
        this.leadingText = leadingText;
    }

    public int getTrailingNumber() {
        return trailingNumber;
    }

    public void setTrailingNumber(int trailingNumber) {
        this.trailingNumber = trailingNumber;
    }
}
