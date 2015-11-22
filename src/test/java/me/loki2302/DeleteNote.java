package me.loki2302;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteNote extends AbstractDocumentationTest {
    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void success() throws Exception {
        Note note = new Note();
        note.id = "123";
        note.text = "Hello there";
        note = noteRepository.save(note);

        document.snippets(
                pathParameters(
                        parameterWithName("id").description("Note identifier")
                ));

        mockMvc.perform(delete("/api/notes/{id}", note.id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void notFound() throws Exception {
        document.snippets(
                pathParameters(
                        parameterWithName("id").description("Note identifier")
                ),
                responseFields(
                        fieldWithPath("error").description("Error reason"),
                        fieldWithPath("message").description("Human-readable error message")
                ));

        mockMvc.perform(delete("/api/notes/{id}", "123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
