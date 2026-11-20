package anon_questions_back.service;

import anon_questions_back.entity.User;
import anon_questions_back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User findOrCreate(Long vkUserId){
        Optional<User> existingUser=userRepository.findByVkUserId(vkUserId);
        
        if(existingUser.isPresent()){
            return existingUser.get();
        }
        
        User newUser=new User();
        newUser.setVkUserId(vkUserId);
        newUser.setLinkSlug(generateSlug());
        
        return  userRepository.save(newUser);
        
    }

    private String generateSlug() {
        String chars="abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb=new StringBuilder();
        Random random=new Random();
        for(int i=0;i<10; i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
