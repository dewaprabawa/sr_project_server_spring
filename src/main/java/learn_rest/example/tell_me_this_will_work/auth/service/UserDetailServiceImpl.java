package learn_rest.example.tell_me_this_will_work.auth.service;

import learn_rest.example.tell_me_this_will_work.auth.repository.UserRepository;
import learn_rest.example.tell_me_this_will_work.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return  UserDetailsImpl.build(user);
    }
}
