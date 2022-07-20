
# Online Class Application System 

# 온라인 수강신청 시스템 

본 예제는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전체 구현과제를 구성한 예제입니다.
이는 Cloud Native Application의 개발에 요구되는 체크포인트들을 통과하기 위한 예시 답안을 포함합니다.
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW


# Table of contents

- [온라인 수강신청 시스템](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [DDD 의 적용](#ddd-의-적용)
    - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
    - [폴리글랏 프로그래밍](#폴리글랏-프로그래밍)
    - [동기식 호출 과 Fallback 처리](#동기식-호출-과-Fallback-처리)
    - [비동기식 호출 과 Eventual Consistency](#비동기식-호출-과-Eventual-Consistency)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출-서킷-브레이킹-장애격리)
    - [오토스케일 아웃](#오토스케일-아웃)
    - [무정지 재배포](#무정지-재배포)
    - [개발 운영 환경 분리](#개발-운영-환경-분리)
    - [모니터링](#모니터링)

# 서비스 시나리오


- 기능적 요구사항
1.  강사가 온라인 강의를 등록/수정/삭제 등 강의를 관리 등록 관리한다.
2.  수강생은 강사가 등록한 강의를 검색한 후 원하는 강의를 신청한다.
3.  수강생은 신청한 강의에 대한 강의료를 결제한다.
4.  강의 수강신청이 완료되면 강의에 대한 교재를 발송한다.
5.  강의 수강신청을 취소하면 강의에 대한 교재 배송을 취소한다.
6.  수강생은 강의 수강신청 내역을 볼 수 있다.


- 비기능적 요구사항
1. 트랜잭션
    1. 강의 결제가 완료 되어야만 수강 신청 완료 할 수 있음 Sync. 호출
    
2. 장애격리
    1. 수강신청 시스템이 과중되면 사용자를 잠시동안 받지 않고 신청을 잠시 후에 하도록 유도한다.  Circuit breaker
    
3. 성능
    1. 학생은 마이페이지에서 등록된 강의와 수강 및 교재 배송 상태를 확인할 수 있어야 한다.  CQRS
    2. 수강신청/배송 상태가 바뀔때마다 내역을 확인할 수 있어야 한다.  Event driven


# 체크포인트

- 분석 설계
  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계

## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과: https://www.msaez.io/#/storming/Qc7rnHkkrURTrLCpeHshWc2R4fh1/09ad22efdecd6796bc07779925709a47


### 이벤트 도출
![이벤트 도출](https://user-images.githubusercontent.com/49930207/131055626-88773fdf-77a1-4ac7-a25e-9b200fa565b1.png)

### Actor, Command 추가
![ActorCommand추가](https://user-images.githubusercontent.com/88864399/135450059-28f34727-9737-4bf3-98ab-1dea01b430a1.png)


### Aggregate으로 묶기 
![aggregate](https://user-images.githubusercontent.com/88864399/135451225-a2d4bf02-475a-4697-91e3-5803e25f6d40.png)


### Bounded Context로 묶기 
![contextbind](https://user-images.githubusercontent.com/88864399/135565258-fcdfcedd-9a66-441e-aa01-0bcbb85d7c9c.png)


### Policy의 이동과 Context 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)
![pubsub](https://user-images.githubusercontent.com/88864399/135450578-fcf0dcf5-cee3-4b3b-aa24-007856b6dfc8.png)

### 기능적/비기능적 요구사항을 만족하는지 검증
![요구사항검증](https://user-images.githubusercontent.com/88864399/135446489-e7926c1b-1626-4bb1-892f-605ecda350da.png)

- 요구사항 만족 검증(1 : 정상처리)     
   1. 수강생이 강의를 신청한다 (ok)
   2. 수강생이 강의를 결제한다 (ok)
   3. 강의신청이 되면 주문 내역이 배송팀에게 전달된다 (ok)
   4. 배송팀에서 강의 교재 배송 출발한다 (ok) 
   
- 요구사항 만족 검증(2 : 취소처리)  
   1. 수강생이 강의를 취소할 수 있다 (ok) 
   2. 강의가 취소되면 결제 취소된다 (ok) 
   3. 결제가 취소되면 배송이 취소된다 (ok) 


### 완료 모델

![완료모델](https://user-images.githubusercontent.com/88864399/135446610-719a7500-c88f-4a45-bec3-31978588c758.png)


### 비기능 요구사항에 대한 검증

- Sync 호출 
  - 강의 결제가 완료 되어야만 수강 신청 완료 할 수 있어야 한다. 
    - Circuit breaker 장애격리
      - 수강신청 시스템에 부하 등으로 인한 장애상태가 되면 사용자를 잠시동안 받지 않고 신청을 잠시 후에 하도록 유도한다
- Async 호출(Event Driven 방식) 
  - 주문상태, 배달상태 등 대부분의 이벤트에 대해 데이터 완벽한 일관성을 유지할 필요성이 없다고 판단되어 Eventual Consistency 방식으로 처리.
- CQRS
  - 학생은 MyPage에서 등록된 강의와 수강 및 교재 배송 상태를 확인할 수 있어야 한다. (비동기식으로 Kafka를 통해 이벤트를 수신)
  

## 헥사고날 아키텍처 다이어그램 도출1

![헥사고날다이어그램1](https://user-images.githubusercontent.com/88864399/135446899-8480f2f7-e83c-44cf-a939-ac1ecca8b4eb.png)

  - MsaEz의 Hexagonal 기능을 활용하여 헥사고날 다이어그램을 추출함. 

## 헥사고날 아키텍처 다이어그램 도출2 
 
 ![헥사고날아키텍처2](https://user-images.githubusercontent.com/88864399/135450215-4bc0c342-ca80-4512-a1b6-f86a67ce19ef.png)

  - MsaEz를 통해 추출한 헥사고날 다이어그램을 통해 다음과 같이 작성
  - 호출관계에서 PubSub 과 Req/Resp 를 구분하여 작성
  - Sub 도메인과 Bounded Context를 분리하여 작성 


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 Bounded Context 별로 대변되는 마이크로 서비스들을 Sping-Boot로 구현하였다. (각 서비스 분산을 위한 gateway 서비스도 구현하였다)   

```
cd class
mvn spring-boot:run 

cd payment
mvn spring-boot:run  

cd delivery
mvn spring-boot:run 

cd mypage
mvn spring-boot:run

cd gateway
mvn spring-boot:run

```

- AWS 클라우드의 EKS 서비스 내에 서비스를 모두 배포 후 상세 정보를 조회한다. : namespace user02로 생성 
```
root@labs-389288629:/home/project# kubectl get all -n user02
NAME                           READY   STATUS    RESTARTS   AGE
pod/class-6d99b46784-l9qpr     1/1     Running   0          52m
pod/gateway-86d945d69-zvbcp    1/1     Running   0          6m48s
pod/mypage-84d8c6f477-p6r2h    1/1     Running   0          4h39m
pod/payment-5f87c76694-2kh8s   1/1     Running   0          4h27m

NAME              TYPE           CLUSTER-IP       EXTERNAL-IP                                                                   PORT(S)        AGE
service/class     ClusterIP      10.100.38.135    <none>                                                                        8080/TCP       52m
service/gateway   LoadBalancer   10.100.113.249   a6ee5f8662eb24e6a959e1de66f329e3-532480599.ap-northeast-2.elb.amazonaws.com   80:32545/TCP   35m
service/mypage    ClusterIP      10.100.194.107   <none>                                                                        8080/TCP       4h39m
service/payment   ClusterIP      10.100.50.151    <none>                                                                        8080/TCP       4h27m

NAME                      READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/class     1/1     1            1           52m
deployment.apps/gateway   1/1     1            1           35m
deployment.apps/mypage    1/1     1            1           4h39m
deployment.apps/payment   1/1     1            1           4h27m

NAME                                 DESIRED   CURRENT   READY   AGE
replicaset.apps/class-6d99b46784     1         1         1       52m
replicaset.apps/gateway-6df7565487   0         0         0       35m
replicaset.apps/gateway-86d945d69    1         1         1       6m48s
replicaset.apps/mypage-84d8c6f477    1         1         1       4h39m
replicaset.apps/payment-5f87c76694   1         1         1       4h27m
```
 
 ![eks](https://user-images.githubusercontent.com/88864399/135464299-f07862ea-0e78-4b38-bbc8-bcc62d57bca8.png)


### 적용 후 REST API 테스트
 
- 등록

1. 수강 신청 

```
http POST http://a6ee5f8662eb24e6a959e1de66f329e3-532480599.ap-northeast-2.elb.amazonaws.com/classes studentName="학생2" classId="2" addr="SEOUL NAMGU" telephoneInfo="010-1234-2345" payMethod="CARD" payAccount="1234-2334-4556-7890" applyStatus="ApplyRequest"

http POST http://localhost:8081/classes studentName="학생2" classId="2" addr="SEOUL NAMGU" telephoneInfo="010-1234-2345" payMethod="CARD" payAccount="1234-2334-4556-7890" applyStatus="ApplyRequest"
 
HTTP/1.1 201 
Content-Type: application/json;charset=UTF-8
Date: Thu, 30 Sep 2021 13:53:32 GMT
Location: http://localhost:8081/classes/1
Transfer-Encoding: chunked

{
    "_links": {
        "class": {
            "href": "http://localhost:8081/classes/1"
        },
        "self": {
            "href": "http://localhost:8081/classes/1"
        }
    },
    "addr": "SEOUL NAMGU",
    "applyStatus": "CLASS_COMPLETED",
    "courseId": null,
    "payAccount": "1234-2334-4556-7890",
    "payMethod": "BANK",
    "studentName": "학생2",
    "telephoneInfo": "010-1234-2345"
}
```

2. 수강 등록 확인

```
http GET http://localhost:8081/classes

HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 30 Sep 2021 14:09:55 GMT
Transfer-Encoding: chunked

{
    "_embedded": {
        "classes": [
            {
                "_links": {
                    "class": {
                        "href": "http://localhost:8081/classes/1"
                    },
                    "self": {
                        "href": "http://localhost:8081/classes/1"
                    }
                },
                "addr": "SEOUL NAMGU",
                "applyStatus": "ApplyRequest",
                "courseId": null,
                "payAccount": "1234-2334-4556-7890",
                "payMethod": "BANK",
                "studentName": "학생2",
                "telephoneInfo": "010-1234-2345"
            },
            {
                "_links": {
                    "class": {
                        "href": "http://localhost:8081/classes/2"
                    },
                    "self": {
                        "href": "http://localhost:8081/classes/2"
                    }
                },
                "addr": "SEOUL NAMGU",
                "applyStatus": "ApplyRequest",
                "courseId": null,
                "payAccount": "1234-2334-4556-7890",
                "payMethod": "CARD",
                "studentName": "학생2",
                "telephoneInfo": "010-1234-2345"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://localhost:8081/profile/classes"
        },
        "search": {
            "href": "http://localhost:8081/classes/search"
        },
        "self": {
            "href": "http://localhost:8081/classes{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 2,
        "totalPages": 1
    }
}
```

3. 결제 확인
```
http GET http://localhost:8083/payments

HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 30 Sep 2021 14:11:08 GMT
Transfer-Encoding: chunked

{
    "_embedded": {
        "payments": [
            {
                "_links": {
                    "payment": {
                        "href": "http://localhost:8083/payments/1"
                    },
                    "self": {
                        "href": "http://localhost:8083/payments/1"
                    }
                },
                "addr": "SEOUL NAMGU",
                "applyId": "1",
                "payAccount": "1234-2334-4556-7890",
                "payMethod": "BANK",
                "payStaus": "PAYMENT_COMPLETED",
                "studentName": "학생2",
                "telephoneInfo": "010-1234-2345"
            },
            {
                "_links": {
                    "payment": {
                        "href": "http://localhost:8083/payments/2"
                    },
                    "self": {
                        "href": "http://localhost:8083/payments/2"
                    }
                },
                "addr": "SEOUL NAMGU",
                "applyId": "2",
                "payAccount": "1234-2334-4556-7890",
                "payMethod": "CARD",
                "payStaus": "PAYMENT_COMPLETED",
                "studentName": "학생2",
                "telephoneInfo": "010-1234-2345"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://localhost:8083/profile/payments"
        },
        "search": {
            "href": "http://localhost:8083/payments/search"
        },
        "self": {
            "href": "http://localhost:8083/payments{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 2,
        "totalPages": 1
    }
}
```

- 취소

1. 수강 취소

```
http PATCH http://localhost:8081/classes/1 applyStatus=“CLASS_CANCELED”
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Thu, 30 Sep 2021 14:23:33 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "class": {
            "href": "http://localhost:8081/classes/1"
        },
        "self": {
            "href": "http://localhost:8081/classes/1"
        }
    },
    "addr": "SEOUL NAMGU",
    "applyStatus": "“CLASS_CANCELED”",
    "courseId": null,
    "payAccount": "1234-2334-4556-7890",
    "payMethod": "BANK",
    "studentName": "학생2",
    "telephoneInfo": "010-1234-2345"
}
```

2 수강 취소내역 확인
```
http GET http://localhost:8081/classes/1
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 30 Sep 2021 14:24:26 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "class": {
            "href": "http://localhost:8081/classes/1"
        },
        "self": {
            "href": "http://localhost:8081/classes/1"
        }
    },
    "addr": "SEOUL NAMGU",
    "applyStatus": "“CLASS_CANCELED”",
    "courseId": null,
    "payAccount": "1234-2334-4556-7890",
    "payMethod": "BANK",
    "studentName": "학생2",
    "telephoneInfo": "010-1234-2345"
}
```

2. 결제 취소 확인 (상태값이 "PaymentCancelled" 로 변경됨을 확인할 수 있음)
```
http GET http://localhost:8083/payments/1
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 30 Sep 2021 14:25:03 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "payment": {
            "href": "http://localhost:8083/payments/1"
        },
        "self": {
            "href": "http://localhost:8083/payments/1"
        }
    },
    "addr": "SEOUL NAMGU",
    "applyId": "1",
    "payAccount": "1234-2334-4556-7890",
    "payMethod": "BANK",
    "payStaus": "PaymentCancelled",
    "studentName": "학생2",
    "telephoneInfo": "010-1234-2345"
}
``` 

//My page 최종적으로 변경되는 데이터 확인 
```
http GET http://localhost:8085/mypages
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 30 Sep 2021 14:26:53 GMT
Transfer-Encoding: chunked

{
    "_embedded": {
        "mypages": [
            {
                "_links": {
                    "mypage": {
                        "href": "http://localhost:8085/mypages/1"
                    },
                    "self": {
                        "href": "http://localhost:8085/mypages/1"
                    }
                },
                "addr": "SEOUL NAMGU",
                "applyId": "1",
                "applyStaus": "ApplyCanceled",
                "courseId": "null",
                "deliveryStatus": "DeliveryCanceled",
                "payAccount": "1234-2334-4556-7890",
                "payMethod": "BANK",
                "payStatus": "PayCanceled",
                "studentName": "학생2",
                "telephoneInfo": "010-1234-2345"
            },
            {
                "_links": {
                    "mypage": {
                        "href": "http://localhost:8085/mypages/2"
                    },
                    "self": {
                        "href": "http://localhost:8085/mypages/2"
                    }
                },
                "addr": "SEOUL NAMGU",
                "applyId": "2",
                "applyStaus": "ApplyFinish",
                "courseId": "null",
                "deliveryStatus": "DeliveryFinish",
                "payAccount": "1234-2334-4556-7890",
                "payMethod": "CARD",
                "payStatus": "PayFinish",
                "studentName": "학생2",
                "telephoneInfo": "010-1234-2345"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://localhost:8085/profile/mypages"
        },
        "search": {
            "href": "http://localhost:8085/mypages/search"
        },
        "self": {
            "href": "http://localhost:8085/mypages"
        }
    }
}
```


## polyglot 

Delivery(배송) 서비스는 mysql 을 사용하여 구현하였다. 
Spring Cloud JPA를 사용하여 개발하였기 때문에 소스의 변경 부분은 전혀 없으며, 단지 Dependency 내 데이터베이스 설정(pom.xml, application.yml)을 변경하는 것으로 기존 H2 DB를 사용하는 것과 mysql DB를 사용하는 것으로 동작할 수 있도록 처리하였다.  

```
# pom.yml (Delivery)

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.25</version>
		</dependency>
		<dependency>

```

```
# application.yml (Delivery)

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql-1631696398.default.svc.cluster.local:3306/class?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: RmDqZpf2rq
  jpa:
    database: mysql
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    generate-ddl: true
    show-sql: true 

```
 

## CQRS

 - Class, Payment, Delivery 등을 통해 생성된 수강신청, 결제, 배송 정보를 별도의 서비스를 통해 조회(Mypage)할 수 있도록 하여 CQRS로 구현함  
 - 비동기식으로 Kafka를 통해 이벤트를 별도로 수신하여 처리될 수 있도록 관리한다


```
MyPageViewHandler.java
~~

  @StreamListener(KafkaProcessor.INPUT)
    public void whenClassRegisted_then_CREATE_1 (@Payload ClassRegisted classRegisted) {
        try {

            System.out.println("\n\n##### listener classRegisted : " + classRegisted.toJson() + "\n\n");

            if (!classRegisted.validate()) return;

            // view 객체 생성
            Mypage mypage = new Mypage();
            // view 객체에 이벤트의 Value 를 set 함
            mypage.setApplyId(String.valueOf(classRegisted.getId()));
            mypage.setCourseId(String.valueOf(classRegisted.getCourseId()));
            mypage.setApplyStaus("ApplyFinish");
            mypage.setPayMethod(classRegisted.getPayMethod());
            mypage.setPayAccount(classRegisted.getPayAccount());
            mypage.setStudentName(classRegisted.getStudentName());
            mypage.setAddr(classRegisted.getAddr());
            mypage.setTelephoneInfo(classRegisted.getTelephoneInfo());
            mypage.setPayStatus("PayFinish");
            //mypage.setDeliveryStatus("D");
            // view 레파지 토리에 save
            mypageRepository.save(mypage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentAppoved_then_UPDATE_1(@Payload PaymentAppoved paymentAppoved) {
        try {
            if (!paymentAppoved.validate()) return;
                // view 객체 조회
                System.out.println("\n\n##### listener paymentAppoved : " + paymentAppoved.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(paymentAppoved.getApplyId());
                for(Mypage mypage : mypageList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setPayStatus("PayFinish");
                    mypage.setApplyStaus("ApplyFinish");
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_2(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회
                System.out.println("\n\n##### listener deliveryStarted : " + deliveryStarted.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(deliveryStarted.getApplyId());
                for(Mypage mypage : mypageList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함

                    mypage.setDeliveryStatus("DeliveryFinish");
                // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenClassCanceled_then_UPDATE_3(@Payload ClassCanceled classCanceled) {
        try {
            if (!classCanceled.validate()) return;
                // view 객체 조회
            System.out.println("\n\n##### listener classCanceled : " + classCanceled.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(String.valueOf(classCanceled.getId()));
                for(Mypage mypage : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setApplyStaus("CancelRequest");
                // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

## Req/Resp 동기식 호출 과 Fallback 처리

Req/Resp 처리를 위해 결제가 처리가 정상적으로 완료되지 않는 건은 수강신청이 불가하도록, 양 쪽의 처리에 일관성을 유지할 수 있도록 트랜잭션으로 처리하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 


```
# (class) PaymentService.java

~~
 
import feign.Feign;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;

@FeignClient(name="payment", url="${feign.client.url.paymentUrl}" )
public interface PaymentService {
    
    @RequestMapping(method= RequestMethod.POST, path="/payments", consumes = "application/json") //payments로 해야 데이터insert
    public boolean payApprove(@RequestBody Payment payment);
    
    @Component
    class PaymentServiceFallback implements PaymentService {
        @Override
        public Long payCellPhone(Long orderId,Integer paymentAmt,Long cellphoneId){
            System.out.println("\n###PaymentServiceFallback works####\n");   // fallback 메소드 작동 테스트
            return 0L;
        }
    }
    
    @Component
    class PaymentServiceConfiguration {
        Feign.Builder feignBuilder(){
            SetterFactory setterFactory = (target, method) -> HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(target.name()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(Feign.configKey(target.type(), method)))
                    // 위는 groupKey와 commandKey 설정
                    // 아래는 properties 설정
                    .andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter()
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                            .withMetricsRollingStatisticalWindowInMilliseconds(10000) // 기준시간
                            .withCircuitBreakerSleepWindowInMilliseconds(3000) // 서킷 열려있는 시간
                            .withCircuitBreakerErrorThresholdPercentage(50)) // 에러 비율 기준 퍼센트
                    ; // 최소 호출 횟수
            return HystrixFeign.builder().setterFactory(setterFactory);
        }
    }
}
```

- class와 payment서비스가 올라가있는 상황에서 정상적으로 등록됨을 확인할 수 있음. 
 
![동기호출1_등록_class](https://user-images.githubusercontent.com/88864399/135483347-68ed79b4-3921-41bd-88aa-002786349713.png)

![동기호출-정상처리](https://user-images.githubusercontent.com/88864399/135483469-2ed6d873-b76b-4d1b-825e-fd23649b346c.png)

 
- 동기식 호출에서는 Payment가 장애가 발생할 경우, Class도 fallback 처리를 하지 못하여 정상적으로 동작하지 못함을 확인(에러 메시지 확인) 
 
![동기호출-정상처리](https://user-images.githubusercontent.com/88864399/135486073-d080f902-ae2e-4189-a0fe-2f4279d0a472.png)
 
![동기호출1_등록_class](https://user-images.githubusercontent.com/88864399/135486083-bba419dd-bd5a-4fc8-8e42-f93a5aa68381.png)


- FallBack 처리

1. Req/Resp 구조의 Class와 Payment 간에 Fallback 처리를 위해 Hystrix를 사용하여 기능을 구현한다. 

2. Class 서비스 내 external 쪽에 PaymentService.java 을 생성하여 fallback 처리를 할 수 있도록 구현한다. 
 

```

# (class) PaymentService.java

~~
 
import feign.Feign;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;

@FeignClient(name="payment", url="${feign.client.url.paymentUrl}", configuration=PaymentService.PaymentServiceConfiguration.class, fallback=PaymentServiceFallback.class)
public interface PaymentService {
    
    @RequestMapping(method= RequestMethod.POST, path="/payments", consumes = "application/json") //payments로 해야 데이터insert
    public boolean payApprove(@RequestBody Payment payment);
    
    @Component
    class PaymentServiceFallback implements PaymentService {
        @Override
        public Long payCellPhone(Long orderId,Integer paymentAmt,Long cellphoneId){
            System.out.println("\n###PaymentServiceFallback works####\n");   // fallback 메소드 작동 테스트
            return 0L;
        }
    }
    
# (class) PaymentServiceFallback.java


import org.springframework.stereotype.Component;

@Component
public class PaymentServiceFallback implements PaymentService {
    @Override
    public boolean payApprove(Payment payment) {

        System.out.println("\\n=========FALLBACK STARTING=========\\n"); //fallback 메소드 작동 테스트
	
        return false;
    }
}
```

-FallBack처리를하면, Payment장애라도 Class기동 중이면 정상처리됨 
 ![fallback처리정상](https://user-images.githubusercontent.com/88864399/135487829-c88cf5ff-8dab-416e-9024-8758dba06ada.png)



## Gateway 

- Gateway 생성하여 각각의 마이크로서비스들을 하나의 통로(진입점)를 통하여 처리될 수 있도록 구현. 

- Gateway 서비스 기동 후, Gateway를 통해 각각의 마이크로서비스로 접근이 가능한지 확인 

```
(gateway) application.yml 

~ 
server:
  port: 8088

---

spring:
  profiles: default
  cloud:
    gateway:
      routes:
        - id: class
          uri: http://localhost:8081
          predicates:
            - Path=/classes/**  
        - id: payment
          uri: http://localhost:8083
          predicates:
            - Path=/payments/** 
        - id: delivery
          uri: http://localhost:8084
          predicates:
            - Path=/deliveries/** 
        - id: mypage
          uri: http://localhost:8085
          predicates:
            - Path= /mypages/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

---

spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: class
          uri: http://class:8080
          predicates:
            - Path=/classes/**  
        - id: payment
          uri: http://payment:8080
          predicates:
            - Path=/payments/** 
        - id: delivery
          uri: http://delivery:8080
          predicates:
            - Path=/deliveries/** 
        - id: mypage
          uri: http://mypage:8080
          predicates:
            - Path= /mypages/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080
```

![gateway](https://user-images.githubusercontent.com/88864399/135489918-45c74b73-7c39-429e-9443-cb7b7155ef45.png)


# 운영

## CI/CD 설정

- AWS가 제공하는 codebuild를 통해 buildspec.yml 파일을 사용하여 pipeline생성 및 Deploy

![codebuild2](https://user-images.githubusercontent.com/88864399/135491714-6404e7fd-912e-43dc-a388-981444cc0e67.png)

- buildspec.yml 파일(예시, class)

```  
version: 0.2

env:
  variables:
    _PROJECT_NAME: "user0202-class"

phases:
  install:
    commands:
      - echo install kubectl
      - curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
      - chmod +x ./kubectl
      - mv ./kubectl /usr/local/bin/kubectl
  pre_build:
    commands:
      - echo Logging in to Amazon ECR...
      - echo $_PROJECT_NAME
      - echo $AWS_ACCOUNT_ID
      - echo $AWS_DEFAULT_REGION
      - echo $CODEBUILD_RESOLVED_SOURCE_VERSION
      - echo start command
      #- $(aws ecr get-login --no-include-email --region $AWS_DEFAULT_REGION)
      - docker login --username AWS -p $(aws ecr get-login-password --region $AWS_DEFAULT_REGION) $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/
  build:
    commands:
      - echo Build started on `date`
      - echo Building the Docker image...
      - mvn package -Dmaven.test.skip=true
      - docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$_PROJECT_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION  .
  post_build:
    commands:
      - echo Pushing the Docker image...
      - docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$_PROJECT_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION
      - echo connect kubectl
      - kubectl config set-cluster k8s --server="$KUBE_URL" --insecure-skip-tls-verify=true
      - kubectl config set-credentials admin --token="$KUBE_TOKEN"
      - kubectl config set-context default --cluster=k8s --user=admin
      - kubectl config use-context default
      - echo $KUBE_URL
      - echo $KUBE_TOKEN
      - kubectl config current-context
      - |
          cat <<EOF | kubectl apply -f -
          apiVersion: v1
          kind: Service
          metadata:
            name: class
            namespace: user02
            labels:
              app: class
          spec:
            ports:
              - port: 8080
                targetPort: 8080
            selector:
              app: class
          EOF
      - |
          cat  <<EOF | kubectl apply -f -
          apiVersion: apps/v1
          kind: Deployment
          metadata:
            name: class
            namespace: user02
            labels:
              app: class
          spec:
            replicas: 1
            selector:
              matchLabels:
                app: class
            template:
              metadata:
                labels:
                  app: class
              spec:
                containers:
                  - name: class
                    image: $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$_PROJECT_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION
                    ports:
                      - containerPort: 8080
                    resources:
                      limits:
                        cpu: 500m
                      requests:
                        cpu: 500m       
                    readinessProbe:
                      httpGet:
                        path: '/classes'
                        port: 8080
                      initialDelaySeconds: 20
                      timeoutSeconds: 2
                      periodSeconds: 5
                      failureThreshold: 10
                    livenessProbe:
                      httpGet:
                        path: '/classes'
                        port: 8080
                      initialDelaySeconds: 180
                      timeoutSeconds: 2
                      periodSeconds: 5
                      failureThreshold: 5
          EOF
#cache:
#  paths:
#    - '/root/.m2/**/*'
``` 



## HPA(autoscale) 

각 마이크로서비스에 부하가 들어올 경우, 동적으로 replica를 확장하여, 각 서비스가 문제 없이 구동될 수 있도록 기능을 적용하고자 함.   


- class 서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. CPU 사용량이 10%를 넘으면 replica 를 최대 10개까지 늘릴 수 있도록 설정해준다.(초기 50%로 설정을 잡으니 부하를 아무리 넣어도 replica가 증가하지 않는다) 

```
kubectl autoscale deploy class --min=1 --max=10 --cpu-percent=10
```

- siege를 통해 부하를 넣어준다. (siege.yaml파일을 만들어 작업 :kubectl apply -f siege.yaml)   
```
siege -c30 –t10S   --content-type "application/json" 'http://localhost:8081/classes POST {"courseId":2}' 
```

- 오토스케일이 어떻게 되고 있는지 모니터링을 걸어 replica 가 증가함을 확인한다. 

![hpa최종부하](https://user-images.githubusercontent.com/88864399/135562410-ffcdbec2-6940-40d1-b1ef-56478956575f.png)
 
 
## Liveness Probe(Self-healing)

- buildspec.yaml파일에 Liveness Probe 가 동작 할 수 있도록 한다. 

```
              spec:
                containers:
                  - name: delivery
                    image: $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$_PROJECT_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION
                    ports:
                      - containerPort: 8080
                    readinessProbe:
                      httpGet:
                        path: '/deliveries'
                        port: 8080
                      initialDelaySeconds: 20
                      timeoutSeconds: 2
                      periodSeconds: 5
                      failureThreshold: 10
                    livenessProbe:
                      httpGet:
                        path: '/deliveries'
                        port: 8080
                      initialDelaySeconds: 120
                      timeoutSeconds: 2
                      periodSeconds: 5
                      failureThreshold: 5
		      
~~~

``` 
  
- Port를 변경하여 kubelet이 지속적으로 실행중인 컨테이너의 Socket을 열려고 시도하고 정상이 아니기에 컨테이너를 재시작한다. RESTARTS 값이 올라감을 확인할 수 있다. 

![livenessProbe](https://user-images.githubusercontent.com/88864399/135493638-6b49ff05-b92c-4df3-9948-b3419bb0e998.png)



## Readiness Probe

* 먼저 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscaler 이나 CB 설정을 제거함

- siege 로 배포작업 직전에 워크로드를 모니터링 함.
```
siege -c50 –t30S  -v -r --content-type "application/json" 'http://localhost:8081/classes POST {"courseId":2}' 

** SIEGE 4.0.5
** Preparing 100 concurrent users for battle.
The server is now under siege...

HTTP/1.1 201     3.43 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     1.28 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     0.20 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     3.44 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     1.18 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     0.28 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     1.41 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     1.22 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     0.21 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     0.13 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     1.41 secs:     251 bytes ==> POST http://localhost:8081/classes
HTTP/1.1 201     1.31 secs:     251 bytes ==> POST http://localhost:8081/classes

``` 

- siege 의 화면으로 넘어가서 Availability 가 100% 미만으로 떨어졌는지 확인
```
Transactions:                    614 hits
Availability:                  35.35 %
Elapsed time:                  34.95 secs
Data transferred:               0.38 MB
Response time:                  3.87 secs
Transaction rate:              17.57 trans/sec
Throughput:                     0.01 MB/sec
Concurrency:                   68.06
Successful transactions:         614
Failed transactions:            1123
Longest transaction:           29.72
Shortest transaction:           0.00
```
- 배포 중 Availability 가 평소 100%에서 35% 대로 떨어지는 것을 확인. 원인은 쿠버네티스가 성급하게 새로 올려진 서비스를 READY 상태로 인식하여 서비스 유입을 진행한 것이기 때문. 이를 막기위해 Readiness Probe 를 설정함:

``` 
# (class) buildspec.yml 파일 내 readiness probe 설정을 다시 돌려 놓음 
 
                    readinessProbe:
                      httpGet:
                        path: '/classes'
                        port: 8080
                      initialDelaySeconds: 20
                      timeoutSeconds: 2
                      periodSeconds: 5
                      failureThreshold: 10
 
```

- 동일한 시나리오로 재배포 한 후 Availability 확인: 

![readiness](https://user-images.githubusercontent.com/88864399/135563231-dba238eb-2092-45bb-9a7a-9d59eff26fbe.png)



배포기간 동안 Availability 가 변화없기 때문에 무정지 재배포가 성공한 것으로 확인됨.


## PersistentVolumeClaim 

- PersistentVolumeClaim 생성
```
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: aws-efs
spec: 
  accessModes: 
  - ReadWriteMany 
  resources:
    requests:
      storage: 1Mi
  storageClassName: aws-efs

root@labs-389288629:/home/project/project/configmap# kubectl apply -f volume-pvc.yaml
persistentvolumeclaim/aws-efs created
```

- PersistentVolumeClaim을 가진 Pod 생성 하기 
```
apiVersion: v1
kind: Pod
metadata:
  name: payment
  namespace: user02
spec: 
  containers:
  - name: payment
    image: nginix:1.15.5
    resources:
      requests:
        cpu: 100m
        memory: 128Mi
      limits:
        cpu: 250m
        memory: 256Mi
    volumeMounts:
    - mountPath: "/mnt/aws"
      name: volume
  volumes:
    - name: volume
      persistentVolumeClaim:
        claimName: aws-efs
	
root@labs-389288629:/home/project/project/configmap# kubectl apply -f pod-with-pvc.yaml
pod/mypod created
```
 
- 생성되었지만 계속 Pending 상태가 지속되어 pvc 내용을 확인 : 프로비저닝 실패 
storageclass.storage.k8s.io "aws-efs" not found 를 찾지 못하여 실패하였다.

```
root@labs-389288629:/home/project/project/configmap# kubectl get pvc -n user02
NAME      STATUS    VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
aws-efs   Pending                                      aws-efs        2m49s
```

![pvc_provisioningfail](https://user-images.githubusercontent.com/88864399/135549625-ee4af60d-a6f1-4694-82b5-aafa8c7d544d.png)


 

## Circuitbreak 
 
특정 서비스(Class)에 과도한 요청으로 지연이 발생하는 경우 CircuitBreake 통해 장애격리를 구현하고자 함. 
Spring FeignClient + Hystrix 사용하여 Circuitbreak 를 구현하고자 하였으나, 구현의 어려움이 있어 중단함. 
 
