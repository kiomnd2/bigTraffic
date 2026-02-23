package kr.kiomn2.bigtraffic.interfaces.room.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kiomn2.bigtraffic.application.room.facade.RoomFacade;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.room.command.*;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomInvitesQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomMembersQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomsQuery;
import kr.kiomn2.bigtraffic.interfaces.room.dto.request.*;
import kr.kiomn2.bigtraffic.interfaces.room.dto.response.*;
import kr.kiomn2.bigtraffic.interfaces.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Room", description = "방 관리 API")
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomFacade roomFacade;
    private final RoomMapper roomMapper;

    @Operation(summary = "방 생성", description = "새로운 방을 생성합니다.")
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody RoomCreateRequest request) {

        CreateRoomCommand command = new CreateRoomCommand(
                user.getId(), request.getName(), request.getDescription(), request.getMaxMembers()
        );
        Room room = roomFacade.createRoom(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toResponse(room));
    }

    @Operation(summary = "방 목록 조회", description = "사용자가 참여 중인 방 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<RoomListResponse> getRooms(@AuthenticationPrincipal User user) {
        GetRoomsQuery query = new GetRoomsQuery(user.getId());
        List<Room> rooms = roomFacade.getRooms(query);
        return ResponseEntity.ok(roomMapper.toListResponse(rooms));
    }

    @Operation(summary = "방 상세 조회", description = "특정 방의 상세 정보를 조회합니다.")
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoom(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId) {

        GetRoomQuery query = new GetRoomQuery(user.getId(), roomId);
        Room room = roomFacade.getRoom(query);
        return ResponseEntity.ok(roomMapper.toResponse(room));
    }

    @Operation(summary = "방 수정", description = "방 정보를 수정합니다. (방장만 가능)")
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId,
            @Valid @RequestBody RoomUpdateRequest request) {

        UpdateRoomCommand command = new UpdateRoomCommand(
                user.getId(), roomId, request.getName(), request.getDescription(), request.getMaxMembers()
        );
        Room room = roomFacade.updateRoom(command);
        return ResponseEntity.ok(roomMapper.toResponse(room));
    }

    @Operation(summary = "방 해산", description = "방을 해산합니다. (방장만 가능)")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> dissolveRoom(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId) {

        DissolveRoomCommand command = new DissolveRoomCommand(user.getId(), roomId);
        roomFacade.dissolveRoom(command);
        return ResponseEntity.noContent().build();
    }

    // --- 멤버 관리 ---

    @Operation(summary = "방 멤버 목록 조회", description = "방의 멤버 목록을 조회합니다.")
    @GetMapping("/{roomId}/members")
    public ResponseEntity<List<RoomMemberResponse>> getRoomMembers(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId) {

        GetRoomMembersQuery query = new GetRoomMembersQuery(user.getId(), roomId);
        List<RoomMember> members = roomFacade.getRoomMembers(query);
        return ResponseEntity.ok(roomMapper.toMemberResponses(members));
    }

    @Operation(summary = "방 참여", description = "초대 코드를 사용하여 방에 참여합니다.")
    @PostMapping("/join")
    public ResponseEntity<RoomMemberResponse> joinRoom(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody JoinRoomRequest request) {

        JoinRoomCommand command = new JoinRoomCommand(user.getId(), request.getInviteCode());
        RoomMember member = roomFacade.joinRoom(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toMemberResponse(member));
    }

    @Operation(summary = "방 나가기", description = "방에서 나갑니다. (방장은 불가)")
    @DeleteMapping("/{roomId}/members/me")
    public ResponseEntity<Void> leaveRoom(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId) {

        LeaveRoomCommand command = new LeaveRoomCommand(user.getId(), roomId);
        roomFacade.leaveRoom(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "멤버 추방", description = "방에서 멤버를 추방합니다. (방장만 가능)")
    @DeleteMapping("/{roomId}/members/{targetUserId}")
    public ResponseEntity<Void> kickMember(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId,
            @PathVariable Long targetUserId) {

        KickMemberCommand command = new KickMemberCommand(user.getId(), roomId, targetUserId);
        roomFacade.kickMember(command);
        return ResponseEntity.noContent().build();
    }

    // --- 초대 관리 ---

    @Operation(summary = "초대 코드 생성", description = "방 초대 코드를 생성합니다. (방장만 가능)")
    @PostMapping("/{roomId}/invites")
    public ResponseEntity<RoomInviteResponse> createInvite(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId,
            @RequestBody(required = false) InviteCreateRequest request) {

        Integer expirationHours = request != null ? request.getExpirationHours() : null;
        CreateInviteCommand command = new CreateInviteCommand(user.getId(), roomId, expirationHours);
        RoomInvite invite = roomFacade.createInvite(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toInviteResponse(invite));
    }

    @Operation(summary = "초대 목록 조회", description = "방의 초대 목록을 조회합니다. (방장만 가능)")
    @GetMapping("/{roomId}/invites")
    public ResponseEntity<List<RoomInviteResponse>> getRoomInvites(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId) {

        GetRoomInvitesQuery query = new GetRoomInvitesQuery(user.getId(), roomId);
        List<RoomInvite> invites = roomFacade.getRoomInvites(query);
        return ResponseEntity.ok(roomMapper.toInviteResponses(invites));
    }

    @Operation(summary = "초대 취소", description = "초대 코드를 취소합니다. (방장만 가능)")
    @DeleteMapping("/{roomId}/invites/{inviteId}")
    public ResponseEntity<Void> revokeInvite(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId,
            @PathVariable Long inviteId) {

        RevokeInviteCommand command = new RevokeInviteCommand(user.getId(), roomId, inviteId);
        roomFacade.revokeInvite(command);
        return ResponseEntity.noContent().build();
    }

    // --- 가계부 연결 ---

    @Operation(summary = "가계부 연결", description = "방에 가계부를 연결합니다.")
    @PostMapping("/{roomId}/account-books")
    public ResponseEntity<RoomAccountBookResponse> linkAccountBook(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId,
            @Valid @RequestBody LinkAccountBookRequest request) {

        LinkAccountBookCommand command = new LinkAccountBookCommand(user.getId(), roomId, request.getAccountBookId());
        RoomAccountBook roomAccountBook = roomFacade.linkAccountBook(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toAccountBookResponse(roomAccountBook));
    }

    @Operation(summary = "연결된 가계부 목록 조회", description = "방에 연결된 가계부 목록을 조회합니다.")
    @GetMapping("/{roomId}/account-books")
    public ResponseEntity<List<RoomAccountBookResponse>> getRoomAccountBooks(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId) {

        List<RoomAccountBook> accountBooks = roomFacade.getRoomAccountBooks(roomId, user.getId());
        return ResponseEntity.ok(roomMapper.toAccountBookResponses(accountBooks));
    }

    @Operation(summary = "가계부 연결 해제", description = "방에서 가계부 연결을 해제합니다. (방장만 가능)")
    @DeleteMapping("/{roomId}/account-books/{accountBookId}")
    public ResponseEntity<Void> unlinkAccountBook(
            @AuthenticationPrincipal User user,
            @PathVariable Long roomId,
            @PathVariable Long accountBookId) {

        UnlinkAccountBookCommand command = new UnlinkAccountBookCommand(user.getId(), roomId, accountBookId);
        roomFacade.unlinkAccountBook(command);
        return ResponseEntity.noContent().build();
    }
}
