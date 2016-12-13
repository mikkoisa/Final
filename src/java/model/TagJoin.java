
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tag_join")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TagJoin.findAll", query = "SELECT t FROM TagJoin t"),
    @NamedQuery(name = "TagJoin.findById", query = "SELECT t FROM TagJoin t WHERE t.id = :id"),
    @NamedQuery(name = "TagJoin.findByPictureid", query = "SELECT t FROM TagJoin t WHERE t.pictureid = :pictureid"),
    @NamedQuery(name = "TagJoin.findByTagid", query = "SELECT t FROM TagJoin t WHERE t.tagid = :tagid")})
public class TagJoin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Picture_id")
    private int pictureid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tag_id")
    private int tagid;

    public TagJoin() {
    }

    public TagJoin(Integer id) {
        this.id = id;
    }

    public TagJoin(Integer id, int pictureid, int tagid) {
        this.id = id;
        this.pictureid = pictureid;
        this.tagid = tagid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPictureid() {
        return pictureid;
    }

    public void setPictureid(int pictureid) {
        this.pictureid = pictureid;
    }

    public int getTagid() {
        return tagid;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
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
        if (!(object instanceof TagJoin)) {
            return false;
        }
        TagJoin other = (TagJoin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TagJoin[ id=" + id + " ]";
    }

}
