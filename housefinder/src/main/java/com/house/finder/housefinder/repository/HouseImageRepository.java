package com.house.finder.housefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.HouseImage;

@Repository
public interface HouseImageRepository extends JpaRepository<HouseImage, Integer> {

	public HouseImage findByUrl(String url);
}
