package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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


@Service
public class MessageService {
    @Autowired MessageRepository messageRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    // 보내기
    public boolean send(MessageDto messageDto){
        try {
            int mno = 0;
            if(memberService.getSession() != null) {
                mno = memberRepository.findByMemail(memberService.getSession()).getMno();
            }
            MessageEntity messageEntity = messageDto.toEntity();
            MemberEntity sendEntity = memberRepository.findById(mno).get();
            messageEntity.setSendermno(sendEntity);

            Optional<MemberEntity> optionalReceiver = memberRepository.findById(messageDto.getReceivermno());
            if(!optionalReceiver.isPresent()){
                System.out.println("메세지 전송 실패(수신자 미존재)");
                return false;
            }
            messageEntity.setReceivermno(optionalReceiver.get());

            // 메세지 저장
            messageRepository.save(messageEntity);
            System.out.println("메세지 전송 성공");
            return true;
        } catch (Exception e) {
            System.out.println("메세지 전송 실패" + e);
            return false;
        }
    }

    //  받은 메세지 조회
    @Transactional(readOnly = true)
    public List<MessageDto> FindReceiverMessage(int mno) {
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("받은 메세지 조회 실패");
            return new ArrayList<>();
        }
        System.out.println("보낸 메세지 조회: " + mno);
        Optional<MemberEntity> memberOtp = memberRepository.findById(mno);
        List<MessageDto> list = new ArrayList<>();

        if(memberOtp.isPresent()) {
            System.out.println("회원 찾음: " +memberOtp.get().getMemail());
            List<MessageEntity> messageEntities = messageRepository.findByReceivermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(memberOtp.get());
            System.out.println("찾은 메세지 수: " + messageEntities.size());

            messageEntities.forEach(entity -> {
                MessageDto dto = entity.toDto();
                dto.setSendmid(entity.getSendermno().getMemail());
                dto.setReceivermid(entity.getReceivermno().getMemail());

                list.add(dto);
                System.out.println("메세지 변환: " + dto.getMetitle());
            });
        } else {
            System.out.println("회원 찾기 불가");
        }
        return list;
    }

    // 보낸 메세지 조회
    @Transactional(readOnly = true)
    public List<MessageDto> FindSendMessage(int mno) {
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("로그인 필요 또는 권한 없음");
            return new ArrayList<>();
        }
        System.out.println("보낸 메세지 조회: " +mno);
        Optional<MemberEntity> memberOtp = memberRepository.findById(mno);
        List<MessageDto> list = new ArrayList<>();

        if(memberOtp.isPresent()) {
            System.out.println("회원 찾음: " + memberOtp.get().getMemail());
            List<MessageEntity> messageEntities = messageRepository.findBySendermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(memberOtp.get());

            messageEntities.forEach(entity -> {
                MessageDto dto = entity.toDto();
                dto.setSendmid(entity.getSendermno().getMemail());
                dto.setReceivermid(entity.getReceivermno().getMemail());

                list.add(dto);
                System.out.println("메세지 변환: " + dto.getMetitle());
            });
        }else {
            System.out.println("회원 찾을 수 없음");
        }
        return list;
    }

    // MessageService.java 파일에 추가할 코드

    // 메세지 삭제 (발신자)
    @Transactional
    public boolean deleteSendMessage(int meno, int mno) {
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("보낸 메세지 삭제 실패");
            return false;
        }
        System.out.println("보낸 메세지 삭제");
        try {
            Optional<MessageEntity> messageOtp = messageRepository.findById(meno);
            if(messageOtp.isPresent()) {
                MessageEntity message = messageOtp.get();
                System.out.println("보낸 메세지 삭제-메세지 찾음");

                if(message.getSendermno().getMno() == mno) {
                    message.deleteBySender();
                    messageRepository.save(message);
                    System.out.println("보낸 메세지 삭제 - 삭제 성공");
                    return true;
                }else {
                    System.out.println("보낸 메세지 삭제 - 권한 없음");
                }
            }else {
                System.out.println("보낸 메세지 삭제 - 권한 없음");
            }
            return false;
        }catch (Exception e) {
            System.out.println("보낸 메세지 삭제 실패" + e);
            return false;
        }
    }

    // 메세지 삭제 (수신자)
    @Transactional
    public boolean deleteByReceiver(int meno, int mno) {
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("받은 메세지 삭제 실패 - 로그인 필요 또는 권한 없음");
            return false;
        }

        System.out.println("받은 메세지 삭제");
        try {
            Optional<MessageEntity> messageOtp = messageRepository.findById(meno);

            if(messageOtp.isPresent()) {
                MessageEntity message = messageOtp.get();
                System.out.println("받은 메세지 삭제 메세지 찾음");

                if(message.getReceivermno().getMno() == mno) {
                    message.deleteByReceiver();
                    messageRepository.save(message);
                    System.out.println("받은 메세지 삭제 성공");
                    return true;
                }else {
                    System.out.println("받은 메세지 삭제 권한 없음");
                }
            }else {
                System.out.println("받은 메세지 삭제 메시지를 찾을 수 없음");
            }
            return false;
        } catch (Exception e) {
            System.out.println("받은 메세지 삭제 실패: " + e);
            return false;
        }
    }


    // 회원 찾기
    public int mno() {
        int mno = 0;
        if(memberService.getSession() != null) {
            mno = memberRepository.findByMemail(memberService.getSession()).getMno();
        }
        return mno;
    }

    public Integer findMnoById(String memail) {
        try {
            MemberEntity member = memberRepository.findByMemail(memail);
            if(member != null) {
                return member.getMno();
            }
            return null;
        }catch (Exception e) {
            System.out.println("회원 정보 조회 실패: " + e);
            return null;
        }
    }

    // 단일 메시지 상세 조회 (읽음 상태 업데이트 없이)
    @Transactional(readOnly = true)
    public MessageDto findMessageById(int meno, int mno) {
        MemberDto loginDto = memberService.getMyInfo();
        if(loginDto == null || loginDto.getMno() != mno) {
            System.out.println("메시지 상세 조회 실패 - 로그인 필요 또는 권한 없음");
            return null;
        }

        try {
            Optional<MessageEntity> messageOpt = messageRepository.findById(meno);
            if(messageOpt.isPresent()) {
                MessageEntity message = messageOpt.get();

                // 접근 권한 확인 (발신자 또는 수신자만 조회 가능)
                if(message.getSendermno().getMno() == mno || message.getReceivermno().getMno() == mno) {
                    MessageDto dto = message.toDto();
                    dto.setSendmid(message.getSendermno().getMemail());
                    dto.setReceivermid(message.getReceivermno().getMemail());
                    return dto;
                } else {
                    System.out.println("메시지 상세 조회 실패 - 권한 없음");
                }
            } else {
                System.out.println("메시지 상세 조회 실패 - 메시지 없음");
            }
            return null;
        } catch (Exception e) {
            System.out.println("메시지 상세 조회 실패: " + e);
            return null;
        }
    }


    //웹소켓 활용 될 부분
//    @Autowired private SimpMessagingTemplate messagingTemplate;  // WebSocket 메시지 전송 도구
//    @Transactional
//    public boolean send(MessageDto messageDto) {
//        try {
//            int mno = messageDto.getSendermno();  // 발신자 ID
//            int receiverMno = messageDto.getReceivermno();  // 수신자 ID
//
//            // 발신자 확인
//            Optional<MemberEntity> senderOpt = memberRepository.findById(mno);
//            if (!senderOpt.isPresent()) {
//                System.out.println("메시지 전송 실패: 발신자 없음");
//                return false;
//            }
//
//            // 수신자 확인
//            Optional<MemberEntity> receiverOpt = memberRepository.findById(receiverMno);
//            if (!receiverOpt.isPresent()) {
//                System.out.println("메시지 전송 실패: 수신자 없음");
//                return false;
//            }
//
//            // 메시지 저장
//            MessageEntity messageEntity = messageDto.toEntity();
//            messageEntity.setSendermno(senderOpt.get());  // 발신자 저장
//            messageEntity.setReceivermno(receiverOpt.get());  // 수신자 저장
//            messageRepository.save(messageEntity);
//
//            // WebSocket으로 메시지 전송
//            messagingTemplate.convertAndSend("/topic/messages", messageDto);
//            System.out.println("메시지 전송 성공");
//
//            return true;
//        } catch (Exception e) {
//            System.out.println("메시지 전송 실패: " + e.getMessage());
//            return false;
//        }
//    }


}