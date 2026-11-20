package anon_questions_back.controller;

import anon_questions_back.dto.AuthResponse;
import anon_questions_back.dto.VkAuthRequest;
import anon_questions_back.entity.User;
import anon_questions_back.security.VkLaunchParamsValidator;
import anon_questions_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final VkLaunchParamsValidator validator;
    private final UserService userService;

    @PostMapping("/vk")
    public ResponseEntity<?> login(@RequestBody VkAuthRequest request) {
        try {
            Long vkUserId = validator.validateAndExtractUserId((request.launchParams()));

            User user = userService.findOrCreate(vkUserId);

            return ResponseEntity.ok(new AuthResponse(
                    user.getId(),
                    user.getVkUserId(),
                    user.getLinkSlug()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

}
