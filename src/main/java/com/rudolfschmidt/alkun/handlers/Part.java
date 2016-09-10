package com.rudolfschmidt.alkun.handlers;

import java.util.ArrayList;
import java.util.List;

public class Part {

    public static Part newInstance() {
        return new Part(new ArrayList<>());
    }

    private String type;
    private String formName;
    private String fileName;
    private List<String> body;

    private Part(List<String> body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getBody() {
        return body;
    }


}
