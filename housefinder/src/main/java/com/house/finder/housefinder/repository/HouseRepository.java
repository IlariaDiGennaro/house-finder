package com.house.finder.housefinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.House;
import com.house.finder.housefinder.bean.HouseStatus;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {

	public List<House> findByHouseStatusAndAdId(HouseStatus houseStatus, String adId);
	
	public House findFirstByHouseStatusAndAdId(HouseStatus houseStatus, String adId);
	
//	@Query("SELECT adId FROM House h WHERE h.houseStatus = :houseStatus")
//	public List<String> findAdIdsByHouseStatus(@Param("houseStatus") HouseStatus houseStatus);

	public List<House> findByHouseStatus(HouseStatus houseStatus);
	
	@Query("SELECT h FROM House h WHERE h.houseStatus != :houseStatus")
	public List<House> findByHouseStatusNotDeletd(@Param("houseStatus") HouseStatus houseStatus);

	public House findByAdId(String adId);
	
	public List<House> findByAdIdNotIn(List<String> adIds);
	
}
