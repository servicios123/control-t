/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.gov.aerocivil.controlt.web.filter;

//import co.edu.udistrital.bolsa.web.bbeans.util.JsfUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class LoginFilter implements Filter{

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        String loginURI = filterConfig.getInitParameter("LoginURI");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        /*HttpSession session = request.getSession();

        if (session == null || session.getAttribute("FuncionarioTO") == null) {
            response.sendRedirect(request.getContextPath() + loginURI); // No logged-in user found, so redirect to login page.
        } else {
            chain.doFilter(req, res); // Logged-in user found, so just continue request.
        }*/

        /**/

        HttpSession session = request.getSession();
        
        if (session!=null && session.getAttribute("FuncionarioTO") != null || request.getRequestURI().endsWith("login.xhtml")) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
            chain.doFilter(req, res);
            
        } else {            
            //HttpServletResponse res = (HttpServletResponse) response;
            //res.sendRedirect( loginURI);
            //String timeoutPage = request.getContextPath() + "/faces/sessionExpired.xhtml";
            /*if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
                response.setContentType("text/xml");
                response.getWriter()
                    .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", loginURI);
            }
            else {
                response.sendRedirect(loginURI);
            }*/
            //System.out.println("loginURI::::" + loginURI);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(loginURI);
            // Force the login
            requestDispatcher.forward(req, res);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
    }


}
