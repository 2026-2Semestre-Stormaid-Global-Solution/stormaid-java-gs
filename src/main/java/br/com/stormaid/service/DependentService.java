package br.com.stormaid.service;

import br.com.stormaid.entity.Dependent;
import br.com.stormaid.entity.User;
import br.com.stormaid.repository.DependentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DependentService {

    private final DependentRepository dependentRepository;

    public DependentService(DependentRepository dependentRepository) {
        this.dependentRepository = dependentRepository;
    }

    @Transactional
    public Dependent createDependent(User guardian, Dependent dependentData) {
        dependentData.setGuardian(guardian);
        dependentData.setActive(true);
        return dependentRepository.save(dependentData);
    }

    public Page<Dependent> listByGuardian(User guardian, Pageable pageable) {
        return dependentRepository.findByGuardian(guardian, pageable);
    }

    public Dependent findByIdAndGuardian(Long id, User guardian) {
        return dependentRepository.findById(id)
                .filter(dep -> dep.getGuardian().getId().equals(guardian.getId()))
                .orElseThrow(() -> new RuntimeException("Dependente não encontrado ou não autorizado"));
    }

    @Transactional
    public Dependent updateDependent(Long id, User guardian, Dependent updatedData) {
        Dependent dependent = findByIdAndGuardian(id, guardian);

        dependent.setFullName(updatedData.getFullName());
        dependent.setRelationship(updatedData.getRelationship());
        dependent.setBirthDate(updatedData.getBirthDate());

        return dependentRepository.save(dependent);
    }

    @Transactional
    public void deleteDependent(Long id, User guardian) {
        Dependent dependent = findByIdAndGuardian(id, guardian);
        dependentRepository.delete(dependent);
    }
}