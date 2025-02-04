package com.dalhousie.Neighbourly.user.communitymanager.model;


import com.dalhousie.Neighbourly.neighbourhood.model.Neighbourhood;
import jakarta.persistence.*;

@Entity
@Table(name = "community_manager")
public class CommunityManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String contact;

    @ManyToOne
    @JoinColumn(name = "neighbourhood_id", nullable = false)
    private Neighbourhood neighbourhood;

    // Constructors
    public CommunityManager() {}

    public CommunityManager(String name, String contact, Neighbourhood neighbourhood) {
        this.name = name;
        this.contact = contact;
        this.neighbourhood = neighbourhood;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Neighbourhood getNeighbourhood() { return neighbourhood; }
    public void setNeighbourhood(Neighbourhood neighbourhood) { this.neighbourhood = neighbourhood; }
}
