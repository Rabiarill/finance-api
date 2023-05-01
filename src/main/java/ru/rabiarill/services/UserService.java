package ru.rabiarill.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rabiarill.exception.model.user.UserNotFoundException;
import ru.rabiarill.models.user.User;
import ru.rabiarill.repositories.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserService {
   private final UserRepository userRepository;

   @Autowired
   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public List<User> findAll() {
      return userRepository.findAll();
   }

   public User findOne(int id) throws UserNotFoundException {
      return userRepository
              .findById(id)
              .orElseThrow(() -> new UserNotFoundException("User with id = " + id + " not found"));
   }

   public Optional<User> findByUsername(String username) {
      return userRepository.findByUsername(username);
   }

   @Transactional
   public void save(User user) {
      userRepository.save(user);
   }

   @Transactional
   public void delete(int id) {
      userRepository.deleteById(id);
   }

   @Transactional
   public void deleteAll(){
      userRepository.deleteAll();
   }

}
