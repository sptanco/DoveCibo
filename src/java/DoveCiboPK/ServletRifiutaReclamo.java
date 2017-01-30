package DoveCiboPK;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author stefano
 */
@WebServlet(name = "ServletRifiutaReclamo", urlPatterns = {"/ServletRifiutaReclamo"})
public class ServletRifiutaReclamo extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //RISTORANTE
            Integer idRO = Integer.parseInt(request.getParameter("idGen"));            
            
            //CREATORE
            Restaurant R = (Restaurant) request.getSession(false).getAttribute("ristorante"); 
            User u = (User) request.getSession(false).getAttribute("User");
            
            //FILTRO USER
            if (! u.getRole().equals("1") ) {
                request.setAttribute("error", "Zona protetta!");
                request.getRequestDispatcher("errore.jsp").forward(request, response);                    
            } else {  
                //INSERIMENTO DB
                if(! new DB_Manager().rifiutaReclamo(idRO))
                    request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);     
                    response.sendRedirect("/DoveCiboGit/reclamoRifiutato.jsp");
            }
        } catch (SQLException ex) {
            request.setAttribute("error", ex.toString());
            request.getRequestDispatcher("errore.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
