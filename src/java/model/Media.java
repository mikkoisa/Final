
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "media")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Media.findAll", query = "SELECT m FROM Media m"),
    @NamedQuery(name = "Media.findById", query = "SELECT m FROM Media m WHERE m.id = :id"),
    @NamedQuery(name = "Media.findByUrl", query = "SELECT m FROM Media m WHERE m.url = :url"),
    @NamedQuery(name = "Media.findByTitle", query = "SELECT m FROM Media m WHERE m.title = :title"),
    @NamedQuery(name = "Media.findByPoints", query = "SELECT m FROM Media m WHERE m.points = :points"),
    @NamedQuery(name = "Media.findByUser", query = "SELECT m FROM Media m WHERE m.user = :user"),
    @NamedQuery(name = "Media.findByTime", query = "SELECT m FROM Media m WHERE m.time = :time"),
    @NamedQuery(name = "Media.findByDescription", query = "SELECT m FROM Media m WHERE m.description = :description")})
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "URL")
    private String url;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Points")
    private int points;
    @Column(name = "User")
    private Integer user;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Size(max = 512)
    @Column(name = "description")
    private String description;
    @Size(max = 512)
    @Column(name = "tURL")
    private String turl;

    public Media() {
    }

    public Media(Integer id) {
        this.id = id;
    }

    public Media(Integer id, String url, String title, int points, Date time) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.points = points;
        this.time = time;
    }
        public Media(String url, String title, int user, String description) {
        this.url = url;
        this.title = title;
        this.user = user;
        this.time = new Date();
        this.points = 0;
        this.description = description;
    }
                public Media(String url, String title, int user, String description, String turl) {
        this.url = url;
        this.title = title;
        this.user = user;
        this.time = new Date();
        this.points = 0;
        this.description = description;
        this.turl = turl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Media)) {
            return false;
        }
        Media other = (Media) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Media[ id=" + id + " ]";
    }

}
