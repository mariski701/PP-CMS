package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LanguageController {
    @Autowired
    LanguageService languageService;

    @PostMapping("/api/language/add")
    public Language addLanguage(@RequestBody Language language) {
        return languageService.addLanguage(language);
    }

    @DeleteMapping("/api/language/remove/{id}")
    public String removeLanguage(@PathVariable int id) {
        return languageService.removeLanguage(id);
    }

    @GetMapping("/api/languages")
    public List<Language> getAllLanguages() {
        return languageService.getAllLanguages();
    }

}
