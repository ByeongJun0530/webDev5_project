package project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import project.model.dto.MessageDto;

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

    public void deleteBySender() {this.deleteBySender = true;}
    public void  deleteByReceiver() {this.deleteByReceiver = true;}
    public boolean isDeleted(){return isDeleteBySender() && isDeleteByReceiver();}

    // entity -> dto
    public MessageDto toDto() {
        return MessageDto.builder()
                .meno(this.meno)
                .metitle(this.metitle)
                .mecontent(this.mecontent)
                .receivermno(this.receivermno.getMno())
                .sendermno(this.sendermno.getMno())
                .build();
    }

}