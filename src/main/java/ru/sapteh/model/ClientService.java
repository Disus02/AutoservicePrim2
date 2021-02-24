package ru.sapteh.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "clientservice")
public class ClientService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private Date startTime;

    private String comment;
    @JoinColumn(name = "ClientID")
    @NonNull
    @ManyToOne
    private Client client;
    @JoinColumn(name = "ServiceID")
    @NonNull
    @ManyToOne
    private Service service;

    @Override
    public String toString() {
        return "ClientService{" +
                "startTime=" + startTime +
                ", client=" + client +
                ", service=" + service +
                '}';
    }
}
