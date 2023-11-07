package com.projects.client.services;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.client.dto.ClientDTO;
import com.projects.client.entities.Client;
import com.projects.client.repositories.ClientRepository;
import com.projects.client.services.exceptions.DataBaseException;
import com.projects.client.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));

		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO obj) {
		Client entity = new Client();
		copyDtoToClientEntity(entity, obj);
		entity = repository.save(entity);
		return new ClientDTO(entity);

	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO obj) {
		try {
			Client entity = repository.getReferenceById(id);
			copyDtoToClientEntity(entity, obj);
			entity = repository.save(entity);
			
			return new ClientDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}

	}

	private void copyDtoToClientEntity(Client entity, ClientDTO obj) {
		// Copie os valores do objetoDTO  'obj' para o objeto entity
		entity.setName(obj.getName());
		entity.setCpf(obj.getCpf());
		entity.setIncome(obj.getIncome());
		entity.setBirthDate(obj.getBirthDate());
		entity.setChildren(obj.getChildren());
	}

}
