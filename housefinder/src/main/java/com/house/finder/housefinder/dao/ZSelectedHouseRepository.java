package com.house.finder.housefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.ZSelectedHouse;

@Repository
public interface ZSelectedHouseRepository extends JpaRepository<ZSelectedHouse, Integer> {

	//SelectedHouse findByIdAnnuncio(String idAnnuncio);
	
}
