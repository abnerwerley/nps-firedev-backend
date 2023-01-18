package com.nps.answer.service;

import com.nps.answer.entity.Answer;
import com.nps.answer.entity.mapper.AnswerMapper;
import com.nps.answer.json.AnswerForm;
import com.nps.answer.json.AnswerResponse;
import com.nps.answer.persistence.AnswerRepository;
import com.nps.exception.RequestException;
import com.nps.exception.ResourceNotFoundException;
import com.nps.question.entity.Question;
import com.nps.question.persistence.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @InjectMocks
    private AnswerService service;

    @Mock
    private AnswerRepository repository;

    @Mock
    private QuestionRepository questionRepository;

    public static final Long ID = 1L;
    public static final Long QUESTION_ID = 1L;
    public static final String RESPONSE = "Pretty good response for a pretty good question.";
    public static final int POINTS = 1;

//    @Test
//    void testRegisterAnswer() {
//        doReturn(getOptionalQuestion()).when(questionRepository).findById(getAnswerForm().getQuestionId());
//        doReturn(getAnswer()).when(repository).save(AnswerMapper.fromFormToEntity(getAnswerForm()));
//        AnswerResponse response = service.registerAnswer(getAnswerForm());
//        Assertions.assertNotNull(response);
//        verify(repository).save(AnswerMapper.fromFormToEntity(getAnswerForm()));
//        verify(questionRepository).findById(QUESTION_ID);
//    }

    @Test
    void testRegisterAnswerWithNoPoints() {
        doReturn(getOptionalQuestion()).when(questionRepository).findById(QUESTION_ID);
        Exception e = assertThrows(RequestException.class, () -> service.registerAnswer(getAnswerFormWithNoPoints()));
        assertEquals("Every answer must have a score.", e.getMessage());
        verify(questionRepository).findById(QUESTION_ID);
    }

    @Test
    void testRegisterAnswerNonexistentQuestionId() {
        doThrow(ResourceNotFoundException.class).when(questionRepository).findById(ID);
        Exception e = assertThrows(ResourceNotFoundException.class, () -> service.registerAnswer(getAnswerForm()));
        assertEquals("Question not found with id: 1", e.getMessage());
    }

    @Test
    void testRegisterAnswerException() {
        doReturn(getOptionalQuestion()).when(questionRepository).findById(QUESTION_ID);
        doThrow(RequestException.class).when(repository).save(AnswerMapper.fromFormToEntity(getAnswerForm()));
        Exception e = assertThrows(RequestException.class, () -> service.registerAnswer(getAnswerForm()));
        assertEquals("Error when registering answer.", e.getMessage());
        verify(questionRepository).findById(QUESTION_ID);
    }

    @Test
    void testGetAllAnswers() {
        doReturn(getAllAnswers()).when(repository).findAll();
        Stream<AnswerResponse> response = service.getAllAnswers();
        assertNotNull(response);
        verify(repository).findAll();
    }

    @Test
    void testGetAllAnswersException() {
        doThrow(RequestException.class).when(repository).findAll();
        Exception exception = assertThrows(Exception.class, () -> service.getAllAnswers());
        assertEquals("Error when getting all answers.", exception.getMessage());
    }

    @Test
    void testGetAnswerById() {
        doReturn(getOptionalAnswer()).when(repository).findById(ID);
        Optional<AnswerResponse> response = service.getAnswerById(ID);
        assertNotNull(response);
        verify(repository).findById(ID);
    }

    @Test
    void testGetAnswerByIdWithNonexistentId() {
        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Answer does not exist.", exception.getMessage());
    }

    @Test
    void testGetAnswerByIdException() {
        doThrow(RequestException.class).when(repository).findById(ID);
        Exception exception = assertThrows(RequestException.class, () -> service.getAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Error when getting answer by id.", exception.getMessage());
    }

    @Test
    void testDeleteAnswer() {
        doReturn(getOptionalAnswer()).when(repository).findById(ID);
        String response = service.deleteAnswerById(ID);
        assertNotNull(response);
        assertEquals("Answer deleted.", response);
        verify(repository).findById(ID);
    }

    @Test
    void testDeleteAnswerWithNonexistentId() {
        doThrow(ResourceNotFoundException.class).when(repository).findById(ID);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Answer does not exist.", exception.getMessage());
    }

    @Test
    void testDeleteAnswerException() {
        doThrow(RequestException.class).when(repository).findById(ID);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteAnswerById(ID));
        assertNotNull(exception);
        assertEquals("Error when deleting answer by id.", exception.getMessage());
    }

    private AnswerForm getAnswerForm() {
        return AnswerForm.builder()
                .response(RESPONSE)
                .score(POINTS)
                .questionId(QUESTION_ID)
                .build();
    }

    private AnswerForm getAnswerFormWithNoPoints() {
        return AnswerForm.builder()
                .response(RESPONSE)
                .questionId(QUESTION_ID)
                .build();
    }

    private Answer getAnswer() {
        return Answer.builder()
                .answerId(ID)
                .response(RESPONSE)
                .score(POINTS)
                .questionId(QUESTION_ID)
                .build();
    }

    private List<Answer> getAllAnswers() {
        return List.of(Answer.builder()
                .answerId(ID)
                .score(POINTS)
                .response(RESPONSE
                ).build());
    }

    private Optional<Answer> getOptionalAnswer() {
        return Optional.of(Answer.builder()
                .answerId(ID)
                .response(RESPONSE)
                .score(POINTS)
                .build());
    }

    private Optional<Question> getOptionalQuestion() {
        return Optional.of(Question.builder()
                .questionId(QUESTION_ID)
                .enquiry("Nice enquiry")
                .build());
    }

    private Question getQuestion() {
        return Question.builder()
                .questionId(QUESTION_ID)
                .enquiry("Nice enquiry")
                .build();
    }
}
