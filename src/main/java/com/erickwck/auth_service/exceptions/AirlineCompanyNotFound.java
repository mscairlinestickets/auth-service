package com.erickwck.auth_service.exceptions;

public class AirlineCompanyNotFound extends RuntimeException {


   public AirlineCompanyNotFound(String username) {
        super("Airline company " +username+ ": not found.");
    }

}
