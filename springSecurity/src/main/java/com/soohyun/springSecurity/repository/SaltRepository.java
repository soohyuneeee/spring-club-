package com.soohyun.springSecurity.repository;

import com.soohyun.springSecurity.model.Salt;
import org.springframework.data.repository.CrudRepository;

public interface SaltRepository extends CrudRepository<Salt,Long> {

}