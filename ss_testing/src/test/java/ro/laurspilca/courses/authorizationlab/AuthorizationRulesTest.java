package ro.laurspilca.courses.authorizationlab;

import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import ro.laurspilca.courses.authorizationlab.model.Document;

import java.util.Optional;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorizationRulesTest extends AbstractAuthorizationTest {


    @Test
    @DisplayName("""
            GIVEN GET /api/public/info is public.
            WHEN Sending GET /api/public/info without a JWT or an Authorization header.
            THEN we get HTTP 200 and the JSON field name equal to document-vault.
            """)
    public void test1() throws Exception {
        mockMvc.perform(get("/api/public/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("document-vault"));
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/me requires an authenticated user
            WHEN Sending GET /api/me without a JWT.
            THEN results HTTP 401 and a WWW-Authenticate header whose value starts with Bearer.
            """)
    public void test2_1() throws Exception {
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", startsWith("Bearer")));
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/me requires an authenticated user
            WHEN Sending GET /api/me with authentication present
            THEN results HTTP 200 OK.
            """)
    public void test2_2() throws Exception {
        mockMvc.perform(get("/api/me").with(jwt()))
                .andExpect(status().isOk());
    }

//    @Test
//    @DisplayName("""
//            GIVEN GET /api/me requires an authenticated user
//            WHEN Sending GET /api/me with authentication present
//            THEN results HTTP 200 OK.
//            """)
//    @WithMockUser
//    public void test2_3() throws Exception {
//        mockMvc.perform(get("/api/me"))
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("""
            GIVEN DELETE /api/documents/{id} requires SCOPE_documents.delete.
            WHEN Sending DELETE /api/documents/d-101 with sub=ana and only the authority
                 SCOPE_documents.read.
            THEN response is HTTP 403.
            """)
    public void test3_1() throws Exception {
        mockMvc.perform(delete("/api/documents/d-101")
                        .with(jwt()
                                .jwt(j -> j.subject("ana"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_documents.read"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            GIVEN DELETE /api/documents/{id} requires SCOPE_documents.delete.
            WHEN Sending DELETE /api/documents/d-101 with sub=ana and the authority
                 SCOPE_documents.delete.
            THEN response is HTTP 200.
            """)
    public void test3_2() throws Exception {
        mockMvc.perform(delete("/api/documents/d-101")
                        .with(jwt()
                                .jwt(j -> j.subject("ana"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_documents.delete"))))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            GIVEN DELETE /api/documents/{id} requires SCOPE_documents.delete.
            WHEN Sending DELETE /api/documents/d-101 with sub=ana and the authority
                 SCOPE_documents.delete.
            THEN response is HTTP 200.
            """)
    @WithMockUser(username = "ana", authorities = "SCOPE_documents.delete")
    public void test3_3() throws Exception {
        mockMvc.perform(delete("/api/documents/d-101"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            GIVEN POST /api/documents requires SCOPE_documents.write.
            WHEN requesting with sub=ana and only SCOPE_documents.write, send POST /api/documents with Content-Type application/json and body {"title":"Security notes","tenant":"alpha"}.
            THEN response is HTTP 201, Location=/api/documents/d-103, and JSON containing id=d-103, title=Security notes, owner=ana, tenant=alpha, archived=false.
            """)
    public void test4() throws Exception {
        mockMvc.perform(post("/api/documents")
                        .with(jwt().jwt(j -> j.subject("ana"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_documents.write"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title":"Security notes",
                                    "tenant":"alpha"
                                }
                                """)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/admin/audit requires the ADMIN role.
            WHEN Send GET /api/admin/audit with sub=operator and only the authority ROLE_ADMIN. Do not
                 add any scopes.
            THEN HTTP 200 and the JSON field eventCount equal to 2.
            """)
    public void test5_1() throws Exception {
        mockMvc.perform(get("/api/admin/audit")
                        .with(jwt()
                                .jwt(j -> j.subject("operator"))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("eventCount").value(2));
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/admin/audit requires the ADMIN role.
            WHEN Send GET /api/admin/audit with sub=operator and only the authority ROLE_ADMIN. Do not
                 add any scopes.
            THEN HTTP 200 and the JSON field eventCount equal to 2.
            """)
    @WithMockUser(username = "operator", roles = "ADMIN")
    public void test5_2() throws Exception {
        mockMvc.perform(get("/api/admin/audit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("eventCount").value(2));
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/reports/export requires SCOPE_reports.read AND SCOPE_reports.export
            WHEN sending GET /api/reports/export with sub=ana and only SCOPE_reports.read.
            THEN response is HTTP 403 because SCOPE_reports.export is missing.
            """)
    public void test6_1() throws Exception {
        mockMvc.perform(get("/api/reports/export")
                        .with(jwt()
                                .jwt(j -> j.subject("ana"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_reports.read"))
                        )
                ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/reports/export requires SCOPE_reports.read AND SCOPE_reports.export
            WHEN sending GET /api/reports/export with sub=ana and only SCOPE_reports.read.
            THEN response is HTTP 403 because SCOPE_reports.export is missing.
            """)
    @WithMockUser(username = "ana", authorities = "SCOPE_reports.read")
    public void test6_2() throws Exception {
        mockMvc.perform(get("/api/reports/export"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/reports/summary requires SCOPE_reports.read OR ROLE_AUDITOR.
            WHEN sending GET /api/reports/summary with sub=auditor and only ROLE_AUDITOR, without SCOPE_reports.read.
            THEN response status is HTTP 200 and the JSON field documentCount equal to 2.
            """)
    public void test7_1() throws Exception {
        mockMvc.perform(get("/api/reports/summary")
                        .with(jwt()
                                .jwt(j -> j.subject("auditor"))
                                .authorities(new SimpleGrantedAuthority("ROLE_AUDITOR"))
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentCount").value(2));
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/reports/summary requires SCOPE_reports.read OR ROLE_AUDITOR.
            WHEN sending GET /api/reports/summary with sub=auditor and only ROLE_AUDITOR, without SCOPE_reports.read.
            THEN response status is HTTP 200 and the JSON field documentCount equal to 2.
            """)
    @WithMockUser(username = "auditor", roles = "AUDITOR")
    public void test7_2() throws Exception {
        mockMvc.perform(get("/api/reports/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentCount").value(2));
    }


    @Test
    @DisplayName("""
            GIVEN GET /api/users/{userId}/documents requires SCOPE_documents.read AND userId equal to sub. ADMIN has no exception.
            WHEN sending GET /api/users/bogdan/documents with sub=ana and only SCOPE_documents.read.
            THEN we get HTTP 403: ana cannot request bogdan's documents.
            """)
    public void test8_1() throws Exception {
        mockMvc.perform(get("/api/users/bogdan/documents")
                        .with(jwt()
                                .jwt(j -> j.subject("ana"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_documents.read"))
                        )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            GIVEN GET /api/tenants/{tenantId}/documents requires SCOPE_documents.read AND the tenant claim equal to tenantId. ADMIN has no exception.
            WHEN sending GET /api/tenants/beta/documents with sub=ana, the claim tenant=alpha, and only SCOPE_documents.read.
            THEN HTTP 403 because the JWT tenant does not match the tenant in the URL.
            """)
    public void test9_1() throws Exception {
        mockMvc.perform(get("/api/tenants/beta/documents")
                        .with(jwt()
                                .jwt(a -> a.subject("ana").claim("tenant", "alpha"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_documents.read"))
                        )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            GIVEN PATCH /api/documents/{id}/archive requires only authentication at the HTTP layer. The service
                  method requires SCOPE_documents.write AND the document owner equal to sub.
            WHEN sending PATCH /api/documents/d-101/archive without a request body, with sub=bogdan and
                 only SCOPE_documents.write.
            THEN HTTP 403. Read d-101 from documentRepository and assert that archived is still false. Keep the
                 service real; do not replace it with a mock.
            """)
    public void test10_1() throws Exception {
        mockMvc.perform(patch("/api/documents/d-101/archive")
                        .with(jwt().jwt(j -> j.subject("bogdan"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_documents.write")))
                )
                .andExpect(status().isForbidden());

        Optional<Document> document = documentRepository.findById("d-101");

        assertTrue(document.isPresent());
        assertFalse(document.get().archived());
    }

}
