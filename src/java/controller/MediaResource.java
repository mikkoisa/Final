/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Media;
import model.Tag;
import model.Users;




@Path("media")
public class MediaResource {

    @Context private HttpServletRequest request;
    private UriInfo context;

    @EJB
    DBManager mm;

    public MediaResource() {

//        mm = new DBManager();
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
    
    private String tseisonBuilder2000(List<JsonObjectBuilder> objs, String arrayName) {
        StringBuilder s = new StringBuilder("{\"number\":"+mm.numberOfMedia()+",\""+arrayName+"\":[");
        String pre = "";
        for (JsonObjectBuilder o : objs) {
            s.append(pre);
            pre = ",";
            s.append(o.build().toString());
        }
        s.append("]}");
        return s.toString();
    }
    
    
    @GET
    @Path("/new/{from}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String newest(@PathParam("from") int from, @PathParam("amount") int amount) {

        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Media m:mm.findNewest(amount,from)) {
       
            out= Json.createObjectBuilder()
                .add("id", m.getId())
                .add("url", m.getUrl())
                .add("title", m.getTitle())
                .add("desc", m.getDescription())
                .add("rating", mm.findMediaRating(m.getId()))
                .add("user", m.getUser())
                    .add("username", mm.findUserById(m.getUser()).getName())
                .add("time", m.getTime().toString());
            objs.add(out);
        }
        
        return tseisonBuilder2000(objs,"media");
    }
        
    @GET
    @Path("/popular/{from}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String popular(@PathParam("from") int from, @PathParam("amount") int amount) {

        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Media m:mm.findPopular(amount,from)) {
       
            out= Json.createObjectBuilder()
                .add("id", m.getId())
                .add("url", m.getUrl())
                .add("title", m.getTitle())
                .add("desc", m.getDescription())
                .add("rating", mm.findMediaRating(m.getId()))
                .add("user", m.getUser())
                    .add("username", mm.findUserById(m.getUser()).getName())
                    
                .add("time", m.getTime().toString());
            objs.add(out);
        }
        
        return tseisonBuilder2000(objs,"media");
    }
    
    @GET
    @Path("/random/{from}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String random(@PathParam("from") int from, @PathParam("amount") int amount) {

        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Media m:mm.findRandom(amount,from)) {
       
            out= Json.createObjectBuilder()
                .add("id", m.getId())
                .add("url", m.getUrl())
                .add("title", m.getTitle())
                .add("desc", m.getDescription())
                .add("rating", mm.findMediaRating(m.getId()))
                .add("user", m.getUser())
                    .add("username", mm.findUserById(m.getUser()).getName())
                    
                .add("time", m.getTime().toString());
            objs.add(out);
        }
        
        return tseisonBuilder2000(objs,"media");
    }
    

    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String mediabyId(@PathParam("id") int i) {

        Media m = mm.findById(i);
        if (m==null) {
            return null;
        }
        JsonObjectBuilder out = null;
             out= Json.createObjectBuilder()
            .add("id", m.getId())
            .add("url", m.getUrl())
            .add("title", m.getTitle())
                     .add("desc", m.getDescription())
                     .add("rating", mm.findMediaRating(m.getId()))
            .add("user", m.getUser())
                     .add("username", mm.findUserById(m.getUser()).getName())
            .add("time", m.getTime().toString());

             
        return out.build().toString();
    }
    @POST
    @Path("{id}/upvote")
    public Response mediaUpvote(@PathParam("id") int mediaid) {
        
        Users u = (Users) request.getSession(false).getAttribute("user");
            if (u!= null && mm.upvote(u.getId(), mediaid) == true) {
                 return Response.status(Response.Status.CREATED).build();
  
            } else {
                 return Response.status(Response.Status.BAD_REQUEST).build();
                
            }
        }
    
    @GET
    @Path("{id}/tags")
     @Produces(MediaType.APPLICATION_JSON)
    public String mediaTags(@PathParam("id") int id) {

        /*
        StringBuilder sb = new StringBuilder("{\"tags\":[");
        String pre = "";
        for (String s:mm.getMediaTags(id)) {
            sb.append(pre);
            pre = ",";
            sb.append("\"").append(s).append("\"");
        }
        sb.append("]}");

        return sb.toString();*/
        
        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Tag t:mm.getMediaTags(id)) {
       
            out= Json.createObjectBuilder()
                .add("id", t.getId())
                .add("name", t.getName());
            objs.add(out);
        }
        
        return tseisonBuilder(objs,"tags");
    }
    
    @GET
    @Path("tags")
    @Produces(MediaType.APPLICATION_JSON)
    public String allTags() {
        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Tag t:mm.getTags()) {
       
            out= Json.createObjectBuilder()
                .add("id", t.getId())
                .add("name", t.getName());
            objs.add(out);
        }
        
        return tseisonBuilder(objs,"tags");
    }
    
    @GET
    @Path("tags/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public String mediaByTag(@PathParam("tag") String tag) {
        
        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Media m:mm.findByTag(tag)) {
       
            out= Json.createObjectBuilder()
                .add("id", m.getId())
                .add("url", m.getUrl())
                .add("title", m.getTitle())
                    .add("desc", m.getDescription())
                     .add("rating", mm.findMediaRating(m.getId()))
                .add("user", m.getUser())
                    .add("username", mm.findUserById(m.getUser()).getName())
                .add("time", m.getTime().toString());
            objs.add(out);
        }
        
        return tseisonBuilder(objs,"media");
    }
    
    @GET
    @Path("tags/id/{tagid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String mediaByTagId(@PathParam("tagid") int tagid) {
        
        JsonObjectBuilder out = null;
        List<JsonObjectBuilder> objs = new ArrayList<>();
        for (Media m:mm.findByTagId(tagid)) {
       
            out= Json.createObjectBuilder()
                .add("id", m.getId())
                .add("url", m.getUrl())
                .add("title", m.getTitle())
                    .add("desc", m.getDescription())
                .add("user", m.getUser())
                    .add("username", mm.findUserById(m.getUser()).getName())
                .add("time", m.getTime().toString());
            objs.add(out);
        }
        
        return tseisonBuilder(objs,"media");
    }


    
  
    
    /**
     * PUT method for updating or creating an instance of UploadResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
