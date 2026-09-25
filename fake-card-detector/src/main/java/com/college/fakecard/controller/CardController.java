package com.college.fakecard.controller;

import com.college.fakecard.model.DetectionResult;
import com.college.fakecard.service.CardAnalysisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CardController {
    private final CardAnalysisService service;
    public CardController(CardAnalysisService service) { this.service = service; }
    @GetMapping("/") public String home() { return "index"; }
    @PostMapping("/analyse")
    public String analyse(@RequestParam("cardImage") MultipartFile image, @RequestParam(required = false) String cardNumber, Model model) {
        try {
            if (image.isEmpty()) throw new IllegalArgumentException("Choose an image before running the analysis.");
            DetectionResult result = service.analyse(image, cardNumber);
            model.addAttribute("result", result); model.addAttribute("filename", image.getOriginalFilename());
            return "result";
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); return "index"; }
    }
}
