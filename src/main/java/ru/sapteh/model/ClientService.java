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
    @NonNull
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Client client;
    @NonNull
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Service service;
}
