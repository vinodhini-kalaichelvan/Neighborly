package com.dalhousie.Neighbourly.parking.entity;

import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "parking_space")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_space_id") 
    private int parkingSpaceId; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // foreign key for user
    private User user;

    @ManyToOne
    @JoinColumn(name = "assign_to_user_id", nullable = true) // foreign key for user
    private User assignToUser;


    @ManyToOne
    @JoinColumn(name = "neighbourhood_id", nullable = false) // foreign key for neighbourhood
    private Neighbourhood neighbourhood;

    @Column(name = "parking_space_name", nullable = false)
    private String parkingSpaceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "parking_type", nullable = false) // Parking type as Enum
    private ParkingType parkingType;

    @Column(name = "price", nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false) // Price type as Enum
    private PriceType priceType;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Enumerated(EnumType.STRING)
    @Column(name = "parking_feature", nullable = false) 
    private ParkingFeature parkingFeature;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_at")
    private Date createdAt;
}
