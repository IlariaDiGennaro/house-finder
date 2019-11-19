package com.house.finder.housefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.Casa;

@Repository
public interface CasaRepository extends JpaRepository<Casa, Integer>{

}
