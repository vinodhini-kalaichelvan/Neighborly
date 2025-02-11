package com.dalhousie.Neighbourly.neighbourhood.entity;

import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Entity
@Data
@Table(name = "neighbourhood")
public class Neighbourhood {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neighbourhood_id")
    private int neighbourhoodId;


    @Column(nullable = false)
    private String name;

    private String location;

    @OneToMany(mappedBy = "neighbourhood", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HelpRequest> helpRequests;

}
