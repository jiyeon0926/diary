package diary.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {
    private static final String[] WHITE_LIST = {"/users", "/login"};


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();

        // 로그인이 안돼있으면 401실행
        if (!isWhiteList(requestURI, method)) {
            HttpSession session = httpServletRequest.getSession(false);

            // 만약 세션이 비어있다면 401 상태코드 throw
            if (session == null || session.getAttribute("loginUser") == null) {

                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 화이트리스트거나 게시글 조회
    private boolean isWhiteList(String requestURI, String method) {

        // 게시글 조회할 때는 로그인 없이 가능
        if (PatternMatchUtils.simpleMatch(new String[]{"/boards", "/boards/*"}, requestURI) && method.equals("GET")) {
            return true;
        }

        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
