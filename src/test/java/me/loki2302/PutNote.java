package me.loki2302;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.dto.NoteAttributesDto;
import me.loki2302.dto.NoteDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PutNote extends AbstractDocumentationTest {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createNewSuccess() throws Exception {
        NoteAttributesDto noteAttributesDto = new NoteAttributesDto();
        noteAttributesDto.text = "hello there";

        document.snippets(
                pathParameters(
                    parameterWithName("id").description("Note identifier")
                ),
                responseHeaders(
                        headerWithName("Location").description("The note location")
                ));

        mockMvc.perform(put("/api/notes/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteAttributesDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createNewBadRequestValidationError() throws Exception {
        NoteAttributesDto noteAttributesDto = new NoteAttributesDto();
        noteAttributesDto.text = "";

        document.snippets(
                pathParameters(
                        parameterWithName("id").description("Note identifier")
                ),
                responseFields(
                        fieldWithPath("error").description("Error reason"),
                        fieldWithPath("message").description("Human-readable error message"),
                        fieldWithPath("errorFields").description("Fields in error")
                ));

        mockMvc.perform(put("/api/notes/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteAttributesDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateExistingSuccess() throws Exception {
        Note note = new Note();
        note.id = "123";
        note.text = "hi";
        noteRepository.save(note);

        NoteAttributesDto noteAttributesDto = new NoteAttributesDto();
        noteAttributesDto.text = "hello there";

        document.snippets(
                pathParameters(
                        parameterWithName("id").description("Note identifier")
                ));

        mockMvc.perform(put("/api/notes/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteAttributesDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateBadRequestValidationError() throws Exception {
        Note note = new Note();
        note.id = "123";
        note.text = "hi";
        noteRepository.save(note);

        NoteAttributesDto noteAttributesDto = new NoteAttributesDto();
        noteAttributesDto.text = "";

        document.snippets(
                pathParameters(
                        parameterWithName("id").description("Note identifier")
                ),
                responseFields(
                        fieldWithPath("error").description("Error reason"),
                        fieldWithPath("message").description("Human-readable error message"),
                        fieldWithPath("errorFields").description("Fields in error")
                ));

        mockMvc.perform(put("/api/notes/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteAttributesDto)))
                .andExpect(status().isBadRequest());
    }
}
