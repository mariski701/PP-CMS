package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertCodeService {
    @Autowired
    private AlertCodeRepository alertCodeRepository;
    @Autowired
    private AlertTranslationRepository alertTranslationRepository;

    public List<AlertCode> getAlertCodes() {
        return alertCodeRepository.findAll();
    }

    public Object removeCode(int id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        AlertCode alertCode = alertCodeRepository.findById(id).orElse(null);
        if (alertCode == null)
        {
            errorProvidedDataHandler.setError("3041");
            return errorProvidedDataHandler;
        }
        List<AlertTranslation> alertTranslationList = alertTranslationRepository.findByAlertCode(alertCode);
        if (!alertTranslationList.isEmpty()) {
            alertTranslationRepository.deleteAll(alertTranslationList);
        }

        alertCodeRepository.delete(alertCode);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;
    }

    public Object addCode(String alertCode, String alertName) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        AlertCode alertCodeTemp = alertCodeRepository.findByAlertCode(alertCode);
        if (alertCodeTemp!=null) {
            errorProvidedDataHandler.setError("3044");
            return errorProvidedDataHandler;
        }
        if (alertCode.equals("")) {
            errorProvidedDataHandler.setError("3042");
            return errorProvidedDataHandler;
        }
        if (alertName.equals("")) {
            errorProvidedDataHandler.setError("3043");
            return errorProvidedDataHandler;
        }
        AlertCode newAlertCode = new AlertCode();
        newAlertCode.setAlertCode(alertCode);
        newAlertCode.setAlertName(alertName);
        errorProvidedDataHandler.setError("2001");
        alertCodeRepository.save(newAlertCode);
        return errorProvidedDataHandler;
    }

    public Object editCode(int id, String alertCode, String alertName) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        AlertCode alertCodeTemp = alertCodeRepository.findByAlertCode(alertCode);
        AlertCode editedAlertCode = alertCodeRepository.findById(id).orElse(null);

        if(alertCodeTemp!= null) {
            if (!editedAlertCode.getAlertCode().equals(alertCode))
            {
                errorProvidedDataHandler.setError("3044");
                return errorProvidedDataHandler;
            }

        }

        if (editedAlertCode == null) {
            errorProvidedDataHandler.setError("3041"); //error code not found
            return errorProvidedDataHandler;
        }
        if (alertCode.equals("")) {
            errorProvidedDataHandler.setError("3042");
        }
        if (alertName.equals("")) {
            errorProvidedDataHandler.setError("3043");
        }
        editedAlertCode.setAlertCode(alertCode);
        editedAlertCode.setAlertName(alertName);
        errorProvidedDataHandler.setError("2001");
        alertCodeRepository.save(editedAlertCode);
        return errorProvidedDataHandler;
    }

}
