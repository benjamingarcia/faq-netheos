package org.bga.netheos.faqnetheos.controller;

import org.bga.netheos.faqnetheos.dto.Faq;
import org.bga.netheos.faqnetheos.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FaqController {

    private final FaqRepository faqRepository;

    @Autowired
    public FaqController(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @PostMapping(value = "/faq")
    public ResponseEntity<Object> addFaq(@RequestBody Faq faq){
        if(faqRepository.add(faq)){
            return new ResponseEntity<>(faq, HttpStatus.OK);
        }
        return new ResponseEntity<>("unable do save movie", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/faq")
    public ResponseEntity<List<Faq>> getFaq(){
        return new ResponseEntity<>(faqRepository.getFaqs(), HttpStatus.OK);
    }


    @GetMapping(value = "/faq/search")
    public ResponseEntity<List<Faq>> searchFaq(@RequestParam("search") String search){
        List<Faq> faqs = faqRepository.getFaqs()
                .stream().filter(faq -> faq.getAnswer().contains(search) || faq.getQuestion().contains(search))
                .collect(Collectors.toList());
        return new ResponseEntity<>(faqs, HttpStatus.OK);
    }

}
