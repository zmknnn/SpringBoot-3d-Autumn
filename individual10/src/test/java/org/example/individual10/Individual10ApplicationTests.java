
package org.example.individual10;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.sql.init.mode=always")
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class Individual10ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @WithMockUser
    void shouldCreateBookAndSaveItToDatabase() throws Exception {
        String requestBody = """
                {
                    "isbn": "978-1234567890",
                    "title": "The Song of Achilles",
                    "year": 2011,
                    "genre": "Historical Fiction",
                    "author": "Madeline Miller"
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Integer count = jdbcClient.sql("""
                SELECT COUNT(*)
                FROM books
                WHERE isbn = :isbn
                """)
                .param("isbn", "978-1234567890")
                .query(Integer.class)
                .single();

        assertEquals(1, count);
    }

    @Test
    @WithMockUser
    void shouldRejectBookWithEmptyTitleWithoutChangingDatabase()
            throws Exception {
        Integer countBefore = jdbcClient.sql(
                        "SELECT COUNT(*) FROM books")
                .query(Integer.class)
                .single();

        String requestBody = """
                {
                    "isbn": "978-1111111111",
                    "title": "",
                    "year": 2020,
                    "genre": "Fantasy",
                    "author": "Test Author"
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Integer countAfter = jdbcClient.sql(
                        "SELECT COUNT(*) FROM books")
                .query(Integer.class)
                .single();

        assertEquals(countBefore, countAfter);
    }
}
