package me.loki2302;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.dto.NoteAttributesDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostNote extends AbstractDocumentationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void success() throws Exception {
        NoteAttributesDto noteAttributesDto = new NoteAttributesDto();
        noteAttributesDto.text = "hello there";

        document.snippets(responseHeaders(
                headerWithName("Location").description("The note location")
        ));

        mockMvc.perform(post("/api/notes/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteAttributesDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void badRequestValidation() throws Exception {
        NoteAttributesDto noteAttributesDto = new NoteAttributesDto();
        noteAttributesDto.text = "";

        document.snippets(responseFields(
                fieldWithPath("error").description("Error reason"),
                fieldWithPath("message").description("Human-readable error message"),
                fieldWithPath("errorFields").description("Fields in error")
        ));

        mockMvc.perform(post("/api/notes/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteAttributesDto)))
                .andExpect(status().isBadRequest());
    }
}
