package ro.laurspilca.courses.authorizationlab;

import ro.laurspilca.courses.authorizationlab.repositories.DocumentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public abstract class AbstractAuthorizationTest {

    protected @Autowired MockMvc mockMvc;

    protected @Autowired DocumentRepository documentRepository;

    protected @MockitoBean(enforceOverride = true) JwtDecoder jwtDecoder;

    @BeforeEach
    void resetDocuments() {
        documentRepository.reset();
    }
}
