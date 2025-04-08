package org.application.ecomappbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name field must have at least 5 characters")
    private String streetName;

    @NotBlank
    @Size(min = 5, message = "Building name field must have at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 3, message = "City field must have at least 3 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State field must have at least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country field must have at least 2 characters")
    private String country;

    @Size(min = 4, message = "Zipcode field must have at least 4 characters")
    private String zipcode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();
}
