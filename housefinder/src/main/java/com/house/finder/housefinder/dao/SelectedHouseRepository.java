package com.house.finder.housefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.SelectedHouse;

@Deprecated
@Repository
public interface SelectedHouseRepository extends JpaRepository<SelectedHouse, Integer> {

	SelectedHouse findByIdAnnuncio(String idAnnuncio);
	
}
