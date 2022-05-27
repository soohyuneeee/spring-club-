package com.soohyun.springSecurity.repository;

import com.soohyun.springSecurity.model.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

}