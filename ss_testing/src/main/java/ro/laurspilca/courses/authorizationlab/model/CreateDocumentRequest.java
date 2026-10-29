package ro.laurspilca.courses.authorizationlab.model;

import jakarta.validation.constraints.NotBlank;

public record CreateDocumentRequest(@NotBlank String title, @NotBlank String tenant) {
}
