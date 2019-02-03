package org.bga.netheos.faqnetheos.dto;

import lombok.Data;

import java.util.List;

@Data
public class Faq {

    private String answer;
    private String question;
    private List<String> tags;

    public Faq() {
    }

    public Faq(String question, String answer, List<String> tags) {
        this.answer = answer;
        this.question = question;
        this.tags = tags;
    }
}

