package com.pms.booking.service;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public abstract class GenericService<T, ID, D> {

    protected final JpaRepository<T, ID> repository;

    protected GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    protected abstract T convertToModel(D dto, ID id);

    // Salva um novo item a partir do DTO
    @Transactional
    public T save(D dto) throws Exception {
        T model = convertToModel(dto, null);
        return repository.save(model);
    }

    // Obtém todos os itens
    public List<T> getAll() {
        return repository.findAll();
    }

    // Obtém um item pelo ID
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    // Exclui um item pelo ID
    @Transactional
    public boolean delete(ID id) throws RuntimeException{
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw  new RuntimeException("Item não encontrado");
    }

    // Atualiza um item pelo ID, a partir do DTO
    @Transactional
    public T update(ID id, D dto) throws RuntimeException {
        if (repository.existsById(id)) {
            T model = convertToModel(dto, id);

            return repository.save(model);
        }
        throw new RuntimeException("Item não encontrado");
    }
}
