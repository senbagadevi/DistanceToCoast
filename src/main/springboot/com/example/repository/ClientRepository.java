package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Client;

@Repository
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

	Boolean existsByPhoneNumber(String phoneNumber);

	Client findByPhoneNumber(String phoneNumber);

	List<Client> findAllByActive(boolean status);

	static String DistanceCalculation = "(6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(c.latitude))))";

	/* The distance calculation */
	@Query()
	public List<Client> findClientWithNearestLocation(@Param("latitude") double latitude,
			@Param("longitude") double longitude, @Param("distance") double distance);

}
