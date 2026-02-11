# AccountGroup 도메인 상세 지식

## 개요
사용자 간 가계부 공유 그룹 도메인 (구현 예정)

## 계획된 기능
- 사용자끼리 그룹 생성
- 그룹 내 가계부 공유
- 월별 캘린더 조회
- 페이징 지원

## 예상 구성요소

### Entity (예상)
- `AccountGroup` - 그룹 (이름, 생성자, 멤버 목록)
- `GroupMember` - 그룹 멤버 (사용자, 역할, 참여일)
- `SharedTransaction` - 공유 거래 내역

### 패키지 위치 (예상)
```
interfaces/accountgroup/api/AccountGroupController.java
interfaces/accountgroup/web/AccountGroupWebController.java
application/accountgroup/service/AccountGroupService.java
application/accountgroup/command/{CreateGroup,InviteMember,ShareTransaction}Command.java
application/accountgroup/query/{GetGroup,GetGroupMembers,GetSharedTransactions}Query.java
domain/accountgroup/entity/{AccountGroup,GroupMember}.java
infrastructure/accountgroup/repository/AccountGroupRepository.java
```

## 참고
- AccountBook 도메인과 연관관계 설계 필요
- User(Auth 도메인)와 멤버 연결
