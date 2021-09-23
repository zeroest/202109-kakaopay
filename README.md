
# 카카오페이 서버개발 과제

## 서버 구성

- 투자 상품이 오픈될 때, 다수의 고객이 동시에 투자를 시작하기에 트래픽 분산을 위해  
데이터를 읽어오는 기능의 서버와 데이터를 쓰는 서버 두개로 나누어 기능을 분리하였다.  
따라서 core 모듈에서 공통적인 기능을 담당하고 http 모듈에서 http 서버의 공통 설정을 구현하고  
읽기 서버는 http_read 쓰기 서버는 http_write 모듈로 분리하였다.  


- PRODUCT 상품 테이블을 기준으로  
PRODUCT_INVEST_STATUS 상품의 투자 상태 테이블  
PRODUCT_INVEST_LOG 상품의 투자 로그 테이블  
을 구축하였다.

![erd](./readme_img/ERD.png)

- index 설정 최적화
- ehcache redis 캐시 최적화
- x-user-id global exception 처리