package com.projects.client.services;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.client.entities.Client;
import com.projects.client.repositories.ClientRepository;
import com.projects.client.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<Client> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list;
	}

	@Transactional(readOnly = true)
	public Client findById(Long id) {
		Optional<Client> obj = repository.findById(id);

		return obj.orElseThrow(() -> new NoSuchElementException("Client not found with ID: " + id));
	}

	@Transactional
	public Client insert(Client obj) {
		// return repository.save(obj);
		Client entity = new Client();
		copyClientData(entity, obj);
		entity = repository.save(entity);
		return entity;

	}

	@Transactional
	public Client update(Long id, Client obj) {
		try {
			Client entity = repository.getReferenceById(id);
			copyClientData(entity, obj);
			// entity = repository.save(entity);
			// return entity;
			return  repository.save(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

	}

	private void copyClientData(Client entity, Client obj) {
		// Copie os valores do objeto obj para o objeto entity
		entity.setName(obj.getName());
		entity.setCpf(obj.getCpf());
		entity.setIncome(obj.getIncome());
		entity.setBirthDate(obj.getBirthDate());
		entity.setChildren(obj.getChildren());
	}
}
