/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Comments;
import model.Users;

/**
 * REST Web Service
 *
 * @author buckfast
 */


@Path("comments")
public class CommentsResource {


    //
    
    private UriInfo context;
    
     @EJB
    DBManager dm;
    //private DBManager dm;
    /**
     * Creates a new instance of CommentsResource
     */
    public CommentsResource() {
       // dm = new DBManager();
    }
    

    
    private String tseisonBuilder(List<JsonObjectBuilder> objs, String arrayName) {
        StringBuilder s = new StringBuilder("{\""+arrayName+"\":[");
        String pre = "";
        for (JsonObjectBuilder o : objs) {
            s.append(pre);
            pre = ",";
            s.append(o.build().toString());
        }
        s.append("]}");
        return s.toString();
    }
    
    @POST

    public Response submitComment(@FormParam("message") String msg, @FormParam("media") int mid, @FormParam("user") int uid, @Context HttpServletRequest request) {

        Users u = (Users)request.getSession(false).getAttribute("user");
        dm.submitComment(u.getId(), mid, msg);
        
        return Response.status(Response.Status.ACCEPTED).entity("hehe").build();

    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String commentsByMediaId(@PathParam("id") int i) {
        List<Comments> list = dm.findCommentsByMediaId(i);

        
        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Comments c:list) {
            
            Date timmeee = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh.mm");
            out= Json.createObjectBuilder()
                .add("id",c.getId())
                .add("comment", c.getComment())
                .add("user", dm.findUserById(c.getUserid()).getName())
                .add("media",c.getMediaId())
                .add("time", formatter.format(timmeee));
            objs.add(out);

        }
        
        
        
        return tseisonBuilder(objs,"comments");
    }
    

    
    /**
     * Retrieves representation of an instance of controller.CommentsResource
     * @return an instance of java.lang.String
     */

    

    /**
     * PUT method for updating or creating an instance of CommentsResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
