package kr.kiomn2.bigtraffic.interfaces.room.web;

import kr.kiomn2.bigtraffic.application.room.facade.RoomFacade;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomInvitesQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomMembersQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomsQuery;
import kr.kiomn2.bigtraffic.interfaces.room.dto.response.*;
import kr.kiomn2.bigtraffic.interfaces.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomWebController {

    private final RoomFacade roomFacade;
    private final RoomMapper roomMapper;

    @GetMapping
    public String roomList(@AuthenticationPrincipal User user, Model model) {
        log.info("방 목록 페이지 접근 - userId: {}", user.getId());

        List<Room> rooms = roomFacade.getRooms(new GetRoomsQuery(user.getId()));
        RoomListResponse response = roomMapper.toListResponse(rooms);

        model.addAttribute("rooms", response.getRooms());
        model.addAttribute("totalCount", response.getTotalCount());
        model.addAttribute("username", user.getName());

        return "room/room-main";
    }

    @GetMapping("/create")
    public String createRoomForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user.getName());
        return "room/create-room";
    }

    @GetMapping("/{roomId}")
    public String roomDetail(@AuthenticationPrincipal User user, @PathVariable Long roomId, Model model) {
        log.info("방 상세 페이지 접근 - userId: {}, roomId: {}", user.getId(), roomId);

        try {
            Room room = roomFacade.getRoom(new GetRoomQuery(user.getId(), roomId));
            List<RoomMember> members = roomFacade.getRoomMembers(new GetRoomMembersQuery(user.getId(), roomId));
            List<RoomAccountBook> accountBooks = roomFacade.getRoomAccountBooks(roomId, user.getId());

            boolean isHost = room.getHostUserId().equals(user.getId());

            List<RoomInvite> invites = List.of();
            if (isHost) {
                invites = roomFacade.getRoomInvites(new GetRoomInvitesQuery(user.getId(), roomId));
            }

            model.addAttribute("room", roomMapper.toResponse(room));
            model.addAttribute("members", roomMapper.toMemberResponses(members));
            model.addAttribute("accountBooks", roomMapper.toAccountBookResponses(accountBooks));
            model.addAttribute("invites", roomMapper.toInviteResponses(invites));
            model.addAttribute("isHost", isHost);
            model.addAttribute("currentUserId", user.getId());
            model.addAttribute("username", user.getName());
            model.addAttribute("memberCount", members.size());

            return "room/room-detail";

        } catch (Exception e) {
            log.error("방 상세 페이지 로드 실패 - userId: {}, roomId: {}", user.getId(), roomId, e);
            model.addAttribute("errorMessage", "방 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }
}
