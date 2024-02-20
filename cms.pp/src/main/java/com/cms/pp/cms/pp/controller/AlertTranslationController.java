package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.dto.AlertTranslationDTO;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import com.cms.pp.cms.pp.service.IAlertTranslationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/alerts/")
public class AlertTranslationController {

	private final IAlertTranslationService alertTranslationService;

	@GetMapping("{language}")
	public List<AlertTranslationDTO> findByLanguage(@PathVariable String language) {
		return alertTranslationService.findByLanguage(language);
	}

	@PostMapping("add")
	public Object addAlertTranslation(@RequestBody AlertTranslationDTO alertTranslationDTO) {
		return alertTranslationService.addAlertTranslation(alertTranslationDTO);
	}

	@PutMapping("edit")
	public Object editAlertTranslation(@RequestBody Map<String, String> body) {
		return alertTranslationService.editAlertTranslation(Integer.parseInt(body.get("id")), body.get("alertName"));
	}

	@GetMapping("find/{id}")
	public AlertTranslation findById(@PathVariable int id) {
		return alertTranslationService.findById(id);
	}

}
