package project.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.MemberEntity;
import project.model.entity.MessageEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    // 수신자로 메세지 찾기
    List<MessageEntity> findByReceivermno(MemberEntity receivermno);
    // 송신자로 메세지 찾기
    List<MessageEntity> findBySendermno(MemberEntity sendermno);
    // 삭제되지 않은 메세지 찾기
    List<MessageEntity> findBySendermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(MemberEntity sendermno);
    List<MessageEntity> findByReceivermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(MemberEntity receivermno);
}
