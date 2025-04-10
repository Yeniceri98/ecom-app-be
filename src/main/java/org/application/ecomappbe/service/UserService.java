package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.RegisterRequest;
import org.application.ecomappbe.model.User;

public interface UserService {
    User register(RegisterRequest registerRequest);
}
