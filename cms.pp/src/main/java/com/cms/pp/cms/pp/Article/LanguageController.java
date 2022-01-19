package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
        origins = {"http://localhost:4200"},
        allowCredentials = "true",
        maxAge = 3600,
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,RequestMethod.POST,
                RequestMethod.DELETE, RequestMethod.PUT,
                RequestMethod.PATCH, RequestMethod.OPTIONS,
                RequestMethod.HEAD, RequestMethod.TRACE
        }
)
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
