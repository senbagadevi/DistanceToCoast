package com.example.service.impl;

import static com.example.util.CommonsValidator.notEmpty;
import static com.example.util.CommonsValidator.validateEntity;
import static com.example.util.CommonsValidator.validateName;
import static com.example.util.CommonsValidator.validateNumber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.constant.Constants;
import com.example.entity.Client;
import com.example.exception.AuthorizationException;
import com.example.exception.BadRequestException;
import com.example.model.ClientModel;
import com.example.repository.ClientRepository;
import com.example.response.base.BaseResponse;
import com.example.response.base.ListResponse;
import com.example.response.base.SingleResponse;
import com.example.service.ClientService;
import com.example.util.CommonUtils;
import com.example.util.Distance;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepo;

	@Override
	public BaseResponse createClient(ClientModel clientMdl) {
		validateName(clientMdl.getState());
		validateNumber(clientMdl.getLatitude(), "latitude");
		validateNumber(clientMdl.getLongitude(), "longitude");
		
		try {
			Client client = new Client();
			client.setClientName(clientMdl.getName());
			client.setLatitude(clientMdl.getLatitude());
			client.setLongitude(clientMdl.getLongitude());
			client.setPhoneNumber(clientMdl.getPhoneNumber());
			clientRepo.save(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseResponse.created("client created succesfully");
	}

	@Override
	public BaseResponse updateClient(long id, ClientModel clientMdl, HttpServletRequest request) {
		Client client = clientRepo.findOne(id);
		validateEntity(client, "client");
		long idFromToken = CommonUtils.getIdFromJWT(request);
		// to ensure the this update is done by the record owner
//		if (idFromToken != client.getId()) {
//			throw new AuthorizationException("Sorry you are not authorized to perform this action");
//		}

		if (notEmpty(clientMdl.getName())) {
			validateName(clientMdl.getName());
			client.setClientName(clientMdl.getName());
		}
		if (notEmpty(clientMdl.getLongitude())) {
			validateNumber(clientMdl.getLongitude(), "longitude");
			client.setLongitude(clientMdl.getLongitude());
		}
		if (notEmpty(clientMdl.getLatitude())) {
			validateNumber(clientMdl.getLatitude(), "latitude");
			client.setLatitude(clientMdl.getLatitude());
		}
		
		clientRepo.save(client);
		return SingleResponse.updated(client);
	}


	@Override
	public BaseResponse getAll() {

		List<Client> clients = clientRepo.findAllByActive(true);

		if (clients.size() == 0)
			return BaseResponse.noContent();

		return ListResponse.found(clients);
	}

	@Override
	public BaseResponse getById(long id) {
		Client client = clientRepo.findOne(id);
		validateEntity(client, "client");
		return SingleResponse.found(client);
	}

	@Override
	public BaseResponse findAllNearestClientWithIndistance(double latitude, double longtitude,string state, double distance) {
		longtitude = checkLng(longtitude);
		latitude = checkLat(latitude);
		
		t<Client> clients = clientRepo.findClientWithNearestLocation(latitude, longtitude, distance);
		
		if (clients.size() < 0)
			return BaseResponse.noContent();

		// distance between the user and the client location
		// list which will be returned as the final result

		List<Client> allActiveClients = clientRepo.findAllByActive(true);
		List<Client> nearestClients = new ArrayList<>();
		for (Client client : allActiveClients) {
			Double nearestDistance = Distance.distance(latitude, longtitude, client.getLatitude(),
					client.getLongitude());
			if (distance >= nearestDistance) {
				nearestClients.add(client);
			}
		}

		if (nearestClients.isEmpty())
			return BaseResponse.noContent();

		return ListResponse.found(clients);
	}

	// if user send 0 long and 0 lat the default will ab amman down town
	private double checkLng(double longtitude) {
		if (longtitude == 0.0)
			longtitude = Double.parseDouble(Constants.LONG);
		return longtitude;
	}

	private double checkLat(double latitude) {
		if (latitude == 0.0)
			latitude = Double.parseDouble(Constants.LATT);
		return latitude;
	}

}
