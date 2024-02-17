package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CustomCorsConfigAnnotation
public class LanguageController {
    @Autowired
    LanguageService languageService;

    @PostMapping("/api/language/add")
    public Object addLanguage(@RequestBody Language language) {
        return languageService.addLanguage(language);
    }

    @DeleteMapping("/api/language/remove/{id}")
    public Object removeLanguage(@PathVariable int id) {
        return languageService.removeLanguage(id);
    }

    @GetMapping("/api/languages")
    public List<Language> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/api/language/{name}")
    public Language getLanguage(@PathVariable String name) {
        return languageService.getLanguage(name);
    }

    @PutMapping("/api/language/edit")
    public Object editLanguage(@RequestBody Language language) {
        return languageService.editLanguage(language);
    }

    @GetMapping("/api/language/id/{id}")
    public Language getLanguageById(@PathVariable int id) {
        return languageService.getLanguageById(id);
    }

}
