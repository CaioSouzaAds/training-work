package com.projects.client.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.projects.client.entities.Client;
import com.projects.client.repositories.ClientRepository;

@Service
public class ClientService implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private ClientRepository repository;

	public Page<Client> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return  list;
	}

}
