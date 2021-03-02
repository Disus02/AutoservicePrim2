package ru.sapteh.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gender gender = (Gender) o;
        return code == gender.code &&
                Objects.equals(name, gender.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
