package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public Member[] membersV1(){
        return (Member[]) memberService.findMembers().toArray();
    }

    @GetMapping("api/v2/members")
    public Result memberV2(){
        List<MemberViewDto> collect = memberService.findMembers().stream()
                .map(m -> new MemberViewDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberViewDto{
        private String name;
    }

    @PostMapping("api/v1/members")
    public Long saveMemberV1(@RequestBody @Valid Member member){
        return memberService.join(member);
    }

    @PostMapping("api/v2/members")
    public synchronized Long saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(request.getAddress());
        return memberService.join(member);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") long id,
                               @RequestBody @Valid
                               UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
        private Address address;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private long id;
        private String name;
    }
}