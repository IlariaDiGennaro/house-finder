package com.house.finder.housefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.ZCasa;

@Repository
public interface ZCasaRepository extends JpaRepository<ZCasa, Integer> {

    ZCasa findByIdAnnuncio(String idAnnuncio);
}
