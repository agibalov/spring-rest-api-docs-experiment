package me.loki2302;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetNotes extends AbstractDocumentationTest {
    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void success() throws Exception {
        createNote("1", "The note one");
        createNote("2", "The note two");
        createNote("3", "The note three");

        document.snippets(responseFields(
                fieldWithPath("[]").description("Collection of notes"),
                fieldWithPath("[].id").description("Note identifier"),
                fieldWithPath("[].text").description("Note text")
        ));

        mockMvc.perform(get("/api/notes/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void createNote(String id, String text) {
        Note note = new Note();
        note.id = id;
        note.text = text;
        noteRepository.save(note);
    }
}
