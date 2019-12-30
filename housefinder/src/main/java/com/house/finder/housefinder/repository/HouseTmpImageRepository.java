package com.house.finder.housefinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.HouseTmp;
import com.house.finder.housefinder.bean.HouseTmpImage;

@Repository
public interface HouseTmpImageRepository extends JpaRepository<HouseTmpImage, Integer> {

	public List<HouseTmpImage> findByUrl(String url);

	public List<HouseTmpImage> findByUrlAndHouseTmp(String url, HouseTmp houseTmp);

}
