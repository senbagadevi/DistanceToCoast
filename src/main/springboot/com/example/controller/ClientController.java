package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.ClientModel;
import com.example.response.base.BaseResponse;
import com.example.service.ClientService;

@RestController
@RequestMapping(value = "/api/v1")
public class ClientController {

	@Autowired
	private ClientService clientService;

	// only user logged in to the system can retrieve all clients
	@PreAuthorize("isRole('ROLE_USER')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public BaseResponse getAllClients() {
		return clientService.getAll();
	}

	// only logged in user can get nearest user
	@PreAuthorize("isRole('ROLE_USER')")
	@RequestMapping(value = "/nearestUser", method = RequestMethod.GET)
	public BaseResponse getNearestClient(@RequestParam(value = "distance") double distance,
			@RequestParam(value = "longitude") double longitude, @RequestParam(value = "latitude") double latitude,
			@RequestParam(value = "state") string state,HttpServletRequest request) {
		return clientService.findAllNearestClientWithIndistance(latitude ,longitude ,state,distance);
	}
}
