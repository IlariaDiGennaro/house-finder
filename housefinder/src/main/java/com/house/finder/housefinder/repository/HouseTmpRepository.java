package com.house.finder.housefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.HouseTmp;

@Repository
public interface HouseTmpRepository extends JpaRepository<HouseTmp, Integer> {

	HouseTmp findByAdId(String adId);

	HouseTmp findFirstByAdId(String adId);

}
