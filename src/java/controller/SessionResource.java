/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Users;

/**
 * REST Web Service
 *
 * @author buckfast
 */

@Path("s")
public class SessionResource {


    @Context private HttpServletRequest request;
    private UriInfo context;

    //private DBManager dm;
    @EJB
    DBManager dm;
    //private final EntityManager em;
    //private EntityTransaction t;
    
    private boolean status;
    /**
     * Creates a new instance of HehheResource
     */
    public SessionResource() {
        //em = Persistence.createEntityManagerFactory("projecktiPU").createEntityManager();
       // dm = new DBManager();
    }

    
    /**
     * Retrieves representation of an instance of controller.HehheResource
     * @return an instance of java.lang.String
     */

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response getInfo() {
            
            model.Users u = null;
            int id;
            String name;
            if (request.getSession(false) != null) {
                status = true;
                 u = (Users) request.getSession(false).getAttribute("user"); 
                id = u.getId();
                name = u.getName();
            } else {
                status = false;
                name="";
                id=0;
                String out = Json.createObjectBuilder()
            .add("status", status)
            .build()
            .toString();
            //return out;
            return Response.status(Response.Status.FORBIDDEN).entity(out).build();

                //return Response.status(Response.Status.UNAUTHORIZED).build();
                //return Response.accepted().entity(out).build();
                
            }
             
		//return "{\"status\":"+status+",\"user\":\""+username+"\"}";
            String out = Json.createObjectBuilder()
            .add("id",id)
            .add("status", status)
            .add("user", name)
            .build()
            .toString();
            //return out;
            return Response.status(Response.Status.ACCEPTED).entity(out).build();
	}

        
        @POST
        @Path("signup")
	public Response signup(@FormParam("signup_username") String user,@FormParam("signup_password") String password) {
        
            if (dm.isUserFound(user) != false) {
                Users u = dm.createUser(user, password);
                request.getSession(true);
                request.getSession(false).setAttribute("user",u);
                return Response.status(Response.Status.CREATED).build();
                
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        
        @POST
        @Path("login")
	public Response login(@FormParam("login_username") String juuseri,@FormParam("login_password") String password) {
            
                model.Users user = dm.findUserByName(juuseri);
                System.out.println(user);

               // t = em.getTransaction();
                //t.begin();
               // Query q = em.createQuery("SELECT u.name FROM Users u WHERE u.name = '"+username+"'");
                /*Query id = em.createQuery("SELECT u.id FROM Users u WHERE u.name = '"+juuseri+"'");
                List<String> userid = id.getResultList();

                if (!userid.isEmpty()) {
                        user = em.find(model.Users.class, userid.get(0));
                }
                t.commit();*/
               // if (user != null) {
                    if ( password.equals(user.getPassword())) {
                        request.getSession(true);
                        request.getSession(false).setAttribute("user",user);


                        /*
                               try {
                                    java.net.URI location = new java.net.URI("/projeckti");
                                    return Response.temporaryRedirect(location).build();
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }*/
                       return Response.status(Response.Status.ACCEPTED).build();
                               //return true;
                      // return null;

                    }else {
                          return Response.status(Response.Status.FORBIDDEN).build();
                    }
                }
                
                //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        //} 
    

    /**
     * PUT method for updating or creating an instance of HehheResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
    
    
    @GET
    @Path("logout")
    public Response dssds() {
            if (request.getSession(false) != null) {
                    request.getSession(false).invalidate();
                    try {
                        java.net.URI location = new java.net.URI("/projeckti");
                        return Response.temporaryRedirect(location).build();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            return Response.notModified().build();
    }

    
}
