package com.dalhousie.Neighbourly.user.resident.model;


import com.dalhousie.Neighbourly.neighbourhood.model.Neighbourhood;
import jakarta.persistence.*;

@Entity
@Table(name = "resident")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String contact;
    private String address;

    @ManyToOne
    @JoinColumn(name = "neighbourhood_id", nullable = false)
    private Neighbourhood neighbourhood;

    // Constructors
    public Resident() {}

    public Resident(String name, String contact, String address, Neighbourhood neighbourhood) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.neighbourhood = neighbourhood;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Neighbourhood getNeighbourhood() { return neighbourhood; }
    public void setNeighbourhood(Neighbourhood neighbourhood) { this.neighbourhood = neighbourhood; }
}
