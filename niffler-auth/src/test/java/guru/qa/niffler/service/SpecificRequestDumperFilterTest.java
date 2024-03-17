package guru.qa.niffler.service;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecificRequestDumperFilterTest {

    @Test
    void doFilterTest(@Mock HttpServletRequest request,
                      @Mock ServletResponse response,
                      @Mock FilterChain chain,
                      @Mock GenericFilter decorate) throws ServletException, IOException {
        final SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(
                decorate, "/login", "/oauth2/.*"
        );

        lenient().when(request.getRequestURI())
                .thenReturn("/login");

        specificRequestDumperFilter.doFilter(request, response, chain);

        verify(decorate, times(1)).doFilter(request, response, chain);
        verify(chain, times(0)).doFilter(request, response);
    }

    @Test
    void doFilterWithNotHttpServletRequestTest(@Mock ServletRequest request,
                                               @Mock ServletResponse response,
                                               @Mock FilterChain chain,
                                               @Mock GenericFilter decorate) throws ServletException, IOException {
        final SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(
                decorate, "/login", "/oauth2/.*"
        );

        specificRequestDumperFilter.doFilter(request, response, chain);

        verify(decorate, times(0)).doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterForNonPatternUriTest(@Mock HttpServletRequest request,
                                      @Mock ServletResponse response,
                                      @Mock FilterChain chain,
                                      @Mock GenericFilter decorate) throws ServletException, IOException {
        final SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(
                decorate, "/login", "/oauth2/.*"
        );

        lenient().when(request.getRequestURI())
                .thenReturn("/logout");

        specificRequestDumperFilter.doFilter(request, response, chain);

        verify(decorate, times(0)).doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterWithoutPatternsTest(@Mock HttpServletRequest request,
                                     @Mock ServletResponse response,
                                     @Mock FilterChain chain,
                                     @Mock GenericFilter decorate) throws ServletException, IOException {
        final SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate);

        lenient().when(request.getRequestURI())
                .thenReturn("/login");

        specificRequestDumperFilter.doFilter(request, response, chain);

        verify(decorate, times(0)).doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void destroyTest(@Mock GenericFilter decorate) {
        final SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate);
        specificRequestDumperFilter.destroy();
        verify(decorate, times(1)).destroy();
    }
}