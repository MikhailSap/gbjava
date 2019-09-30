package server;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Entity
@Table(name = "members")
@Data
public class Member {
    @Id
    private int id_member;
    private String nick;
    @Transient
    private DataInputStream in;
    @Transient
    private DataOutputStream out;
    @Transient
    private boolean isOnline;
}
