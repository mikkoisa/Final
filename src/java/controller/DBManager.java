
package controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Comments;
import model.Media;
import model.Rating;

import model.Tag;
import model.Users;

@Stateless
public class DBManager {
    
    @PersistenceContext(name = "projecktiPU")
      EntityManager em;
    //private EntityTransaction t;

    public DBManager() {
                //em = Persistence.createEntityManagerFactory("projecktiPU").createEntityManager();
    }
    
    
    public Users findUserById(int id) {
//        t = em.getTransaction();
//        t.begin();
    	return (Users) em.createNamedQuery("Users.findById").setParameter("id", id).getSingleResult();
        
//        List<Users> list = new ArrayList<>();
//        list = q.getResultList();
////        t.commit();
//        return list.get(0);
    }
      public Users findUserByName(String name) {
//        t = em.getTransaction();
//        t.begin();
    	//Users u = (Users) em.createNamedQuery("Users.findByName").setParameter("id", name).getSingleResult();
Query q = em.createQuery("SELECT u FROM Users u WHERE u.name = '"+name+"'");
        List<Users> list = new ArrayList<>();
        list = q.getResultList();
////        t.commit();
        return list.get(0);
        //return u;
    }
    public List<Media> findNewest(int amount, int from) {
        List<Media> list = new ArrayList<>();
        //t = em.getTransaction();
       // t.begin();
    	Query q = em.createQuery("SELECT m FROM Media m ORDER BY m.time DESC").setMaxResults(amount);
        
        list = q.getResultList();
        //t.commit();
        
         List<Media> newList = list.subList(from, list.size());

        return newList;
    }
    
    public List<Media> findRandom(int amount, int from) {
        List<Media> list = new ArrayList<>();
        Query q = em.createNativeQuery("SELECT * FROM media ORDER BY RAND() LIMIT "+amount+"", Media.class);
        list = (List<Media>) q.getResultList();
        List<Media> newList = list.subList(from, list.size());
        return newList;
    }
    
    public List<Media> findPopular(int amount, int from) {
        List<Integer> list = new ArrayList<>();
        //t = em.getTransaction();
       // t.begin();
    	Query q = em.createQuery("SELECT r.mediaId FROM Rating r GROUP BY r.mediaId ORDER BY COUNT(r.mediaId) DESC").setMaxResults(amount);
        
        list = q.getResultList();
        //t.commit();
        
         List<Integer> newList = list.subList(from, list.size());
         
        List<Media> medias = new ArrayList<>();
         for (Integer l : newList ) {
             Media m = this.findById(l);
             medias.add(m);
             System.out.println(m.getTitle()+" "+this.findMediaRating(l));
         }

        return medias;
        
    }
    
    public List<Comments> findCommentsByMediaId(int id) {
        List<Comments> list = new ArrayList<>();
       //t = em.getTransaction();
        //t.begin();
    	Query q = em.createQuery("SELECT c FROM Comments c WHERE c.mediaId = "+id);
        
        list = q.getResultList();
        //t.commit();
        
        return list;
    }

    
    public int findMediaRating(int mediaid) {
        List<Rating> list = new ArrayList<>();
        //t = em.getTransaction();
        //t.begin();
    	Query q = em.createNativeQuery("SELECT * FROM rating WHERE rating.media_Id = '"+mediaid+"'", Rating.class);
        
        
        list = (List<Rating>) q.getResultList();

        //t.commit();

        if (list.isEmpty()) {
            return 0;
        } else {
            return list.size();
        }


    }
    
    public Media findById(int i) {
        Media m = null;
       // t = em.getTransaction();
       // t.begin();
    	m = em.find(Media.class, i);
        
       // t.commit();
        
        return m;
    }
    
    public List<Media> findByTag(String tag) {
        List<Media> list = new ArrayList<>();
       // t = em.getTransaction();
       // t.begin();
    	Query q = em.createNativeQuery("SELECT * FROM media INNER JOIN tag_join on media.id = tag_join.Picture_id INNER JOIN tag on tag_join.tag_id = tag.id WHERE tag.name = '"+tag+"'", Media.class);
        list = (List<Media>) q.getResultList();
       // t.commit();
        
        return list;
    }
    public List<Media> findByTagId(int id) {
        List<Media> list = new ArrayList<>();
       // t = em.getTransaction();
       // t.begin();
    	Query q = em.createNativeQuery("SELECT * FROM media INNER JOIN tag_join on media.id = tag_join.Picture_id INNER JOIN tag on tag_join.tag_id = tag.id WHERE tag.id = '"+id+"'", Media.class);
        list = (List<Media>) q.getResultList();
       // t.commit();
        
        return list;
    }
    
    public void submitComment(int user, int media, String msg) {

        //Comments comm = new Comments();
        //comm.setUserid(user); comm.setMediaId(media); comm.setComment(msg);
        //comm.setTime(new Timestamp(System.currentTimeMillis()));

        //System.out.println(comm.getComment() + "  "+ comm.getMediaId());
      /* t = em.getTransaction();
        t.begin();
        em.persist(comm);
        t.commit();
        return comm;
        */
       // t = em.getTransaction();
      //  t.begin();
        em.createNativeQuery("INSERT INTO comments (Comment, User_id, media_id) VALUES ('"+msg+"','"+user+"','"+media+"')").executeUpdate();
       // t.commit();
    }
    
    public int addMedia(Media m) {
        em.persist(m);em.flush();
        return m.getId();
        //return m.getId();
        
        //em.createNativeQuery("INSERT INTO media (URL, Title, User, description) VALUES ('"+url+"','"+title+"','"+userid+"','"+description+"'").executeUpdate();
    }
    
    public void addTagToMedia(String tagi, int mediaid) {
        
        List<Tag> t = new ArrayList<>();
        Query q = em.createQuery("SELECT t FROM Tag t WHERE t.name= '"+tagi+"'");
        t = q.getResultList();
        
        Tag newtag;
        if (t.isEmpty()) {
            newtag = new Tag(tagi);
            em.persist(newtag); em.flush();
            em.createNativeQuery("INSERT INTO tag_join (Picture_id, Tag_id) VALUES ('"+mediaid+"','"+newtag.getId()+"')").executeUpdate();
        } else {
            Tag oldtag = t.get(0);
            em.createNativeQuery("INSERT INTO tag_join (Picture_id, Tag_id) VALUES ('"+mediaid+"','"+oldtag.getId()+"')").executeUpdate();
        }
        
        
        
    }
    
    public boolean upvote(int userid, int mediaid) {
        List<Rating> r = new ArrayList<>();
        Query q = em.createQuery("SELECT r FROM Rating r WHERE (r.mediaId = '"+mediaid+"' AND r.userId = '"+userid+"')");
        r = q.getResultList();
        
        if (r.isEmpty()) {
            em.createNativeQuery("INSERT INTO rating (media_id, user_id, rating) VALUES ('"+mediaid+"','"+userid+"','"+1+"')").executeUpdate();
            return true;
        } else {
            return false;
        }
    }

    /*
    public Users getUser(String username) {
        Query id = em.createQuery("SELECT u.id FROM Users u WHERE u.name = '"+username+"'");
    }
        */
    public int numberOfMedia() {
       // t = em.getTransaction();
       // t.begin();
        Query q = em.createQuery ("SELECT count(m) FROM Media m");
        Number number = (Number) q.getSingleResult();
       // t.commit();
        
        return number.intValue();
    }

    public List<Tag> getMediaTags(int mediaid) {
        List<Tag> list = new ArrayList<>();
       // t = em.getTransaction();
       // t.begin();
    	Query q = em.createNativeQuery("SELECT * FROM tag INNER JOIN tag_join on tag.id = tag_join.tag_id WHERE Picture_id = "+mediaid, Tag.class);
        list = (List<Tag>) q.getResultList();
       // t.commit();
        
        return list;
    }
    
    public List<Tag> getTags() {
        List<Tag> list = new ArrayList<>();
       // t = em.getTransaction();
       // t.begin();
    	Query q = em.createQuery("SELECT t FROM Tag t ORDER BY t.name ASC");
        list = q.getResultList();
        //t.commit();
        
        return list;
    }
    
    
    public boolean isUserFound(String name) {
         List<Tag> list = new ArrayList<>();
       // t = em.getTransaction();
       // t.begin();
    	Query q = em.createQuery("SELECT u FROM Users u WHERE u.name = '"+name+"'");
        list = q.getResultList();
       // t.commit();
        
        return list.isEmpty();
    }
    
    public Users createUser(String name, String password) {
        
        
        Users u = new Users();
        u.setName(name);
        u.setPassword(password);
        u.setLevel(0);
        
       // t = em.getTransaction();
       // t.begin();
        em.persist(u);
        //t.commit();
        return u;
        
    }
}
