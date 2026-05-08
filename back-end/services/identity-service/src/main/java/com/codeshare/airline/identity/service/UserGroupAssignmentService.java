package com.codeshare.airline.identity.service;

import java.util.Set;
import java.util.UUID;

public interface UserGroupAssignmentService {

    Set<String> resolveGroupCodes(UUID userId);
}