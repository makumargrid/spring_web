package com.example.springweb.servlet;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class InfoServletTest {

    @Test
    void doGetWritesJsonBody() throws Exception {
        InfoServlet servlet = new InfoServlet();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/servlet/info");
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).contains("application/json");
        assertThat(response.getContentAsString()).contains("InfoServlet");
    }
}
