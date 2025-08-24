## Arrange-Act-Assert 패턴
- 테스트 코드를 순차적인 세 단계로 구분하는 기법
- Arrange 단계는 테스트에 필요한 데이터를 생성하고 시스템의 사전 조건을 준비
- Act 단계는 테스트 대상 기능을 실행
- Assert 단계는 실행 결과가 기대와 일치하는지 확인
- 테스트의 준비, 실행, 검증 단계가 명확히 구분되어 테스트 작성 시 각 단계에 집중할 수 있고 테스트 가독성을 높인다
- 각 단계를 작성하는 순서는 다양하다

## `@SpringBootTest`
- 테스트 클래스가 Spring Boot 기반의 테스트 메서드를 실행하도록 지정
- classes 요소에 Spring Boot 구성 클래스 형식을 지정
- 테스트 대상 응용프로그램이 웹 서버에서 구동되도록 webEnvironment 요소를 지정

## `postForEntity(p1, p2, p3)`
- p1 : api 경로 (`/seller/signUp`)
- p2 : 요청 본문 데이터
- p3 : 응답 본문 데이터의 자바 타입


## API 목록
### 판매자 회원 가입
요청
- 메서드: POST
- 경로: /seller/signUp
- 헤더
```
Content-Type: application/json
```
- 본문
```
CreateSellerCommand {
    email: string,
    username: string,
    password: string
}
```
- curl 명령 예시
```shell
curl -i -X POST 'http://localhost:8080/seller/signUp' \
-H 'Content-Type: application/json' \
-d '{
  "email": "seller1@example.com",
  "username": "seller1",
  "password": "seller1-password"
}'
```

`-i` : 응답 헤더 출력
`-X` : http 메서드 지정
`-H` : 요청 헤더 지정
`-d` : 요청 본문 데이터 지정

성공
- 상태코드: 204 No Content

정책
- 이메일 주소는 유일해야 한다
- 사용자이름은 유일해야 한다
- 사용자 이름은 3자 이상의 영문자, 숫자, 하이픈, 밑줄 문자로 구성되어야 한다
- 비밀번호는 8자 이상의 문자로 구성되어야 한다

테스트
- [x] 올바르게 요청하면 204 No Content 상태코드를 반환한다
- [x] email 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다
- [x] email 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다
- [x] username 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다
- [x] username 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다
- [x] username 속성이 올바른 형식을 따르면 204 No Content 상태코드를 반환한다
- [x] password 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다
- [x] password 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다
- [x] email 속성에 이미 존재하는 이메일 주소가 지정되면 400 Bad Request 상태코드를 반환한다
- [x] username 속성에 이미 존재하는 사용자 이름이 지정되면 400 Bad Request 상태코드를 반환한다
- [x] 비밀번호를 올바르게 암호화한다