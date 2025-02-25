package customer.cap_java_test;

import static cds.gen.catalogservice.CatalogService_.BOOKS;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;

import com.sap.cds.ql.Delete;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.adminservice.Books;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminServiceITest {
    private static final String booksURI = "/odata/v4/AdminService/Books";
    private static final String ADMIN_USE_STRING = "admin";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersistenceService db;

    @AfterEach
    public void cleanup() {
        db.run(Delete.from(BOOKS));
    }

    @Test
    @WithMockUser(ADMIN_USE_STRING)
    public void createBook() throws Exception {
        Books book = Books.create();
        book.setTitle("test");

        String payload = book.toJson();
        mockMvc.perform(post(booksURI).contentType("application/json").content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("test"));
    }
}
