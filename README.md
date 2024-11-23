# 📔 일기 뉴스피드
- 오늘 하루를 마무리하며 회고하자는 의미로 일기를 작성
- 로그인을 통해 인증된 회원만 일기 CRUD 가능
- 팔로우를 통해 팔로우한 사용자들의 일기를 조회하고 댓글 및 좋아요를 남길 수 있음

# ⭐ 기능
- 프로필 관리
  - 프로필 수정
  - 프로필 단건 조회
- 뉴스피드 게시물 관리
  - 작성
  - 수정
  - 삭제
  - 단건 조회
  - 전체 조회
  - 팔로우한 사용자의 게시물 전체 조회
  - 10개씩 페이지네이션
- 사용자 인증
  - 회원가입
  - 회원탈퇴
  - 이미 탈퇴한 회원의 이메일 사용 불가
- 친구 관리
  - 팔로우
  - 팔로우 취소
  - 특정 사용자의 팔로우 전체 조회
  - 특정 사용자의 팔로워 전체 조회
- 조건별 정렬
  - 생성일자 순
  - 수정일자 기준 최신순
  - 좋아요 많은 순
- 기간별 게시물 검색
- 댓글
  - 작성
  - 수정
  - 삭제
  - 전체 조회
- 좋아요
  - 게시글 좋아요
  - 게시글 좋아요 취소
  - 댓글 좋아요
  - 댓글 좋아요 취소
 
# 🚨 주의사항
- 로그인한 사용자는 본인의 프로필만 수정 가능
- 프로필 수정 전 비밀번호 확인 필수
- 현재 비밀번호와 동일한 비밀번호로는 변경 불가
- 게시물 수정 및 삭제는 작성자 본인만 가능
- 댓글 수정 및 삭제는 댓글 작성자 혹은 게시물 작성자만 가능
- 본인이 작성한 게시물과 댓글에 좋아요 불가
- 같은 게시물에는 사용자당 한 번만 좋아요 가능
- 기간별 게시물 검색 시, 시작일자와 종료일자 중 하나라도 지정 안하면 게시물 전체 조회

# 👩‍💻 기능별 담당자
|담당자|기능|블로그 주소|깃허브 주소|
|:----|:----|:----|:----|
|김지연|1. 친구 관리</br>2. 댓글|https://blog.naver.com/yeondata</br>https://velog.io/@yeoni9094/posts|https://github.com/jiyeon0926|
|김단빈|1. 사용자 인증</br>2. 좋아요|https://dreamcompass.tistory.com/|https://github.com/kimdanbin|
|장대산|1. 게시물</br>2. 조건별 정렬</br>3.기간별 게시물 검색|https://velog.io/@diqkraud/posts|https://github.com/daesan12|
|임현수|1. 프로필|https://bluewhale-korea.tistory.com/|https://github.com/HySu|

------------------

# 📄 API 명세서
|기능|Method|URL|RequestHeader|Request|Response|상태코드|비고|
|:----|:----|:----|:----|:----|:----|:----|:----|
|회원가입|POST|/users|POST /users HTTP/1.1</br>Content-Type: application/json|{</br>”email” : “asdf@naver.com”,</br>”username” : “유저이름”,</br>”password”: “1234”</br>}||성공</br>201 Created</br></br>실패</br>400 Bad Request||
|로그인|POST|/login|POST /login HTTP/1.1</br>Content-Type: application/json|{</br>”email” : “asdf@naver.com”,</br>”password”: “1234”</br>}||성공</br>200 OK</br></br>실패</br>400 Bad Request</br>401 Unauthorized||
|로그아웃|POST|/logout|POST /logout HTTP/1.1</br>Cookie: JSESSIONID= ${sessionId}|||성공</br>200 OK||
|탈퇴 시, 비밀번호 확인|POST|/users/checkuser|POST /users/checkuser HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”email”:  “asdf@naver.com”,</br>”password”: “1234”</br>}||성공</br>200 OK</br></br>실패</br>400 Bad Request||
|회원탈퇴|DELETE|/users|DELETE /users HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|||성공</br>200 OK||
|팔로우|POST|/users/{userId}/following|POST /users/1/following HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|||성공</br>200 OK</br></br>실패</br>404 Not Found||
|팔로우 취소|DELETE|/users/{userId}/following|DELETE /users/1/following HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|||성공</br>200 OK</br></br>실패</br>400 Bad Request||
|팔로우 전체 조회|GET|/users/{userId}/following|GET /users/1/following HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||[</br>{</br>”followee_id”: 4,</br>”createAt”: 2024-11-19 17:07:01T17:39:04.048075</br>},</br>{</br>”followee_id”: 4,</br>”createAt”: 2024-11-19 17:07:01T17:39:04.048075</br>}</br>]|성공</br>200 OK|빈 배열일 경우에도 200 OK|
|팔로워 전체 조회|GET| /users/{userId}/follower|GET /users/1/follower HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||[</br>{</br>”follower_id”: 4,</br>”createAt”: 2024-11-19 17:07:01T17:39:04.048075</br>},</br>{</br>”follower_id”: 4,</br>”createAt”: 2024-11-19 17:07:01T17:39:04.048075</br>}</br>]|성공</br>200 OK|빈 배열일 경우에도 200 OK|
|프로필 단건 조회|GET|/users/{userId}/profile| GET /users/1/profile HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||{</br>”id” : 1,</br>”username” : “유저이름”,</br>”email”: “asdf@naver.com”</br>}|성공</br>200 OK</br></br>실패</br>404 Not Found||
|프로필 수정 시, 비밀번호 확인|POST| /users/{userId}/profile| POST /users/1/profile HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br> “password”: “1234”</br>}||성공</br>200 OK</br></br>실패</br>400 Bad Request||
|프로필 수정|PATCH| /users/{userId}/profile|PATCH /users/1/profile HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”username” : “유저이름”,</br>”password”: “1234”</br>}||성공</br>200 OK||
|게시물 작성|POST|/boards|POST /boards HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”</br>}|{</br>"id": 1,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likeCount": 0</br>}| 성공</br>201 Created</br></br>실패</br>400 Bad Request| 모든 값은 필수이며 날씨는 2글자 이내만 가능|
|게시물 수정|PATCH|/boards/{boardId}|PATCH /boards/1 HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”title” : “제목”,</br>”content” : "내용”</br>}|{</br>"id": 1,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likecount": 0</br>}|성공</br>200 OK</br></br>실패</br>400 Bad Request</br>404 Not Found||
|게시물 삭제|DELETE|/boards/{boardId}|DELETE /boards/1 HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|||성공</br>201 Created</br></br>실패</br>400 Bad Request||
|게시물 전체 조회|GET|/boards|GET /boards HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||[</br>{</br>"id": 1,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likeCount": 3</br>},</br>{</br>"id": 2,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likeCount": 1</br>}</br>]|성공</br>201 Created</br></br>실패</br>400 Bad Request||
|게시물 단건 조회|GET|/boards/{boardId}|GET /boards/1 HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||{</br>"id": 1,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likeCount": 3</br>}|성공</br>201 Created</br></br>실패</br>400 Bad Request||
|팔로우한 사용자의 게시물 전체 조회|GET|/boards/following|GET /boards/following HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||[</br>{</br>"id": 1,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likeCount": 3},</br>{</br>"id": 2,</br>”title” : “제목”,</br>”content” : "내용”,</br>”weather”: “맑음”,</br>”createdAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-19 17:07:01T17:39:04.048075,</br>"likeCount": 1</br>}</br>]|성공</br>200 OK||
|댓글 작성|POST|/boards/{boardId}/comments|POST /boards/1/comments HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”content” : "내용”</br>}|{</br>"id": 3,</br>”content” : "내용”,</br>”createdAt”: 2024-11-20 17:07:01T17:39:04.048075,</br>"modifiedAt”: 2024-11-20 17:07:01T17:39:04.048075,</br>"likeCount": 0</br>}|성공</br>201 Created||
|댓글 수정|PATCH|/boards/{boardId}/comments/{commentId}|PATCH /boards/1/comments/1 HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}| {</br>”content” : "내용2”</br>}|{</br>"id": 3,</br>”content” : "내용”,</br>"modifiedAt”: 2024-11-20 17:07:01T17:39:04.048075,</br>"likeCount": 4</br>}|성공</br>200 OK</br></br>실패</br>400 Bad Request</br>403 Forbidden||
|특정 게시물의 댓글 전체 조회|GET|/boards/{boardId}/comments|GET /boards/1/comments HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||[</br>{</br>"id": 4,</br>”content” : "내용”,</br>"modifiedAt”: 2024-11-20 17:07:01T17:39:04.048075</br>"likeCount": 4</br>},</br>{</br>"id": 3,</br>”content” : "내용”,</br>"modifiedAt”: 2024-11-20 17:07:01T17:39:04.048075</br>"likeCount": 2</br>}</br>]|성공</br>200 OK</br></br>실패</br>404 Not Found||
|게시물 좋아요|POST|/boards/{boardId}/good|POST /boards/1/good HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”email” : “asdf@naver.com”,</br>”username” : “유저이름”,</br>”password”: “1234”</br>}||성공</br>200 OK||
|게시물 좋아요 취소|DELETE|/boards/{boardId}/good|DELETE /boards/1/good HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|||성공</br>200 OK</br></br>실패</br>400 Bad Request||
|댓글 좋아요|POST|/boards/{boardId}/comments/{commentId}/good | POST /boards/1/comments/1/good HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}|{</br>”email” : “asdf@naver.com”,</br>”username” : “유저이름”,</br>”password”: “1234”</br>}||성공</br>200 OK</br></br>실패</br>400 Bad Request||
|댓글 좋아요 취소|DELETE|/boards/{boardId}/comments/{commentId}/good | DELETE /boards/1/comments/1/good HTTP/1.1</br>Content-Type: application/json</br>Cookie: JSESSIONID= ${sessionId}||| 성공</br>200 OK</br></br>실패</br>400 Bad Request||

---------------

# ☁ ERD
- user Table : 회원 테이블
- board Table : 게시물 테이블
- follow Table : 친구 관리 테이블
- comment Table : 댓글 테이블
- goodboard Table : 게시물 좋아요 테이블
- goodcomment Table : 댓글 좋아요 테이블

![image](https://github.com/user-attachments/assets/a773d266-a95f-4a92-9b8d-99de92c2549e)
