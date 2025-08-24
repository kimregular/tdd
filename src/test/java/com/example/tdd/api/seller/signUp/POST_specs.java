package com.example.tdd.api.seller.signUp;

import com.example.tdd.*;
import com.example.tdd.command.CreateSellerCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.tdd.EmailGenerator.generateEmail;
import static com.example.tdd.PasswordGenerator.generatePassword;
import static com.example.tdd.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 테스트 클래스가 Spring Boot 기반의 테스트 메서드를 실행하도록 지정
 * classes 요소에 Spring Boot 구성 클래스 형식을 지정
 * 테스트 대상 응용프로그램이 웹 서버에서 구동되도록 webEnvironment 요소를 지정
 */
@SpringBootTest(
        classes = TddApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/signUp")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // arrange
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                "password");

        // act
        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );
        // 1. api 경로
        // 2. 요청 본문 데이터
        // 3. 응답 본문 데이터의 자바 형식

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        var command = new CreateSellerCommand(
                null,
                generateUsername(),
                "password");

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "invalid-email@",
            "invalid-email@test",
            "invalid-email@test.",
            "invalid-email@.com"
    })
    void email_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
            String email,
            @Autowired TestRestTemplate client
    ) {
        var command = new CreateSellerCommand(
                email,
                generateUsername(),
                "password");

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        var command = new CreateSellerCommand(
                generateEmail(),
                null,
                "password");

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "se",
            "seller ",
            "seller.",
            "seller!",
            "seller@"
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
            String username,
            @Autowired TestRestTemplate client
    ) {

        var command = new CreateSellerCommand(
                generateEmail(),
                username,
                "password"
        );

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "seller",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789",
            "seller_",
            "seller-",
    })
    void username_속성이_올바른_형식을_따르면_204_No_Content_상태코드를_반환한다(
            String username,
            @Autowired TestRestTemplate client
    ) {

        var command = new CreateSellerCommand(
                generateEmail(),
                username,
                "password"
        );

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void password_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                null
        );

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "pass",
            "pass123"
    })
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
            String password,
            @Autowired TestRestTemplate client
    ) {
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                password
        );

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
    
    @Test
    void email_속성에_이미_존재하는_이메일_주소가_지정되면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        String email = generateEmail();
        client.postForEntity(
                "/seller/signUp",
                new CreateSellerCommand(email, generateUsername(), "password"),
                Void.class
        );

        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                new CreateSellerCommand(email, generateUsername(), "password"),
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
    
    @Test
    void username_속성에_이미_존재하는_사용자_이름이_지정되면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        String username = generateUsername();
        client.postForEntity(
                "/seller/signUp",
                new CreateSellerCommand(generateEmail(), username, "password"),
                Void.class
        );
        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                new CreateSellerCommand(generateEmail(), username, "password"),
                Void.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화한다(
            @Autowired TestRestTemplate client,
            @Autowired SellerRepository sellerRepository,
            @Autowired PasswordEncoder passwordEncoder
    ) {
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
        );

        client.postForEntity(
                "/seller/signUp",
                command,
                Void.class
        );

        Seller seller = sellerRepository
                .findAll()
                .stream()
                .filter(s -> s.getEmail().equals(command.email()))
                .findFirst()
                .orElseThrow();

        String actual = seller.getHashedPassword();
        assertThat(actual).isNotNull();
        assertThat(passwordEncoder.matches(command.password(), actual)).isTrue();
    }
}
