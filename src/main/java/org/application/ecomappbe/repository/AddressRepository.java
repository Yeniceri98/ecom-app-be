package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.Address;
import org.application.ecomappbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByStreetNameAndBuildingName(String streetName, String buildingName);
    List<Address> findByUser(User currentUser);
}
