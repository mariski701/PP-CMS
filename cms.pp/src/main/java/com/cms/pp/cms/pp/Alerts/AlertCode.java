package com.cms.pp.cms.pp.Alerts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "alert_code")
public class AlertCode {
    @Id
    @GeneratedValue
    @Column(unique = true, length = 128)
    private int id;

    @Column(name = "alert_name")
    private String alertName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "alertCode")
    private Collection<AlertTranslation> alertTranslation;

    @Version
    private Long version;
}
