package br.com.stormaid.repository;

import br.com.stormaid.entity.Dependent;
import br.com.stormaid.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Long> {
    Page<Dependent> findByGuardian(User guardian, Pageable pageable);
}