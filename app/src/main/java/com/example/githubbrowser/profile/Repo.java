package com.example.githubbrowser.profile;

import java.io.Serializable;

public class Repo implements Serializable {
    private String username;
    private String repoName;
    private String ownImgUrl;
    private String repoDesc;
    private String repoLang;
    private int repoStar;
    private int repoFork;

    public Repo(){}

    public Repo(String repoName, String username, String ownImgUrl) {
        this.repoName = repoName;
        this.username = username;
        this.ownImgUrl = ownImgUrl;
    }

    public Repo(String repoName, String repoDesc, int repoStar,
                String repoLang, String username, String ownImgUrl, int repoFork){
        this.repoName = repoName;
        this.repoDesc = repoDesc;
        this.repoStar = repoStar;
        this.repoLang = repoLang;
        this.username = username;
        this.ownImgUrl = ownImgUrl;
        this.repoFork = repoFork;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getOwnImgUrl() {
        return ownImgUrl;
    }

    public void setOwnImgUrl(String ownImgUrl) {
        this.ownImgUrl = ownImgUrl;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }

    public String getRepoLang() {
        return repoLang;
    }

    public void setRepoLang(String repoLang) {
        this.repoLang = repoLang;
    }

    public int getRepoStar() {
        return repoStar;
    }

    public void setRepoStar(int repoStar) {
        this.repoStar = repoStar;
    }

    public int getRepoFork() {
        return repoFork;
    }

    public void setRepoFork(int repoFork) {
        this.repoFork = repoFork;
    }
}
