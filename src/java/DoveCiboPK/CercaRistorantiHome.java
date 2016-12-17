/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DoveCiboPK;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.SQLException;
//import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author stefano
 */
@WebServlet(name = "CercaRistorantiHome", urlPatterns = {"/CercaRistorantiHome"})
public class CercaRistorantiHome extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            
            String string = request.getParameter("go").toLowerCase();
            
            String[] separated = string.split("\\s*,\\s*");
            
            
            Set <Integer> id = new HashSet <Integer> ();
            
            for(int i = 0;i<separated.length;i++){
                for(int g = 0; g < separated[i].length();g++){
                    if(g >= 2){
                        String str = separated[i].substring(0, g+1);
                        ArrayList <Integer> list = new ArrayList <Integer>();
                        Restaurant res = new Restaurant(-1, str, "", "",null, null, null, null);
                        if(!(new DB_Manager()).SetResForName(res, list)){
                            request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                        }
                        for(int j = 0;j<list.size();j++){
                            id.add(list.get(j));
                        }

                        ArrayList <Integer> list1 = new ArrayList <Integer>();
                        Coordinate cor = new Coordinate(null,null,str,str,str);
                        if(!(new DB_Manager()).SetResForNazione(cor, list1)){
                            request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                        }
                        for(int j = 0;j<list1.size();j++){
                            id.add(list1.get(j));
                        }

                        ArrayList <Integer> list2 = new ArrayList <Integer>();
                        if(!(new DB_Manager()).SetResForCity(cor, list2)){
                            request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);   
                        }
                        for(int j = 0;j<list2.size();j++){
                            id.add(list2.get(j));
                        }

                        ArrayList <Integer> list3 = new ArrayList <Integer>();
                        if(!(new DB_Manager()).SetResForAdrers(cor, list3)){
                            request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                        }
                        for(int j = 0;j<list3.size();j++){
                           id.add(list3.get(j));
                        }

                        ArrayList <Integer> list4 = new ArrayList <Integer>();
                        ArrayList <Integer> list5 = new ArrayList <Integer>();
                        
                        if(!(new DB_Manager()).SetResForCuisine(str,list4)){
                            request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                        }
                        for(int j = 0;j<list4.size();j++){
                            if(!(new DB_Manager()).SetResForCuisineId(list4.get(j),list5)){
                                request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                            }
                        }
                        for(int j = 0;j<list5.size();j++){
                           id.add(list5.get(j));
                        }  
                    }
                }
            }
            
           
            
            
            
            session.removeAttribute("id_restaurant");
            session.removeAttribute("res_name");
            session.removeAttribute("cuisine_name");
            session.removeAttribute("prezzo_min");
            session.removeAttribute("prezzo_max");
            session.removeAttribute("res_address");
            session.removeAttribute("res_city");
            session.removeAttribute("res_id");
            
            session.setAttribute("id_restaurant", id);
            
            int i= 0;   
            for (Iterator<Integer> it = id.iterator(); it.hasNext(); ) {
                Integer f = it.next();
                session.setAttribute("res_id"+i, f);
                Restaurant res = new Restaurant(f, "", "", "",null, null, null, null);
                if(!(new DB_Manager()).cercaRistorante_perId(res)){
                    request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                }
                session.setAttribute("res_name"+i, res.getName());
                
                List<Integer> id_cuisine = new ArrayList<Integer>();
                List<String> cuisine_name = new ArrayList<String>();
                if(!(new DoveCiboPK.DB_Manager()).cercaCuisine_perId_Restaurant(res, id_cuisine)){
                    request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                }
                Iterator itr1 = id_cuisine.iterator();
                while (itr1.hasNext()) {
                    Integer element1 = (Integer) itr1.next();

                    if(!(new DoveCiboPK.DB_Manager()).cercaCuisine(element1, cuisine_name)){
                        request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                    }

                }
                session.setAttribute("cuisine_name"+i, cuisine_name);
                
                Integer prezzo[] = {0,0};
                if(!(new DoveCiboPK.DB_Manager()).cercaPriceRanges_Restaurant(res, prezzo)){
                    request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                }
                session.setAttribute("prezzo_min"+i, prezzo[0]);
                session.setAttribute("prezzo_max"+i, prezzo[1]);
                
                String address[] = {"w", "W"};
                if(!(new DoveCiboPK.DB_Manager()).cercaCoordinate_perID_Restaurant(res, address)){
                    request.getRequestDispatcher("erroreConnessione.jsp").forward(request, response);
                }
                session.setAttribute("res_address"+i, address[0]);
                session.setAttribute("res_city"+i, address[1]);
                
                i++;
                
            }
            
            response.sendRedirect("/DoveCiboGit/CercaRistorantiHome.jsp");
            
    
        } catch (Exception ex) {
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
    }// </editor-fold>

}