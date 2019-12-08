package com.house.finder.housefinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.House;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {

	public List<House> findByHouseStatus(String houseStatus);
	
	@Query("SELECT adId FROM House h WHERE h.houseStatus = :houseStatus")
	public List<String> findAdIdsByHouseStatus(String houseStatus);
}
