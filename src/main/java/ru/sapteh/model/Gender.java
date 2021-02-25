package ru.sapteh.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "gender")
public class Gender {
    @Id
    @NonNull
    private char code;

    @NonNull
    private String name;

    @Override
    public String toString() {
        return String.format("%s",code);
    }
}
