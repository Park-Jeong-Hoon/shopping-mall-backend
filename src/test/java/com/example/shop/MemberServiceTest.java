package com.example.shop;

import com.example.shop.dto.JoinDto;
import com.example.shop.model.Address;
import com.example.shop.repository.MemberRepository;
import com.example.shop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void join() throws Exception {

        // given
        Address address = new Address("region1", "road1", "home1", "123-456");
        JoinDto joinDto = new JoinDto();
        joinDto.setUsername("test");
        joinDto.setPassword("1234");
        joinDto.setName("test");
        joinDto.setPhone("01012341234");
        joinDto.setEmail("abc@com");
        joinDto.setAddress(address);

        // when
        Long saveId = memberService.join(joinDto);

        // then
        Assertions.assertEquals("test", memberRepository.findById(saveId).get().getUsername());
    }

    @Test
    public void validateDuplicate() throws Exception {

        // given
        Address address = new Address("region1", "road1", "home1", "123-456");

        JoinDto joinDto1 = new JoinDto();
        joinDto1.setUsername("test");
        joinDto1.setPassword("1234");
        joinDto1.setName("test");
        joinDto1.setPhone("01012341234");
        joinDto1.setEmail("abc@com");
        joinDto1.setAddress(address);

        JoinDto joinDto2 = new JoinDto();
        joinDto2.setUsername("test");
        joinDto2.setPassword("1234");
        joinDto2.setName("test");
        joinDto2.setPhone("01012341234");
        joinDto2.setEmail("abc@com");
        joinDto2.setAddress(address);

        // when
        memberService.join(joinDto1);

        // then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(joinDto2);
        }, "중복 회원 예외가 발생해야 한다.");
    }
}
