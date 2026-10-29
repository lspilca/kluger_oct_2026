package ro.laurspilca.courses.authorizationlab.services;

import org.springframework.stereotype.Service;

@Service
public class InfoService {

    public String applicationName() {
        return "document-vault";
    }
}
