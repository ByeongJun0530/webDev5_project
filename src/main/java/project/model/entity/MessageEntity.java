package project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meno; // 식별번호

    @Column(nullable = false, columnDefinition = "varchar(225)")
    private String metitle; // 제목
    @Column(nullable = false, columnDefinition = "text")
    private String mecontent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sendermno", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private MemberEntity sendermno; // 송신자

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receivermno", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private MemberEntity receivermno; // 송신자

    @Column(nullable = false)
    private boolean deleteBySender;
    @Column(nullable = false)
    private boolean deleteByReceiver;

}
