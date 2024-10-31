package com.haith.cookingrecipeapp.models;

public class InstructionStepModel {
    private int stepNumber;
    private String stepContent;

    public InstructionStepModel(int stepNumber, String description) {
        this.stepNumber = stepNumber;
        this.stepContent = "Step " + stepNumber + ": " + description;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public String getStepContent() {
        return stepContent;
    }
}
