package com.dalhousie.Neighbourly.neighbourhood.model;



import com.dalhousie.Neighbourly.user.admin.model.Admin;
import com.dalhousie.Neighbourly.user.communitymanager.model.CommunityManager;
import com.dalhousie.Neighbourly.user.resident.model.Resident;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "neighbourhood")
public class Neighbourhood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String location;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "neighbourhood", cascade = CascadeType.ALL)
    private List<CommunityManager> communityManagers;

    @OneToMany(mappedBy = "neighbourhood", cascade = CascadeType.ALL)
    private List<Resident> residents;

    // Constructors
    public Neighbourhood() {}

    public Neighbourhood(String name, String location, Admin admin) {
        this.name = name;
        this.location = location;
        this.admin = admin;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public List<CommunityManager> getCommunityManagers() { return communityManagers; }
    public void setCommunityManagers(List<CommunityManager> communityManagers) { this.communityManagers = communityManagers; }

    public List<Resident> getResidents() { return residents; }
    public void setResidents(List<Resident> residents) { this.residents = residents; }
}
