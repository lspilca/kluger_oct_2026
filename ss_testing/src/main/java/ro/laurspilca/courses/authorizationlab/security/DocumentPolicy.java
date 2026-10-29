package ro.laurspilca.courses.authorizationlab.security;

import ro.laurspilca.courses.authorizationlab.model.Document;
import ro.laurspilca.courses.authorizationlab.repositories.DocumentRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component("documentPolicy")
public class DocumentPolicy {

    private static final String TENANT_CLAIM = "tenant";

    private final DocumentRepository documentRepository;

    public DocumentPolicy(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public boolean isSubject(String userId, Authentication authentication) {
        String subject = subject(authentication);
        return subject != null && subject.equals(userId);
    }

    public boolean isOwner(String documentId, Authentication authentication) {
        String subject = subject(authentication);
        if (subject == null || documentId == null) {
            return false;
        }
        return documentRepository.findById(documentId)
                .map(Document::owner)
                .filter(subject::equals)
                .isPresent();
    }

    public boolean hasTenant(String tenantId, Authentication authentication) {
        if (tenantId == null || !(principal(authentication) instanceof Jwt jwt)) {
            return false;
        }
        return jwt.getClaims().get(TENANT_CLAIM) instanceof String tenant
                && !tenant.isBlank()
                && tenant.equals(tenantId);
    }

    private String subject(Authentication authentication) {
        if (principal(authentication) instanceof Jwt jwt) {
            String subject = jwt.getSubject();
            return subject == null || subject.isBlank() ? null : subject;
        }
        return null;
    }

    private Object principal(Authentication authentication) {
        return authentication == null ? null : authentication.getPrincipal();
    }
}
