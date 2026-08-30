package com.college.democollege.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String state;
    private String city;
    private double latitude;
    private double longitude;
    private String nearByAddress;

}
