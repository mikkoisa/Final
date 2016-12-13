/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import model.Media;
import model.Users;

/**
 *
 * @author buckfast
 */
// /var/www/html/content
@MultipartConfig(location = "/var/www/html/content") 
@WebServlet(name = "upload", urlPatterns = {"/upload"})
public class Upload extends HttpServlet {

    @EJB
    DBManager dm;
   
    protected Response processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            String title = request.getParameter("uptitle");
            String desc = request.getParameter("updesc");
            String tags = request.getParameter("uptags");
            System.out.println(title);
            
            Users u;
            request.getSession(true);
            u = (Users) request.getSession(false).getAttribute("user");
            if (u != null) {
            String filename = u.getId()+"_"+ request.getPart("upphoto").getSubmittedFileName();
            
            request.getPart("upphoto").write(filename);
            
            
            String[] s = tags.split("\\s+");
            
            String asd = "/var/www/html/content" + filename;
            String asd2 = "t_"+"/var/www/html/content" + filename;
            File thumb = createThumbnail(asd,asd2);
            //10.114.32.34
            Media m = new Media("http://10.114.32.34/content/" + filename, title, u.getId(), desc, asd2);
            
            
            
            
            int mediaid = dm.addMedia(m);
            for (int i =0; i<s.length; i++) {
                dm.addTagToMedia(s[i], mediaid);
            }
            
            response.sendRedirect("/projeckti");
            } else {
                return null;
            }
          return null;  
        } 
    
    public File createThumbnail(String imageUrl, String targetPath) {
    final int imageSize = 100;
    File thumbnail = new File(targetPath);

    try {
        thumbnail.getParentFile().mkdirs();
        thumbnail.createNewFile();
        BufferedImage sourceImage = ImageIO.read(new File(imageUrl));
        float width = sourceImage.getWidth();
        float height = sourceImage.getHeight();

        BufferedImage img2;
        if (width > height) {
            float scaledWidth = (width / height) * (float) imageSize;
            float scaledHeight = imageSize;

            BufferedImage img = new BufferedImage((int) scaledWidth, (int) scaledHeight, sourceImage.getType());
            Image scaledImage = sourceImage.getScaledInstance((int) scaledWidth, (int) scaledHeight, Image.SCALE_SMOOTH);
            img.createGraphics().drawImage(scaledImage, 0, 0, null);

            int offset = (int) ((scaledWidth - scaledHeight) / 2f);
            img2 = img.getSubimage(offset, 0, imageSize, imageSize);
        }
        else if (width < height) {
            float scaledWidth = imageSize;
            float scaledHeight = (height / width) * (float) imageSize;

            BufferedImage img = new BufferedImage((int) scaledWidth, (int) scaledHeight, sourceImage.getType());
            Image scaledImage = sourceImage.getScaledInstance((int) scaledWidth, (int) scaledHeight, Image.SCALE_SMOOTH);
            img.createGraphics().drawImage(scaledImage, 0, 0, null);

            int offset = (int) ((scaledHeight - scaledWidth) / 2f);
            img2 = img.getSubimage(0, offset, imageSize, imageSize);
        }
        else {
            img2 = new BufferedImage(imageSize, imageSize, sourceImage.getType());
            Image scaledImage = sourceImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
            img2.createGraphics().drawImage(scaledImage, 0, 0, null);
        }
        ImageIO.write(img2, "png", thumbnail);
    }
    catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return thumbnail;
}


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
        processRequest(request, response);
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
