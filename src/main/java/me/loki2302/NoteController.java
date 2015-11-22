package me.loki2302;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

@RestController
@RequestMapping("/api/notes")
@Api(
        description = "Note Controller description", // OK
        consumes = MediaType.APPLICATION_JSON_VALUE, // OK
        produces = MediaType.APPLICATION_JSON_VALUE // FAIL: Swagger says */* instead
)
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(
            value = "Create a new note", // OK
            notes = "Use this request to create a new note", // OK
            code = 201, // FAIL: Swagger keeps saying 200
            responseHeaders = { // FAIL: Swagger doesn't display this anywhere
                    @ResponseHeader(
                            name = "Location",
                            description = "Location of the newly created note")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    code = 201, // OK
                    message = "Note has been created successfully", // OK
                    responseHeaders = { // FAIL: Swagger doesn't display this anywhere
                            @ResponseHeader(
                                    name = "Location",
                                    description = "Location of the newly created note")
                    }
            ),
            @ApiResponse(
                    code = 400, // OK
                    message = "Request was not valid" // OK
            )
    })
    public ResponseEntity createNote(
            // FAIL: Swagger displays NoteDto instead of NoteAttributesDto (i.e. there's "id")
            @ApiParam(
                    value = "Note attributes" // OK
            )
            @Valid @RequestBody NoteAttributesDto noteAttributesDto) {

        Note note = new Note();
        note.id = UUID.randomUUID().toString();
        note.text = noteAttributesDto.text;
        note = noteRepository.save(note);

        String noteId = note.id;
        URI noteUri = fromMethodCall(on(NoteController.class).getNote(noteId)).build().toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(noteUri);

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an existing note or create note with id", // OK
            notes = "Use this request to update an existing note or create a new note" // OK
            // FAIL: Swagger says that default response is 200
    )
    @ApiResponses({
            @ApiResponse(
                    code = 201, // OK
                    message = "Note has been created successfully", // OK
                    responseHeaders = { // FAIL: Swagger doesn't display this anywhere
                            @ResponseHeader(
                                    name = "Location",
                                    description = "Location of the newly created note")
                    }
            ),
            @ApiResponse(
                    code = 204, // OK
                    message = "Note has been updated successfully" // OK
            ),
            @ApiResponse(
                    code = 400, // OK
                    message = "Request was not valid" // OK
            )
    })
    public ResponseEntity createOrUpdateNote(
            @ApiParam( // OK
                    value = "Note id"
            )
            @PathVariable("noteId") String noteId,

            // FAIL: Swagger displays NoteDto instead of NoteAttributesDto (i.e. there's "id")
            @ApiParam(
                    value = "Note attributes" // OK
            )
            @Valid @RequestBody NoteAttributesDto noteDto) {

        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            note = new Note();
            note.id = noteId;
            note.text = noteDto.text;
            noteRepository.save(note);

            URI noteUri = fromMethodCall(on(NoteController.class).getNote(noteId)).build().toUri();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(noteUri);

            return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
        }

        note.text = noteDto.text;
        noteRepository.save(note);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all notes", // OK
            notes = "Use this request to get all notes", // OK
            code = 200, // OK
            responseContainer = "List", // OK
            response = NoteDto.class // OK
    )
    @ApiResponses({
            @ApiResponse( // FAIL: Swagger doesn't display this anywhere
                    code = 200,
                    message = "List of notes"
            )
    })
    public ResponseEntity getNotes() {
        List<Note> notes = noteRepository.findAll();
        List<NoteDto> noteDtos = makeNoteDtosFromNotes(notes);

        return new ResponseEntity(noteDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get note by id", // OK
            notes = "Use this request to get a note by id", // OK
            code = 200, // OK
            response = NoteDto.class // OK
    )
    @ApiResponses({
            @ApiResponse( // FAIL: Swagger doesn't display this anywhere
                    code = 200,
                    message = "Note exists"
            ),
            @ApiResponse( // OK
                    code = 404,
                    message = "Note doesn't exist"
            )
    })
    public ResponseEntity getNote(
            @ApiParam( // OK
                    value = "Note id"
            )
            @PathVariable("noteId") String noteId) {

        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            throw new NotFoundApiException();
        }

        NoteDto noteDto = makeNoteDtoFromNote(note);

        return new ResponseEntity(noteDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.DELETE)
    @ApiOperation(
            value = "Delete note by id", // OK
            notes = "Use this request to delete an existing note by id", // OK
            code = 204 // FAIL: Swagger keeps saying 200
    )
    @ApiResponses({
            @ApiResponse( // FAIL: Swagger doesn't display this anywhere
                    code = 204,
                    message = "Note has been delete successfully"
            ),
            @ApiResponse( // OK
                    code = 404,
                    message = "Note doesn't exist"
            )
    })
    public ResponseEntity deleteNote(
            @ApiParam(
                    value = "Note id"
            )
            @PathVariable("noteId") String noteId) {

        boolean noteExists = noteRepository.exists(noteId);
        if(!noteExists) {
            throw new NotFoundApiException();
        }

        noteRepository.delete(noteId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException e) {
        return e.getResponseEntity();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorFields = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        Map<String, Object> dto = new HashMap<>();
        dto.put("error", "VALIDATION");
        dto.put("errorFields", errorFields);
        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }

    private static NoteDto makeNoteDtoFromNote(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.id = note.id;
        noteDto.text = note.text;
        return noteDto;
    }

    private static List<NoteDto> makeNoteDtosFromNotes(List<Note> notes) {
        return notes.stream()
                .map(NoteController::makeNoteDtoFromNote)
                .collect(Collectors.toList());
    }
}
