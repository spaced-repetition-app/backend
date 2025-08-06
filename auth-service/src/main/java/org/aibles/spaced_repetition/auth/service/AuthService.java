package org.aibles.spaced_repetition.auth.service;

import lombok.RequiredArgsConstructor;
import org.aibles.spaced_repetition.auth.entity.Account;
import org.aibles.spaced_repetition.auth.entity.User;
import org.aibles.spaced_repetition.auth.repository.AccountRepository;
import org.aibles.spaced_repetition.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

}