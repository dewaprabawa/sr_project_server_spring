package learn_rest.example.tell_me_this_will_work.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import learn_rest.example.tell_me_this_will_work.auth.model.User;


public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private UserData userData;

    public UserDetailsImpl(UserData userData){
        this.userData = userData;
    }

    public static UserDetailsImpl build(User user){

        List<GrantedAuthority> authorities = user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        var userData = new UserData(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getProfileImageUrl(),
                user.getCountryCode(),
                user.getPhoneNumber()
        );
        return new UserDetailsImpl(
                userData
        );
    }


    /**
     *
     * @getter USER MODEL
     */

    public String getProfileImageUrl(){
        return userData.getProfileImageUrl();
    }

    public String getId() {
        return userData.getId();
    }

    public String getEmail() {
        return userData.getEmail();
    }

    public String getPhoneNumber(){
        return userData.getPhoneNumber();
    }

    public String getCountryCode(){
        return userData.getCountryCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userData.getAuthorities();
    }

    @Override
    public String getPassword() {
        return userData.getPassword();
    }

    @Override
    public String getUsername() {
        return userData.getUsername();
    }




    /**
     *
     * @security
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl)) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(userData, that.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userData);
    }
}



 class UserData {


    private String id;
    private String username;
    private String email;
    private String profileImageUrl;
    private Collection<? extends GrantedAuthority> authorities;
    private String phoneNumber;
    private String countryCode;

    @JsonIgnore
    private String password;

    UserData(String id,String username,String email,String password ,Collection<? extends GrantedAuthority> authorities, String profileImageUrl, String countryCode, String phoneNumber){
        this.id = id;
        this.email = email;
        this.username = username;
        this.authorities = authorities;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
    };

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}
