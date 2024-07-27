package org.proom.server.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.proom.server.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author vasyalike
 */
@WebMvcTest(BoardsController.class)
class BoardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @Test
    void testIndex() throws Exception {
        var res = this.mockMvc
                .perform(get("/boards"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"boards\":[]}", res.getResponse().getContentAsString());
        verify(boardService).getBoards();
    }

    @Test
    void testShow() throws Exception {
        var res = this.mockMvc
                .perform(get("/boards/ID"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"board\":null}", res.getResponse().getContentAsString());
        verify(boardService).getBoard("ID", null);
    }

    @Test
    void testShowForPlayer() throws Exception {
        var res = this.mockMvc
                .perform(get("/boards/ID/PLRID"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"board\":null}", res.getResponse().getContentAsString());
        verify(boardService).getBoard("ID", "PLRID");
    }

    @Test
    void testSitIn() throws Exception {
        var res = this.mockMvc
                .perform(
                        post("/boards/ID/sit-in")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(
                                        new BoardsController.SitInRequest("EID", 1)
                                ))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"board\":null}", res.getResponse().getContentAsString());
        verify(boardService).sitIn("ID", 1, "EID");
    }

    @Test
    void testFold() throws Exception {
        var res = this.mockMvc
                .perform(post("/boards/ID/fold/PLRID").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"board\":null}", res.getResponse().getContentAsString());
        verify(boardService).fold("ID", "PLRID");
    }

    @Test
    void testCall() throws Exception {
        var res = this.mockMvc
                .perform(post("/boards/ID/call/PLRID").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"board\":null}", res.getResponse().getContentAsString());
        verify(boardService).call("ID", "PLRID");
    }

    @Test
    void testCheck() throws Exception {
        var res = this.mockMvc
                .perform(post("/boards/ID/check/PLRID").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("application/json", res.getResponse().getContentType());
        assertEquals("{\"board\":null}", res.getResponse().getContentAsString());
        verify(boardService).check("ID", "PLRID");
    }
}
