package ro.laurspilca.courses.authorizationlab.services;

import ro.laurspilca.courses.authorizationlab.repositories.DocumentRepository;

import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final DocumentRepository documentRepository;

    public ReportService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public String exportId() {
        return "exp-001";
    }

    public long documentCount() {
        return documentRepository.count();
    }
}
