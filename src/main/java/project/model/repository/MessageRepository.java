package project.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.MemberEntity;
import project.model.entity.MessageEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    // 받은 메세지 조회 (삭제되지 않은 메세지만)
    List<MessageEntity> findByReceivermnoAndDeleteByReceiverFalse(MemberEntity receivermno);

    // 보낸 메세지 조회 (삭제되지 않은 메세지만)
    List<MessageEntity> findBySendermnoAndDeleteBySenderFalse(MemberEntity sendermno);

    // 양쪽 모두 삭제되지 않은 메세지 조회
    List<MessageEntity> findByReceivermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(MemberEntity receivermno);
    List<MessageEntity> findBySendermnoAndDeleteBySenderFalseAndDeleteByReceiverFalse(MemberEntity sendermno);
}