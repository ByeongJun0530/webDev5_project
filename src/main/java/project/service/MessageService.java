package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.model.dto.MemberDto;
import project.model.dto.MessageDto;
import project.model.entity.MemberEntity;
import project.model.entity.MessageEntity;
import project.model.repository.MemberRepository;
import project.model.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageService {
    @Autowired MessageRepository messageRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    // 메세지 보내기
    public boolean send(MessageDto messageDto){
        try {
            int mno = 0;
            if(memberService.getSession() != null) {
                mno = memberRepository.findByMemail(memberService.getSession()).getMno();
            }

            // dto -> entity
            MessageEntity messageEntity = messageDto.toEntity();
            // 로그인된 회원 발신자 설정
            MemberEntity sendEntity = memberRepository.findById(mno).get();
            messageEntity.setSendermno(sendEntity);
            // 수신자 존재 여부 확인 및 설정
            Optional<MemberEntity> optionalReceiver = memberRepository.findById(messageDto.getReceivermno());
            if(!optionalReceiver.isPresent()) {
                System.out.println("메세지 전송 실패: 수신자가 존재하지 않음");
                return false;
            }
            messageEntity.setReceivermno(optionalReceiver.get());

            // 메세지 저장
            messageRepository.save(messageEntity);
            System.out.println("메세지 전송 성공");
            return true;
        } catch (Exception e) {
            System.out.println("메세지 전송 실패: " + e);
            return false;
        }
    }

    // 받은 메세지 조회 추가 (삭제된 메세지 필터링)
    @Transactional(readOnly = true)
    public List<MessageDto> FindReceiverMessage(int mno){
        // 로그인 상태 확인
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("받은 메세지 조회 실패: 로그인 필요 또는 권한 없음");
            return new ArrayList<>();
        }
        System.out.println("보낸 메세지 조회: " + mno);

        // 회원 찾기
        Optional<MemberEntity> memberOtp = memberRepository.findById(mno);
        List<MessageDto> list = new ArrayList<>();

        if(memberOtp.isPresent()){
            System.out.println("회원 찾음: " + memberOtp.get().getMemail());
            // 받은 메세지 찾기
            // 삭제x 메세지만 반환
            List<MessageEntity> messageEntities = messageRepository.findByReceivermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(memberOtp.get());
            System.out.println("찾은 메세지 수: " + messageEntities.size());

            // 엔티티 리스트 -> dto
            messageEntities.forEach(entity -> {
                MessageDto dto = entity.toDto();
                // 송신자와 수신자 아이디 설정 => 화면에서 회원 번호가 아닌 실제 회원 아이디를 표시하기 위함
                dto.setSendname(entity.getSendermno().getMemail());
                dto.setReceivername(entity.getReceivermno().getMemail());

                list.add(dto);
                System.out.println("메세지 변환: " + dto.getMetitle());
            });
        } else {
            System.out.println("회원 찾을 수 없음");
        }
        return list;
    }

    // 보낸 메세지 조회 추가(삭제된 메세지 필터링)
    @Transactional(readOnly = true)
    public List<MessageDto> FindSendMessage(int mno) {
        // 로그인 상태
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("받은 메세지 조회 실패: 로그인 필요 또는 권한 없음");
            return new ArrayList<>();
        }
        System.out.println("보낸 메세지 조회: " + mno);
        // 회원 찾기
        Optional<MemberEntity> memberOtp = memberRepository.findById(mno);
        List<MessageDto> list = new ArrayList<>();

        if(memberOtp.isPresent()){
            System.out.println("회원 찾음: " + memberOtp.get().getMemail());
            // 보낸 메세지 찾기
            List<MessageEntity> messageEntities = messageRepository.findBySendermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(memberOtp.get());
            System.out.println("찾은 메세지 수 " + messageEntities.size());

            // 엔티티 리스트 -> dto
            messageEntities.forEach(entity -> {
                MessageDto dto = entity.toDto();
                // 송신자와 수신자 아이디 설정
                dto.setSendname(entity.getSendermno().getMemail());
                dto.setReceivername(entity.getReceivermno().getMemail());

                list.add(dto);
                System.out.println("메세지변환: " + dto.getMetitle());
            });
        } else {
            System.out.println("회원 찾을 수 없음");
        }
        return list;
    }
    // 보낸 메세지 삭제
    @Transactional
    public boolean deleteSendMessage(int meno, int mno){
        // 로그인 상태 확인
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("보낸 메세지 삭제 실패: 로그인 필요 또는 권한 없음");
            return false;
        }
        System.out.println("[보낸 메세지 삭제]meno: " + meno + "mno: " + mno);
        try {
            Optional<MessageEntity> messageOtp = messageRepository.findById(meno);
            if(messageOtp.isPresent()) {
                MessageEntity message = messageOtp.get();
                System.out.println("[보낸 메세지 삭제]메세지 찾음: " +message.getMetitle() );

                // 송신자 확인
                if(message.getSendermno().getMno() == mno) {
                    message.deleteBySender();
                    messageRepository.save(message); // 상태 업데이트
                    System.out.println("[보낸메세지 삭제] 삭제 성공");
                    return true;
                }else {
                    System.out.println("[보낸 메세지 삭제] 권한 없음: 송신자가 아님");
                }
            }else {
                System.out.println("[보낸 메세지 삭제] 메세지를 찾을 수 없음");
            }
            return false;
        } catch (Exception e) {
            System.out.println("보낸 메세지 삭제 실패: " +e);
            return false;
        }
    }

    // 받은 메세지 삭제
    @Transactional
    public boolean deleteReceiverMessage(int meno, int mno) {
        // 로그인 상태 확인
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("받은 메세지 삭제 실패: 로그인 필요 또는 권한 없음");
            return false;
        }
        System.out.println("[받은 메세지 삭제] meno: " + meno + ", mno: " + mno);
        try {
            Optional<MessageEntity> messageOpt = messageRepository.findById(meno);

            if(messageOpt.isPresent()) {
                MessageEntity message = messageOpt.get();
                System.out.println("받은 메세지 삭제 메세지 찾음: " + message.getMetitle());

                // 수신자만 삭제 가능
                if(message.getReceivermno().getMno() == mno) {
                    message.deleteByReceiver();
                    messageRepository.save(message); // 상태 업데이트
                    System.out.println("받은 메세지 삭제 성공");
                    return true;
                } else {
                    System.out.println("받은 메세지 삭제 권한 없음: 수신자가 아님");
                }
            } else {
                System.out.println("받은 메세지 삭제 메세지를 찾을 수 없음");
            }
            return false;
        } catch (Exception e) {
            System.out.println("받은 메세지 삭제 실패" + e);
            return false;
        }
    }

    public int mno() {
        int mno=0;
        if(memberService.getSession()!=null) {
            mno = memberRepository.findByMemail(memberService.getSession()).getMno();
        }
        return mno;
    }

    // ID로 회원번호 찾기
    public Integer findMnoById(String mid) {
        try {
            MemberEntity member = memberRepository.findByMemail(mid);
            if (member != null) {
                return member.getMno();
            }
            return null;
        } catch (Exception e) {
            System.out.println("회원번호 조회 실패: " + e);
            return null;
        }
    }
}
