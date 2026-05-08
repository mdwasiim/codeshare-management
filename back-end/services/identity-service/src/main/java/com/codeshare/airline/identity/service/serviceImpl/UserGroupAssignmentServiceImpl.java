package com.codeshare.airline.identity.service.serviceImpl;

import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.UserGroup;
import com.codeshare.airline.identity.repository.UserGroupRepository;
import com.codeshare.airline.identity.service.UserGroupAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupAssignmentServiceImpl
        implements UserGroupAssignmentService {

    private final UserGroupRepository
            userGroupRepository;

    @Override
    public Set<String> resolveGroupCodes(
            UUID userId
    ) {

        return userGroupRepository
                .findByUser_Id(userId)
                .stream()

                .map(UserGroup::getGroup)

                .filter(Objects::nonNull)

                .map(Group::getCode)

                .collect(Collectors.toSet());
    }
}