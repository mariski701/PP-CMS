package com.cms.pp.cms.pp.Alerts;


import com.cms.pp.cms.pp.Article.Language;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Alert_translation")
public class AlertTranslation {
    @Id
    @GeneratedValue
    @Column(unique = true, length = 128)
    private int id;

    @Column(name = "error_translation")
    private String errorTranslation;

    @ManyToOne
    @JoinColumn(name ="language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name ="alertCode_id")
    private AlertCode alertCode;
}
