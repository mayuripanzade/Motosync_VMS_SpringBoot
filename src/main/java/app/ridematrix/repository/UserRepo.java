package app.ridematrix.repository;

import app.ridematrix.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<CustomUser,Integer>
{
        Optional<CustomUser> findByUsername(String username);
}