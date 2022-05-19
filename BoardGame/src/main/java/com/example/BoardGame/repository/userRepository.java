
package com.example.BoardGame.repository;

import com.example.BoardGame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface userRepository extends JpaRepository <User,Long> {//<Entitiy, id유형>

}
